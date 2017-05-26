package cn.buk.api.wechat.service;


import cn.buk.api.wechat.dao.WeixinDao;
import cn.buk.api.wechat.dto.*;
import cn.buk.api.wechat.entity.*;
import cn.buk.api.wechat.util.EncoderHandler;
import cn.buk.api.wechat.util.HttpUtil;
import cn.buk.api.wechat.util.SignUtil;
import cn.buk.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.buk.api.wechat.util.HttpUtil.sendResponse;

/**
 * Created by yfdai on 2017/2/6.
 */
@Component
public class WeixinServiceImpl implements WeixinService {

    private static Logger logger = Logger.getLogger(WeixinServiceImpl.class);

    /**
     * 文本的客服消息
     */
    private final static String WX_CUSTOM_MSGTYPE_TEXT = "text";
    /**
     * 图文(news)的客服消息
     */
    private final static String WX_CUSTOM_MSGTYPE_NEWS = "news";

    @Value("${Weixin_Id}")
    private int weixinId;

    /**
     * 以下三个参数是微信接口需要用到的
     */
    @Value("${Weixin_AppId}")
    private String appId;

    @Value("${Weixin_AppSecret}")
    private String appSecret;

    @Value("${Weixin_Token}")
    private String weixinToken;


    @Autowired
    private WeixinDao weixinDao;


    public String getAppid() {
        return this.appId;
    }

    public JsSdkParam getJsSdkConfig(String jsapi_url) {
        JsSdkParam jsapiParam = new JsSdkParam();
        jsapiParam.setAppId(this.appId);

        Token ticket = getJsSdkTicket();


        // 3. 签名
        Map<String, String> ret = SignUtil.sign(ticket.getAccess_token(), jsapi_url);
        jsapiParam.setTimestamp(ret.get("timestamp"));
        jsapiParam.setNonceStr(ret.get("nonceStr"));
        jsapiParam.setSignature(ret.get("signature"));

        jsapiParam.setUrl(jsapi_url);

        return jsapiParam;
    }

    /**
     * @param weixinOauthCode code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     */
    public WeixinOauthToken getOauthToken(final String weixinOauthCode) {
        final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", appId));
        params.add(new BasicNameValuePair("secret", appSecret));
        params.add(new BasicNameValuePair("code", weixinOauthCode));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        String jsonStr = HttpUtil.getUrl(url, params);

        //判断返回结果
        JSONObject param = JSON.parseObject(jsonStr);

        if (param.get("errcode") == null) {
            WeixinOauthToken token = new WeixinOauthToken();
            token.setAccess_token((String) param.get("access_token"));
            token.setRefresh_token((String) param.get("refresh_token"));
            token.setOpenid((String) param.get("openid"));
            token.setScope((String) param.get("scope"));
            token.setExpires_in((Integer) param.get("expires_in"));

            token.setWeixinId(this.weixinId);
            weixinDao.createWeixinOauthToken(token);

            return token;
        } else {
            return null;
        }
    }


    public boolean verifyWeixinSource(String signature, String timestamp, String nonce) {
        try {
            ArrayList<String> al = new ArrayList<>();
            al.add(this.weixinToken);
            al.add(timestamp);
            al.add(nonce);
            Collections.sort(al);

            String allString = "";
            for (String temp : al) {
                allString += temp;
            }

            String mySignature = EncoderHandler.encode("SHA1", allString);

            return mySignature.compareTo(signature) == 0;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * 获取微信自定义菜单
     */
    public String getCustomMenu() {
        Token token = getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));

        String jsonStr = HttpUtil.getUrl(url, params);

        try {
            jsonStr = new String(jsonStr.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }

    /**
     * 从数据库中读取微信自定义菜单设置，
     * 创建微信自定义菜单
     */
    public String createCustomMenu() {
        WeixinMenu wm = new WeixinMenu();

        List<WeixinCustomMenu> menus = weixinDao.searchCustomMenus(this.weixinId);

        String url;

        //一级菜单
        for(WeixinCustomMenu m1: menus) {
            if (m1.getLevel() != 1) continue;

            WeixinMenuItem dto = new WeixinMenuItem();
            wm.getButton().add(dto);
            dto.setName(m1.getName());

            //二级菜单
            for(WeixinCustomMenu m2: menus) {
                if (m2.getLevel() != 2) continue;
                if (m2.getParentId() != m1.getId()) continue;

                if (dto.getSub_button() == null) {
                    dto.setSub_button(new ArrayList<WeixinMenuItem>());
                }

                WeixinMenuItem dto2 = new WeixinMenuItem();
                dto.getSub_button().add(dto2);
                dto2.setName(m2.getName());
                if (m2.getType().equalsIgnoreCase("VIEW")) {
                    dto2.setType(m2.getType());
                    url = buildUrlInWeixin(m2.getUrl());
                    dto2.setUrl(url);
                } else if (m2.getType().equalsIgnoreCase("click")) {
                    dto2.setType(m2.getType());
                    dto2.setKey(m2.getKey());
                }

            }
        }

        String jsonBody = JSON.toJSON(wm).toString();
        logger.info(jsonBody);

        Token token = getToken();
        url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token.getAccess_token() ;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        return HttpUtil.postUrl(url, jsonBody);
    }


    private synchronized Token getToken() {
        Token token = weixinDao.retrieveWeixinToken(this.weixinId, Token.WEIXIN_SERVICE_TOKEN);

        if (token == null || DateUtil.getPastSeconds(token.getCreateTime()) >= token.getExpires_in()) {
            token = refreshWeixinToken();
        }

        return token;
    }

    /**
     * 获取js-sdk ticket, 可刷新
     */
    private synchronized Token getJsSdkTicket() {
        Token token = weixinDao.retrieveWeixinToken(this.weixinId, Token.WEIXIN_JS_SDK_TICKET);

        if (token == null || DateUtil.getPastSeconds(token.getCreateTime()) >= token.getExpires_in()) {
            token = refreshWeixinJsSdkTicket();
        }

        return token;
    }

    /**
     * 重新获取weixin的access token
     */
    private Token refreshWeixinToken() {
        final String url = "https://api.weixin.qq.com/cgi-bin/token?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "client_credential"));
        params.add(new BasicNameValuePair("appid", appId));
        params.add(new BasicNameValuePair("secret", appSecret));

        String jsonStr = HttpUtil.getUrl(url, params);

        //判断返回结果
        JSONObject param = (JSONObject) JSON.parse(jsonStr);

        Token token = new Token();
        token.setAccess_token((String) param.get("access_token"));
        token.setExpires_in((Integer) param.get("expires_in"));
        token.setWeixinId(this.weixinId);

        weixinDao.createWeixinToken(token);

        return token;
    }

    /**
     * 获取js sdk需要的ticket
     */
    private Token refreshWeixinJsSdkTicket() {
        Token accessToken = getToken();

        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", accessToken.getAccess_token()));
        params.add(new BasicNameValuePair("type", "jsapi"));

        String jsonStr = HttpUtil.getUrl(url, params);
        logger.info(jsonStr);

        //判断返回结果
        JSONObject param = (JSONObject) JSON.parse(jsonStr);

        Token ticket = new Token();
        ticket.setAccess_token((String) param.get("ticket"));
        ticket.setExpires_in((Integer) param.get("expires_in"));
        ticket.setWeixinType(Token.WEIXIN_JS_SDK_TICKET);

        weixinDao.createWeixinToken(ticket);

        return ticket;
    }

    /**
     * 获取微信素材列表
     */
    public String getMaterials() {
        WeixinMediasRequest request = new WeixinMediasRequest();
        request.setType("image");
        request.setOffset(0);
        request.setCount(20);

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        Token token = getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + token.getAccess_token();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        String result = HttpUtil.postUrl(url, jsonBody);

        try {
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");

            WeixinMediasResponse rs = JSON.parseObject(result, WeixinMediasResponse.class);
            if (rs != null) {
                logger.info(rs.getTotal_count());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * 根据openid获取用户信息
     */
    public WeixinUserInfo getUserInfo(String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", this.getToken().getAccess_token()));
        params.add(new BasicNameValuePair("openid", openid));
        params.add(new BasicNameValuePair("lang", "zh_CN"));

        String jsonStr = HttpUtil.getUrl(url, params);

        logger.debug(url);
        logger.debug(jsonStr);

        return JSON.parseObject(jsonStr, WeixinUserInfo.class);
    }

    /**
     * 处理微信公众号里面的事件, 以客服消息发送给用户
     */
    public void processWeixinEvent(HttpServletResponse response, WxData rq) {
        if ("subscribe".equalsIgnoreCase(rq.getEvent())) {
            List<Object> articles = new ArrayList<>();

            WxArticle article = new WxArticle();
            article.setTitle("新注册会员立送1000积分");
            article.setDescription("竭诚为您提供优质服务，欢迎您注册会员。");
            article.setPicurl("");
            String url0 = "";
            String url = buildUrlInWeixin(url0);
            article.setUrl(url);
            articles.add(article);

            article = new WxArticle();
            article.setTitle("体验优质服务，现在就开始吧");
            article.setDescription("欢迎来体验我们的优质服务");
            article.setPicurl("");
            url0 = "";
            url = buildUrlInWeixin(url0);
            article.setUrl(url);
            articles.add(article);

            this.sendCustomMessage(rq.getFromUserName(), WX_CUSTOM_MSGTYPE_NEWS, null, articles);

        } else if ("unsubscribe".equalsIgnoreCase(rq.getEvent())) {
            logger.warn(rq.getFromUserName() + " unsubscribe.");

        } else if ("CLICK".equalsIgnoreCase(rq.getEvent())) {
            logger.debug(rq.getEventKey() + ".");
        } else if ("TEMPLATESENDJOBFINISH".equalsIgnoreCase(rq.getEvent())) {
            //在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中。
            logger.info(rq.getMsgId() + ", " + rq.getStatus());
        }
    }

    /**
     * 同步微信关注用户的OpenId到本地
     */
    public int syncUserList() {
        Token token = getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));
        params.add(new BasicNameValuePair("next_openid", ""));

        String jsonStr = HttpUtil.getUrl(url, params);

        //判断返回结果
        JSONObject jsonResult = JSONObject.parseObject(jsonStr);
        int total = (Integer) jsonResult.get("total");
        int count = (Integer) jsonResult.get("count");
        JSONObject dataObject = (JSONObject)jsonResult.get("data");
        JSONArray array = dataObject.getJSONArray("openid");

        for(int i = 0; i < array.size(); i++) {
            String openId = (String)array.get(i);
            WeixinUser user = new WeixinUser();
            user.setWeixinOpenId(openId);


            WeixinUserInfo userDetail = getUserInfo(openId);
            if (userDetail != null ) {
                user.setSubscribe(userDetail.getSubscribe());
                if (userDetail.getSubscribe() == 1) {
                    BeanUtils.copyProperties(userDetail, user);
                    user.setSubscribe_time(DateUtil.timestampToDate(userDetail.getSubscribe_time() * 1000));
                }
            }

            // 将用户列表保存到本地
            if (weixinDao.searchWeixinUser(user.getOwnerId(), user.getWeixinOpenId()) == null) {
                int status = weixinDao.createWeixinUser(user);
                if (status == -100) {
                    user.setNickname("");
                    status = weixinDao.createWeixinUser(user);
                }
            }
        }

        jsonStr = "total: " + total + ", count: " + count;

        return count;
    }

    /**
     * 同步消息模板到本地
     */
    public List<WeixinTemplate> syncTemplates() {
        List<WeixinTemplate> results = new ArrayList<>();

        Token token = getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));

        String jsonStr = HttpUtil.getUrl(url, params);

        WxTemplateList templateList =  JSON.parseObject(jsonStr, WxTemplateList.class);
        List<WxTemplate> wxTemplates = templateList.getTemplate_list();

        for(WxTemplate t0: wxTemplates) {
            WeixinTemplate t1 = new WeixinTemplate();
            results.add(t1);

            BeanUtils.copyProperties(t0, t1);

            //保存t1在本地数据库
            t1.setOwnerId(this.weixinId);

            if (weixinDao.searchWeixinTemplate(this.weixinId, t1.getTemplate_id()) == null) {
                 weixinDao.createWeixinTemplate(t1);
            }
        }

        return results;
    }

    /**
     * 发送模板消息
     */
    public String sendTemplateMsg(WxTemplateSend wxTplRq) {
        String jsonBody = JSON.toJSONString(wxTplRq);
        logger.debug(jsonBody);

        Token token = getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token.getAccess_token() ;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        String result = HttpUtil.postUrl(url, jsonBody);

        try {
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            logger.debug(result);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * 处理微信推送过来的 用户消息和开发者需要的事件推送
     */
    public void processWeixinMessage(HttpServletRequest request, HttpServletResponse response) {

        final String requestXml = this.readInputXml(request);
        logger.debug("request xml: " + requestXml);

        WxData rq = WxData.fromXml(requestXml);

        if ("text".equalsIgnoreCase(rq.getMsgType()) ||
                "image".equalsIgnoreCase(rq.getMsgType()) ||
                "voice".equalsIgnoreCase(rq.getMsgType())
                ) {

            // 转发消息给客服系统
            WxData rs = new WxData();
            rs.setMap("ToUserName", rq.getFromUserName());
            rs.setMap("FromUserName", rq.getToUserName());
            rs.setMap("CreateTime", DateUtil.getCurDateTime().getTime() / 1000);
            rs.setMap("MsgType", "transfer_customer_service");

            String xml = rs.toXml();
            logger.debug(xml);

            sendResponse(response, xml);

            return;
        }

        sendResponse(response, "success"); // 向微信服务器发送 success, 稍后用客服消息发送结果给客户

        if ("event".equalsIgnoreCase(rq.getMsgType())) {
            this.processWeixinEvent(response, rq);

            return;
        }
    }

    @Override
    public List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc) {
        return weixinDao.searchSubscribers(this.weixinId, sc);
    }

    @Override
    public Token searchAccessToken(int enterpriseId) {
        return this.getToken();
    }

    @Override
    public WeixinTemplate searchWeixinTemplate(String id) {
        return weixinDao.searchWeixinTemplate(this.weixinId, id);
    }

    @Override
    public List<WeixinTemplate> searchTemplates(int enterpriseId) {
        if (enterpriseId == this.weixinId)
            return weixinDao.searchWeixinTemplates(this.weixinId);
        else
            return new ArrayList<>();
    }

    /**
     * 读取微信转发过来的xml数据
     */
    private String readInputXml(HttpServletRequest request) {
        String result = null;
        BufferedReader br;

        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将实际url和授权url绑定
     */
    private String buildUrlInWeixin(String url0) {
        try {
            return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                    this.appId +
                    "&redirect_uri=" + URLEncoder.encode(url0, "UTF-8") + "&response_type=code&scope=snsapi_base&state=#wechat_redirect";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 发送客户消息给用户
     */
    private String sendCustomMessage(final String touser, final String msgType, final String content, List<Object> articles) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", touser);
        jsonObject.put("msgtype", msgType);

        if (WX_CUSTOM_MSGTYPE_TEXT.equalsIgnoreCase(msgType)) {
            // 发送文本消息
            JSONObject textObject = new JSONObject();
            textObject.put("content", content);

            jsonObject.put("text", textObject);

        } else if (WX_CUSTOM_MSGTYPE_NEWS.equalsIgnoreCase(msgType)) {
            // 发送图文消息(news)
            JSONObject newsObj = new JSONObject();
            jsonObject.put("news", newsObj);


            JSONArray jsonArray = new JSONArray(articles);
            newsObj.put("articles", jsonArray);
        }

        String jsonBody = jsonObject.toJSONString();
        logger.debug(jsonBody);

        Token token = getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token.getAccess_token() ;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        String result = HttpUtil.postUrl(url, jsonBody);

        try {
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            logger.debug(result);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public List<WeixinCustomMenu> searchCustomMenus(final int enterpriseId) {
        return weixinDao.searchCustomMenus(enterpriseId);
    }

    @Override
    public int deleteCustomMenu(int enterpriseId, int id) {
        return weixinDao.deleteCustomMenu(enterpriseId, id);
    }

    @Override
    public int createCustomMenu(final int enterpriseId, String name, String type, String url, String key, int level, int parentId) {
        if (enterpriseId != this.weixinId) return -1;

        WeixinCustomMenu o = new WeixinCustomMenu();

        o.setEnterpriseId(enterpriseId);

        o.setName(name);
        o.setType(type);
        o.setUrl(url);
        o.setKey(key);
        o.setLevel(level);
        o.setParentId(parentId);

        int retCode = weixinDao.createCustomMenu(o);

        return retCode;
    }
}
