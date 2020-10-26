package cn.buk.api.wechat.service;


import cn.buk.api.wechat.dao.WeixinDao;
import cn.buk.api.wechat.dto.*;
import cn.buk.api.wechat.entity.*;
import cn.buk.api.wechat.util.EncoderHandler;
import cn.buk.api.wechat.util.FileUtil;
import cn.buk.api.wechat.util.HttpUtil;
import cn.buk.api.wechat.util.SignUtil;
import cn.buk.common.sc.CommonSearchCriteria;
import cn.buk.util.DateUtil;
import cn.buk.common.JsonResult;
import cn.buk.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.buk.api.wechat.util.HttpUtil.downloadFile;
import static cn.buk.api.wechat.util.HttpUtil.postUrl;
import static cn.buk.api.wechat.util.HttpUtil.sendResponse;

/**
 * Created by yfdai on 2017/2/6.
 */
public class WeixinServiceImpl implements WeixinService {

    private static Logger logger = Logger.getLogger(WeixinServiceImpl.class);

    /**
     * 文本的客服消息
     */
    private final static String WX_CUSTOM_MSGTYPE_TEXT = "text";
    /**
     * 图文(news)的客服消息 发送图文消息（点击跳转到外链）
     */
    private final static String WX_CUSTOM_MSGTYPE_NEWS = "news";

    /**
     * 图文(news)的客服消息 发送图文消息（点击跳转到图文消息页面）
     */
    private final static String WX_CUSTOM_MSGTYPE_MPNEWS = "mpnews";

    // 地址
    private static final String URL = "http://www.csdn.net";
    // 编码
    private static final String ECODING = "UTF-8";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";


    private static final String DOWNLOAD_DIR = "/temp";


    private static final String WX_API_OAUTH = "https://api.weixin.qq.com/sns/oauth2/access_token?";

    private static final String WX_API_MENU_INFO =  "https://api.weixin.qq.com/cgi-bin/menu/get?";

    /**
     * 获取永久素材接口
     */
    private static final String WX_API_MATERIAL_GET = "https://api.weixin.qq.com/cgi-bin/material/get_material?";
    /**
     * 删除永久素材接口
     */
    private static final String WX_API_MATERIAL_DEL = "https://api.weixin.qq.com/cgi-bin/material/del_material?";

    /**
     * 获取媒体文件（临时素材）接口
     */
    private static final String WX_API_MEDIA_GET = "https://api.weixin.qq.com/cgi-bin/media/get?";


    public static String postFile(String url, String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;

        String result = null;

        try {
            URL url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            String boundary = "-----------------------------" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = conn.getOutputStream();
            output.write(("--" + boundary + "\r\n").getBytes());
            output.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", file.getName()).getBytes("UTF-8"));
            output.write("Content-Type: image/jpeg \r\n\r\n".getBytes());
            byte[] data = new byte[1024];
            int len = 0;
            FileInputStream input = new FileInputStream(file);
            while ((len = input.read(data)) > -1) {
                output.write(data, 0, len);
            }
            output.write(("\r\n--" + boundary + "\r\n\r\n").getBytes());
            output.flush();
            output.close();
            input.close();
            InputStream resp = conn.getInputStream();
            StringBuffer sb = new StringBuffer();
            while ((len = resp.read(data)) > -1)
                sb.append(new String(data, 0, len, "utf-8"));
            resp.close();
            result = sb.toString();
            //System.out.println(result);
        } catch (ClientProtocolException e) {
            logger.error("postFile，不支持http协议", e);
        } catch (IOException e) {
            logger.error("postFile数据传输失败", e);
        }
        System.out.println(result);
        return result;
    }

    /***
     * 获取ImageUrl地址
     *
     * @param HTML
     * @return
     */
    private List<String> getImageUrl(String HTML) {
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
        List<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        return listImgUrl;
    }

    /***
     * 获取ImageSrc地址
     *
     * @param listImageUrl
     * @return
     */
    private List<String> getImageSrc(List<String> listImageUrl) {
        List<String> listImgSrc = new ArrayList<>();
        for (String image : listImageUrl) {
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()) {
                listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return listImgSrc;
    }

    /***
     * 下载图片
     *
     * @param listImgSrc
     */
    private void Download(List<String> listImgSrc) {
        try {
            for (String url : listImgSrc) {
                String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
                URL uri = new URL(url);
                InputStream in = uri.openStream();
                FileOutputStream fo = new FileOutputStream(new File(imageName));
                byte[] buf = new byte[1024];
                int length = 0;
                System.out.println("开始下载:" + url);
                while ((length = in.read(buf, 0, buf.length)) != -1) {
                    fo.write(buf, 0, length);
                }
                in.close();
                fo.close();
                System.out.println(imageName + "下载完成");
            }
        } catch (Exception e) {
            System.out.println("下载失败");
        }
    }

//    @Value("${Weixin_Id}")
//    private int weixinId;
//
//    /**
//     * 以下三个参数是微信接口需要用到的
//     */
//    @Value("${Weixin_AppId}")
//    private String appId;
//
//    @Value("${Weixin_AppSecret}")
//    private String appSecret;
//
//    @Value("${Weixin_Token}")
//    private String weixinToken;


    @Autowired
    private WeixinDao weixinDao;


    private WeixinServiceConfig getWeixinServiceConfig(int enterpriseId) {
        return weixinDao.getWeixinServiceConfig(enterpriseId);
    }

    private String getAppToken(int enterpriseId) {
        WeixinServiceConfig config = getWeixinServiceConfig(enterpriseId);
        return config == null ? null : config.getToken();
    }

    private String getAppId(int enterpriseId) {
        WeixinServiceConfig config = getWeixinServiceConfig(enterpriseId);
        return config == null ? null : config.getAppId();
    }

    public JsSdkParam getJsSdkConfig(int enterpriseId, String jsapi_url) {
        JsSdkParam jsapiParam = new JsSdkParam();
        jsapiParam.setAppId(getAppId(enterpriseId));

        Token ticket = getJsSdkTicket(enterpriseId);

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
    public WeixinOauthToken getOauthToken(final int enterpriseId, final String weixinOauthCode) {
        final String url = WX_API_OAUTH;

        WeixinServiceConfig config = getWeixinServiceConfig(enterpriseId);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", config.getAppId()));
        params.add(new BasicNameValuePair("secret", config.getAppSecret()));
        params.add(new BasicNameValuePair("code", weixinOauthCode));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        final String jsonStr = HttpUtil.getUrl(url, params);

        //判断返回结果
        JSONObject param = JSON.parseObject(jsonStr);

        if (param.get("errcode") == null) {
            WeixinOauthToken token = new WeixinOauthToken();
            token.setAccess_token((String) param.get("access_token"));
            token.setRefresh_token((String) param.get("refresh_token"));
            token.setOpenid((String) param.get("openid"));
            token.setScope((String) param.get("scope"));
            token.setExpires_in((Integer) param.get("expires_in"));

            token.setEnterpriseId(enterpriseId);
            weixinDao.createWeixinOauthToken(token);

            return token;
        } else {
            logger.info(jsonStr);
            return null;
        }
    }


    public boolean verifyWeixinSource(int enterpriseId, String signature, String timestamp, String nonce) {
        try {
            ArrayList<String> al = new ArrayList<>();
            al.add(getAppToken(enterpriseId));
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
    /**
     *
     * @param enterpriseId 需要查询的enterpriseId; 否则使用默认的
     * @return
     */
    public String getCustomMenu(final int enterpriseId) {
        final String url = WX_API_MENU_INFO;

        Token token = getToken(enterpriseId);

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
    public BaseResponse createCustomMenu(final int enterpriseId) {
        WeixinMenu wm = new WeixinMenu();

        List<WeixinCustomMenu> menus = weixinDao.searchCustomMenus(enterpriseId);

        String url = "";

        //一级菜单
        for(WeixinCustomMenu m1: menus) {
            if (m1.getLevel() != 1) continue;

            WeixinMenuItem dto = new WeixinMenuItem();
            wm.getButton().add(dto);
            dto.setName(m1.getName());

            if (m1.getType() != null && m1.getType().equalsIgnoreCase("view")) {
                dto.setType(m1.getType());

                url = m1.getUrl();
                if (m1.getBindUrl() == 1) {
                    url = buildUrlInWeixin(enterpriseId, m1.getUrl());
                }

                dto.setUrl(url);
                continue;
            } else if (m1.getType() != null && m1.getType().equalsIgnoreCase("click")) {
                dto.setType(m1.getType());
                dto.setKey(m1.getKey());

                continue;
            }

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

                    url = m2.getUrl();
                    if (m2.getBindUrl() == 1) {
                        url = buildUrlInWeixin(enterpriseId, m2.getUrl());
                    }

                    dto2.setUrl(url);
                } else if (m2.getType().equalsIgnoreCase("click")) {
                    dto2.setType(m2.getType());
                    dto2.setKey(m2.getKey());
                }

            }
        }

        String jsonBody = JSON.toJSON(wm).toString();
        logger.info(jsonBody);

        Token token = getToken(enterpriseId);
        url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token.getAccess_token() ;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        String jsonStr = HttpUtil.postUrl(url, jsonBody);

        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    public synchronized Token getToken(final int enterpriseId) {
        Token token = weixinDao.retrieveWeixinToken(enterpriseId, Token.WEIXIN_SERVICE_TOKEN, 0);

        if (token == null || DateUtil.getPastSeconds(token.getCreateTime()) >= token.getExpires_in()) {
            token = refreshWeixinToken(enterpriseId);
        }

        return token;

    }

    @Override
    public List<WeixinGroup> getGroupList(final int enterpriseId) {
        return weixinDao.listWeixinGroup(enterpriseId);
    }

    @Override
    public WeixinGroup getGroupInfo(final int enterpriseId, int groupId) {
        return weixinDao.getWeixinGroup(enterpriseId, groupId);
    }


    /**
     * @Override
     * @param enterpriseId
     * @param msg
     * @param weixinIds
     * @param deptIds
     * @param tagIds
     */
    public void sendTextMsg(int enterpriseId, String msg, String weixinIds, String deptIds, String tagIds) {
        if (weixinIds != null && weixinIds.equalsIgnoreCase("NONE")) return;

        WeixinEntConfig cfg = weixinDao.getWeixinEntConfig(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT);
        if (cfg == null) {
            logger.warn("No weixin config.");
            return;
        }

        logger.info(cfg.getEnterpriseId() + ", " + enterpriseId + ": " + cfg.getId() + ", " + cfg.getCorpId() + ", " + cfg.getAgentId() + ", " + cfg.getSecret());

        TextMessage txtMsg = new TextMessage();
        txtMsg.setAgentid(cfg.getAgentId());

        if (weixinIds != null && weixinIds.trim().length() > 0) {
            txtMsg.setTouser( weixinIds.replaceAll(";", "|"));
        }

        if (deptIds != null && deptIds.trim().length() > 0) {
            txtMsg.setToparty(deptIds.replaceAll(";", "|"));
        }

        if (tagIds != null && tagIds.trim().length() > 0) {
            txtMsg.setTotag(tagIds.replaceAll(";", "|"));
        }


        txtMsg.setContent(msg  + ". " + DateUtil.formatDate(DateUtil.getCurDateTime(), "MM-dd HH:mm:ss"));

        String jsonStr = com.alibaba.fastjson.JSON.toJSONString(txtMsg);


        Token token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT);
        logger.info("token: " + token.getId() + ", " + token.getEnterpriseId() + ", " + token.getMsgType() + ", " + token.getAccess_token());
        logger.info(jsonStr);

        JsonResult jsonResult = doSendTextMsg(jsonStr, token);

        if (jsonResult.getErrcode() == 0) {
            //发送成功
            //return jsonResult;
        } else if (jsonResult.getErrcode() == 40014) {
            //invalid access_token
            //try again
            System.out.println("try again ............................................................................");
            token = getToken(enterpriseId, WeixinEntConfig.WORK_WX_DEFAULT, true);
            doSendTextMsg(jsonStr, token);
        } else {
            logger.error(jsonResult.getErrcode() + " - " + jsonResult.getErrmsg());
        }
    }

    /**
     * 获取js-sdk ticket, 可刷新
     */
    private synchronized Token getJsSdkTicket(final int enterpriseId) {
        Token token = weixinDao.retrieveWeixinToken(enterpriseId, Token.WEIXIN_JS_SDK_TICKET, 0);

        if (token == null || DateUtil.getPastSeconds(token.getCreateTime()) >= token.getExpires_in()) {
            token = refreshWeixinJsSdkTicket(enterpriseId);
        }

        return token;
    }

    /**
     * 重新获取weixin的access token
     */
    private Token refreshWeixinToken(final int enterpriseId) {
        final String url = "https://api.weixin.qq.com/cgi-bin/token?";

        WeixinServiceConfig config = getWeixinServiceConfig(enterpriseId);
        String appId = config.getAppId();
        String appSecret = config.getAppSecret();

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
        token.setEnterpriseId(enterpriseId);

        weixinDao.createWeixinToken(token);

        return token;
    }

    /**
     * 获取js sdk需要的ticket
     */
    private Token refreshWeixinJsSdkTicket(final int enterpriseId) {
        Token accessToken = getToken(enterpriseId);

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
     *获取微信素材列表
     * @param mediaType 素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count 返回素材的数量，取值在1到20之间
     * @return
     */
    public WxMaterials getMaterials(final int enterpriseId, final String mediaType, final int offset, final int count) {
        WeixinMediasRequest request = new WeixinMediasRequest();
        request.setType(mediaType);
        request.setOffset(offset);
        request.setCount(count);

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        Token token = getToken(enterpriseId);
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + token.getAccess_token();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        String result = HttpUtil.postUrl(url, jsonBody);

//        try {
//            //result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        WxMaterials rs = JSON.parseObject(result, WxMaterials.class);

        return rs;
    }

    /**
     * 新增其他类型永久素材

     接口调用请求说明

     通过POST表单来调用接口，表单id为media，包含需要上传的素材内容，有filename、filelength、content-type等信息。请注意：图片素材将进入公众平台官网素材管理模块中的默认分组。
     *
     * @param filePath 文件路径
     * @param mediaType 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @return 返回原始结果
     */
    public WxMediaResponse addMaterial(final int enterpriseId, final String filePath, final String mediaType) {
        final String API_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?";

        Token token = getToken(enterpriseId);
        String url = API_URL + "access_token=" + token.getAccess_token() + "&type=" + mediaType;

        String result = postFile(url, filePath);

        WxMediaResponse rs = JSON.parseObject(result, WxMediaResponse.class);

        if (rs.getErrcode() <= 0) {
            //上传成功
            WeixinMaterial wm = new WeixinMaterial();
            wm.setEnterpriseId(enterpriseId);
            wm.setMaterialType(mediaType);
            wm.setMediaId(rs.getMedia_id());
            wm.setUrl(rs.getUrl());

            weixinDao.createWeixinMaterial(wm);
        } else {
            logger.error(result);
        }


        return rs;
    }

    /**
     * 新增永久图文素材
     * @return
     */
    public WxMediaResponse addMaterialNews(final int enterpriseId, WxNewsRequest request) {
        //检查内容中是否有图片链接，有的话把图片上传到微信，并替换链接
        replaceImageUrl(enterpriseId, request.getArticles());

//        http请求方式: POST，https协议
//        ?access_token=ACCESS_TOKEN
        final String API_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?";

        Token token = getToken(enterpriseId);
        String url = API_URL + "access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        String result = postUrl(url, jsonBody);

        logger.info(result);

        WxMediaResponse rs = JSON.parseObject(result, WxMediaResponse.class);
//        {
//            "media_id":MEDIA_ID
//        }
        return rs;
    }

    private void replaceImageUrl(final int enterpriseId, List<WxNews> articles) {
        for(WxNews article: articles) {
            List<String> images = getImageUrl(article.getContent());
            List<String> urls = getImageSrc(images);
            for(String url: urls) {
                //1.下载该图片
                logger.info(url);
                String filepath = getFilePath(url);
                String filename = HttpUtil.download(url, filepath);

                logger.info(filename);
                if (filename != null) {
                    //2.调用接口上传图片
                    WxMediaResponse rs = uploadNewsImage(enterpriseId, filename);
                    //3.用返回的新url替换
                    if (rs != null && rs.getUrl() != null) {
                        article.setContent(article.getContent().replace(url, rs.getUrl()));
                    }
                }
            }
        }
    }

    private String getFilePath(String url) {
        String[] vals = url.split("/");
        String val = vals[vals.length - 1];

        if (val.indexOf(".") > 0) {
            return DOWNLOAD_DIR + "/" + val;
        } else {
            return null;
        }
    }

    /**
     * 上传图文消息内的图片获取URL
     本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
     * @param filePath
     * @return
     */
    public WxMediaResponse uploadNewsImage(final int enterpriseId, String filePath) {
        final String API_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?";

        Token token = getToken(enterpriseId);
        String url = API_URL + "access_token=" + token.getAccess_token();

        String result = postFile(url, filePath);

//        {
//            "url":  "http://mmbiz.qpic.cn/mmbiz/gLO17UPS6FS2xsypf378iaNhWacZ1G1UplZYWEYfwvuU6Ont96b1roYs CNFwaRrSaKTPCUdBK9DgEHicsKwWCBRQ/0"
//        }
        logger.info(result);

        WxMediaResponse rs = JSON.parseObject(result, WxMediaResponse.class);

        return rs;
    }

    /**
     *http请求方式: POST,https协议
     * @param mediaId
     * @return
     */
    public String getMaterial(final int enterpriseId, final String mediaType, final String mediaId) {
        WxMediaRequest request = new WxMediaRequest();
        request.setMedia_id(mediaId);

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        Token token = getToken(enterpriseId);
        final String url = WX_API_MATERIAL_GET + "access_token=" + token.getAccess_token();


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        if (mediaType.equalsIgnoreCase(WeixinMaterial.MATERIAL_NEWS) || mediaType.equalsIgnoreCase(WeixinMaterial.MATERIAL_VIDEO)) {
            return HttpUtil.postUrl(url, jsonBody);
        } else {
            return downloadFile(url, jsonBody, null);
        }
    }

    /**
     * 删除永久素材
     */
    public WxMediaResponse delMaterial(final int enterpriseId, String mediaId) {
        WxMediaRequest request = new WxMediaRequest();
        request.setMedia_id(mediaId);

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        Token token = getToken(enterpriseId);
        final String url = WX_API_MATERIAL_DEL + "access_token=" + token.getAccess_token();


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        String result = HttpUtil.postUrl(url, jsonBody);

        WxMediaResponse rs = JSON.parseObject(result, WxMediaResponse.class);

        return rs;
    }

    /**
     * 获取临时素材
     * @param mediaType
     * @param mediaId
     * @return 语音和图片素材返回下载到本地后的地址；视频文件返回URL
     */
    public String getMedia(final int enterpriseId, String mediaType, String mediaId) {
        Token token = getToken(enterpriseId);
        final String url = WX_API_MEDIA_GET + "access_token=" + token.getAccess_token() + "&media_id=" + mediaId;



        if (mediaType.equalsIgnoreCase(WeixinMaterial.MATERIAL_VIDEO)) {
            return HttpUtil.getUrl(url, null);
        } else {
            return HttpUtil.download(url, null);
        }
    }

    /**
     * 根据openid获取用户信息
     */
    public WeixinUserInfo getUserInfo(final int enterpriseId, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", this.getToken(enterpriseId).getAccess_token()));
        params.add(new BasicNameValuePair("openid", openid));
        params.add(new BasicNameValuePair("lang", "zh_CN"));

        String jsonStr = HttpUtil.getUrl(url, params);

        logger.debug(url);
        logger.debug(jsonStr);

        return JSON.parseObject(jsonStr, WeixinUserInfo.class);
    }

    /**
     * 获取素材总数
     * 1.永久素材的总数，也会计算公众平台官网素材管理中的素材
     2.图片和图文消息素材（包括单图文和多图文）的总数上限为5000，其他素材的总数上限为1000
     3.调用该接口需https协议
     * @return
     */
    public WxMaterialSummary getMaterialSummary(final int enterpriseId) {
        final String url = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?";

        Token token = getToken(enterpriseId);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));

        String result = HttpUtil.getUrl(url, params);

        WxMaterialSummary summary =  JSON.parseObject(result, WxMaterialSummary.class);

        return summary;
    }

    /**
     * 处理微信公众号里面的事件, 以客服消息发送给用户
     */
    public void processWeixinEvent(final int enterpriseId, HttpServletResponse response, WxData rq) {
        if ("subscribe".equalsIgnoreCase(rq.getEvent())) {
            // TODO 通过特定场景二维码关注 eventKey
            List<Object> articles = new ArrayList<>();
            String mediaId = null;

            List<WeixinNews> newsList = weixinDao.searchWeixinNews(enterpriseId);
            for(WeixinNews o: newsList) {
                if (o.getMediaId() != null && o.getMediaId().trim().length() > 0) {
                    mediaId = o.getMediaId();
                    break;
                }

                WxArticle article = new WxArticle();
                articles.add(article);

                article.setTitle(o.getTitle());
                article.setDescription(o.getDescription());
                article.setPicurl(o.getPicurl());
                String url0 = o.getUrl();
                String url = buildUrlInWeixin(enterpriseId, url0);
                article.setUrl(url);
            }

            if (mediaId != null) {
                this.sendCustomMessage(enterpriseId, rq.getFromUserName(), WX_CUSTOM_MSGTYPE_MPNEWS, mediaId, null);
            } else {
                this.sendCustomMessage(enterpriseId, rq.getFromUserName(), WX_CUSTOM_MSGTYPE_NEWS, null, articles);
            }
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
    public int syncUserList(final int enterpriseId) {
        final String url = "https://api.weixin.qq.com/cgi-bin/user/get?";

        Token token = getToken(enterpriseId);

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
            user.setEnterpriseId(enterpriseId);


            WeixinUserInfo userDetail = getUserInfo(enterpriseId, openId);
            if (userDetail != null ) {
                user.setSubscribe(userDetail.getSubscribe());
                if (userDetail.getSubscribe() == 1) {
                    BeanUtils.copyProperties(userDetail, user);
                    user.setSubscribe_time(DateUtil.timestampToDate(userDetail.getSubscribe_time() * 1000));


                    String temp = user.getNickname();
                    temp = StringUtil.filterEmoji(temp, "");
                    user.setNickname(temp);
                }
            }

            // 将用户列表保存到本地
            WeixinUser user0 = weixinDao.searchWeixinUser(user.getEnterpriseId(), user.getWeixinOpenId());
            if (user0 == null) {
                int status = weixinDao.createWeixinUser(user);
            } else {
                user0.setNickname(user.getNickname());
                user0.setRemark(user.getRemark());

                weixinDao.updateWeixinUser(user0);
            }
        }

        //jsonStr = "total: " + total + ", count: " + count;

        return count;
    }

    /**
     * 同步消息模板到本地
     */
    public List<WeixinTemplate> syncTemplates(final int enterpriseId) {
        List<WeixinTemplate> results = new ArrayList<>();

        Token token = getToken(enterpriseId);
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
            t1.setEnterpriseId(enterpriseId);

            if (weixinDao.searchWeixinTemplate(enterpriseId, t1.getTemplate_id()) == null) {
                 weixinDao.createWeixinTemplate(t1);
            }
        }

        return results;
    }

    /**
     * 发送模板消息
     */
    public String sendTemplateMsg(final int enterpriseId, WxTemplateSend wxTplRq) {
        String jsonBody = JSON.toJSONString(wxTplRq);
        logger.debug(jsonBody);

        Token token = getToken(enterpriseId);
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
    public void processWeixinMessage(final int enterpriseId, HttpServletRequest request, HttpServletResponse response) {

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
            this.processWeixinEvent(enterpriseId, response, rq);

            return;
        }
    }

    @Override
    public List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc) {
        return weixinDao.searchSubscribers(enterpriseId, sc);
    }

    @Override
    public List<WeixinMaterial> searchMaterials(int enterpriseId, CommonSearchCriteria sc) {
        return weixinDao.searchMaterials(enterpriseId, sc);
    }

    @Override
    public List<WeixinMaterial> searchMaterials(int enterpriseId, String mediaId) {
        return weixinDao.searchMaterials(enterpriseId, mediaId);
    }

    @Override
    public int createWeixinMaterial(int enterpriseId, String mediaType, String mediaId, String url, String name) {
        List<WeixinMaterial> list = searchMaterials(enterpriseId, mediaId);

        if (list == null || list.size() == 0) {
            WeixinMaterial wm = new WeixinMaterial();
            wm.setEnterpriseId(enterpriseId);
            wm.setMaterialType(mediaType);
            wm.setMediaId(mediaId);
            wm.setUrl(url);
            wm.setName(name);

            return weixinDao.createWeixinMaterial(wm);
        } else {
            WeixinMaterial wm = list.get(0);
            wm.setMaterialType(mediaType);
            wm.setUrl(url);
            wm.setName(name);

            return weixinDao.updateWeixinMaterial(wm);
        }
    }

    @Override
    public WeixinMaterial searchWeixinMaterial(final int enterpriseId, int id) {
        return weixinDao.searchWeixinMaterial(enterpriseId, id);
    }

    @Override
    public Token searchAccessToken(int enterpriseId) {
        return this.getToken(enterpriseId);
    }

    @Override
    public WeixinTemplate searchWeixinTemplate(final int enterpriseId, String id) {
        return weixinDao.searchWeixinTemplate(enterpriseId, id);
    }

    @Override
    public List<WeixinTemplate> searchTemplates(int enterpriseId) {
            return weixinDao.searchWeixinTemplates(enterpriseId);
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
     * state: wxs_5
     * wxs 标识weixin_service_no，微信服务号，后面的数字5标识该企业的enterpriseId
     */
    public String buildUrlInWeixin(final int enterpriseId, String url0) {
        String appId = getAppId(enterpriseId);
        try {
            return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + appId
                    + "&redirect_uri=" + URLEncoder.encode(url0, "UTF-8")
                    + "&response_type=code"
                    + "&scope=snsapi_userinfo" +
                    "&state=" + "wxs_" + enterpriseId +
                    "#wechat_redirect";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 发送客户消息给用户
     */
    public String sendCustomMessage(final int enterpriseId, final String touser, final String msgType, final String content, List<Object> articles) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", touser);
        jsonObject.put("msgtype", msgType);

        if (WX_CUSTOM_MSGTYPE_TEXT.equalsIgnoreCase(msgType)) {
            // 发送文本消息
            JSONObject textObject = new JSONObject();
            textObject.put("content", content);

            jsonObject.put("text", textObject);
        } else if (WX_CUSTOM_MSGTYPE_MPNEWS.equalsIgnoreCase(msgType)) {
            JSONObject textObject = new JSONObject();
            textObject.put("media_id", content);

            jsonObject.put("mpnews", textObject);
        } else if (WX_CUSTOM_MSGTYPE_NEWS.equalsIgnoreCase(msgType)) {
            // 发送图文消息(news)
            JSONObject newsObj = new JSONObject();
            jsonObject.put("news", newsObj);


            JSONArray jsonArray = new JSONArray(articles);
            newsObj.put("articles", jsonArray);
        }

        String jsonBody = jsonObject.toJSONString();
        logger.debug(jsonBody);

        Token token = getToken(enterpriseId);
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

    public int createCustomMenu(final int enterpriseId, String name, String type, String url, String key, int level, int parentId) {
        return this.createCustomMenu(enterpriseId, name, type, url, key, level, parentId, 1);
    }

    @Override
    public int createCustomMenu(int enterpriseId, String name, String type, String url, String key, int level, int parentId, int bindUrl) {
        WeixinCustomMenu o = new WeixinCustomMenu();
        o.setEnterpriseId(enterpriseId);
        o.setName(name);
        o.setType(type);
        o.setUrl(url);
        o.setKey(key);
        o.setLevel(level);
        o.setParentId(parentId);
        o.setBindUrl(bindUrl);

        return weixinDao.createCustomMenu(o);
    }

    public List<WeixinUser> getUserList(final int enterpriseId) {
        return weixinDao.listWeixinUser(enterpriseId);
    }

    public JsonResult apiUpdateGroup(final int enterpriseId, int groupId, String groupName) {
        JsonResult resultStatus = new JsonResult();
        //TODO
        String url = null; //PropertiesUtil.getProperty(AppConfig.API_WEIXIN_UPDATE_GROUP);

        Token token = getToken(enterpriseId);
        url += "access_token=" + token.getAccess_token();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", groupName);
        jsonObject.put("id", groupId);

        JSONObject jsonRoot = new JSONObject();
        jsonRoot.put("group", jsonObject);

        String jsonBody = jsonRoot.toString();

        String jsonStr = HttpUtil.postUrl(url, jsonBody);


        //判断返回结果
        JSONObject jsonResult = JSONObject.parseObject(jsonStr);
        int errcode = (Integer)jsonResult.get("errcode");
        if (errcode != 0) {
            resultStatus.setErrcode(errcode);
            resultStatus.setErrmsg((String)jsonResult.get("errmsg"));
            return resultStatus;
        }

        WeixinGroup weixinGroup = weixinDao.getWeixinGroup(enterpriseId, groupId);
        if (weixinGroup == null) {
            resultStatus.setErrcode(-1);
            resultStatus.setErrmsg("远端修改成功，本地未找到对应的数据");
            return resultStatus;
        }

        weixinGroup.setGroupName(groupName);
        int status = weixinDao.updateWeixinGroup(weixinGroup);
        if ( status == 1) {
            resultStatus.setErrcode(0);
            resultStatus.setErrmsg("修改成功");
        } else {
            resultStatus.setErrcode(10000);
            resultStatus.setErrmsg("保存时失败");
        }

        return resultStatus;
    }

    public String apiGetUserList(final int enterpriseId) {
        //TODO
        String url = null;//PropertiesUtil.getProperty(AppConfig.API_WEIXIN_LIST_USER);
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        Token token = getToken(enterpriseId);

        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));
        params.add(new BasicNameValuePair("next_openid", ""));


        String jsonStr = HttpUtil.getUrl(url, params);


        //判断返回结果
        JSONObject jsonResult = JSONObject.parseObject(jsonStr);
        int total = (Integer) jsonResult.get("total");
        int count = (Integer) jsonResult.get("count");
        JSONObject dataObject = (JSONObject)jsonResult.get("data");
        JSONArray array = dataObject.getJSONArray("openid");
        int saveCount=0;
        for(int i = 0; i < array.size(); i++) {
            String openId = (String)array.get(i);
            WeixinUser user = new WeixinUser();
            user.setWeixinOpenId(openId);
            user.setEnterpriseId(enterpriseId);
            int status = weixinDao.createWeixinUser(user);
            if(status==1) saveCount++;
        }

        jsonStr = "total: " + total + ", count: " + count + ", saveCount: " + saveCount;


        return jsonStr;
    }

    public String apiGetGroupId(final int enterpriseId, String weixinOpenId) {
        //TODO
        String url = null;//PropertiesUtil.getProperty(AppConfig.API_WEIXIN_GET_GROUP_ID);

        Token token = getToken(enterpriseId);
        //https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN
        url += "access_token=" + token.getAccess_token();


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openid", weixinOpenId);
        String jsonBody = jsonObject.toString();

        String jsonStr = HttpUtil.postUrl(url, jsonBody);


        //判断返回结果
        JSONObject jsonResult = JSONObject.parseObject(jsonStr);
        if (jsonResult.get("errcode") != null) {
            jsonStr = "error: " + jsonResult.get("errmsg");
            return jsonStr;
        }

        int groupId = (Integer)jsonResult.get("groupid");
        WeixinUser user = weixinDao.getWeixinUser(enterpriseId, weixinOpenId);
        if (user != null) {
            user.setGroupId(groupId);
            int status = weixinDao.updateWeixinUser(user);
            if (status == 1) jsonStr = "OK: 获取成功";
            else jsonStr = "error: 保存失败";
        } else {
            jsonStr = "error: 未找到对应用户";
        }

        return jsonStr;
    }

    public String apiGetGroupList(final int enterpriseId) {
        //TODO
        String url = null; //PropertiesUtil.getProperty(AppConfig.API_WEIXIN_LIST_GROUP);
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        Token token = getToken(enterpriseId);

        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));


        String jsonStr = HttpUtil.getUrl(url, params);


        //判断返回结果
        JSONObject jsonResult = JSONObject.parseObject(jsonStr);
        JSONArray array = jsonResult.getJSONArray("groups");
        int saveCount=0;

        for(int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = (JSONObject)array.get(i);

            int groupId = (Integer)jsonObject.get("id");
            String groupName = (String)jsonObject.get("name");
            int count = (Integer)jsonObject.get("count");

            WeixinGroup group = new WeixinGroup();
            group.setEnterpriseId(enterpriseId);
            group.setGroupId(groupId);
            group.setGroupName(groupName);
            group.setCount(count);

            int status = weixinDao.createWeixinGroup(group);
            if(status==1) saveCount++;
        }

        jsonStr = "saveCount: " + saveCount;


        return jsonStr;
    }

    public String apiGetUserInfo(final int enterpriseId, int userId) {
        WeixinUser user = weixinDao.getWeixinUser(enterpriseId, userId);

        int status = executeApiGetUserInfo(enterpriseId, user);
        String jsonStr = "";
        if (status == 1)
            jsonStr = "OK";
        else
            jsonStr = "ER";

        return jsonStr;
    }

    public String apiGetUserInfo(final int enterpriseId) {
        List<WeixinUser> users = weixinDao.listWeixinUser(enterpriseId);

        String jsonStr="";
        int saveCount=0;

        for(WeixinUser user: users) {
            int status =executeApiGetUserInfo(enterpriseId, user);
            if(status==1) saveCount++;
        }

        jsonStr = "saveCount: " + saveCount;

        return jsonStr;
    }

    public String sendWeixinCustomMessage(final int enterpriseId, String toUser, String content) {
//        http请求方式: POST
//        https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
//        各消息类型所需的JSON数据包如下。
//                发送文本消息
//        {
//            "touser":"OPENID",
//                "msgtype":"text",
//                "text":
//            {
//                "content":"Hello World"
//            }
//        }
        Token token = getToken(enterpriseId);
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token.getAccess_token() ;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", toUser);
        jsonObject.put("msgtype", "text");


        JSONObject contentObject = new JSONObject();
        contentObject.put("content", content);

        jsonObject.put("text", contentObject);

        String jsonBody = jsonObject.toString();

        System.out.println(jsonBody);
        System.out.println(token.getAccess_token());

        String jsonStr = HttpUtil.postUrl(url, jsonBody);
        return jsonStr;
    }

    public JsonResult apiCreateGroup(final int enterpriseId, String groupName) {
        JsonResult resultStatus = new JsonResult();
        String url = null; //TODO PropertiesUtil.getProperty(AppConfig.API_WEIXIN_CREATE_GROUP);

        Token token = getToken(enterpriseId);
        url += "access_token=" + token.getAccess_token();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", groupName);

        JSONObject jsonRoot = new JSONObject();
        jsonRoot.put("group", jsonObject);

        String jsonBody = jsonRoot.toString();

        String jsonStr = HttpUtil.postUrl(url, jsonBody);


        //判断返回结果
        JSONObject jsonResult = JSONObject.parseObject(jsonStr);
        if (jsonResult.get("errcode") != null) {
            resultStatus.setErrcode((Integer)jsonResult.get("errcode"));
            resultStatus.setErrmsg((String)jsonResult.get("errmsg"));
            return resultStatus;
        }

        jsonObject = (JSONObject)jsonResult.get("group");
        int groupId = (Integer)jsonObject.get("id");

        WeixinGroup weixinGroup = new WeixinGroup();
        weixinGroup.setEnterpriseId(enterpriseId);
        weixinGroup.setGroupId(groupId);
        weixinGroup.setGroupName(groupName);

        int status = weixinDao.createWeixinGroup(weixinGroup);
        if ( status == 1) {
            resultStatus.setErrcode(0);
            resultStatus.setErrmsg("创建成功");
        } else {
            resultStatus.setErrcode(10000);
            resultStatus.setErrmsg("保存时失败");
        }

        return resultStatus;
    }

    public int createWeixinAccessTime(final int enterpriseId, String weixinOpenId) {
        return weixinDao.createWeixinAccessTime(weixinOpenId, enterpriseId);
    }

    public String createWeixinTemporaryQr(final int enterpriseId) {
//        临时二维码请求说明
//
//        http请求方式: POST
//        URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
//        POST数据格式：json
//        POST数据例子：{"expire_seconds": 1800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
//        参数说明
//        参数 	说明
//        expire_seconds 	该二维码有效时间，以秒为单位。 最大不超过1800。
//        action_name 	二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
//        action_info 	二维码详细信息
//        scene_id 	场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
//
//        返回说明
//
//        正确的Json返回结果:
//
//        {"ticket":"gQG28DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0FuWC1DNmZuVEhvMVp4NDNMRnNRAAIEesLvUQMECAcAAA==","expire_seconds":1800}

        Token token = getToken(enterpriseId);
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + token.getAccess_token() ;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expire_seconds", 1800);
        jsonObject.put("action_name", "QR_SCENE");


        JSONObject sceneObject = new JSONObject();
        sceneObject.put("scene_id", 10000000 + 898);

        JSONObject actionInfoObject = new JSONObject();
        actionInfoObject.put("scene", sceneObject);

        jsonObject.put("action_info", actionInfoObject);

        String jsonBody = jsonObject.toString();

        System.out.println(jsonBody);
        System.out.println(token.getAccess_token());

        String jsonStr = HttpUtil.postUrl(url, jsonBody);
        return jsonStr;
        //return jsonBody;
    }

    public String deleteCustomMenu(final int enterpriseId) {
        Token token = getToken(enterpriseId);
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + token.getAccess_token();

        String jsonStr = HttpUtil.getUrl(url, null);
        return jsonStr;
    }

    /**
     *
     * @param enterpriseId 企业id
     * @param jsonFilename 保存自定义菜单信息的文件名
     * @return
     */
    public BaseResponse createCustomMenu(final int enterpriseId, String jsonFilename) {
        Token token = getToken(enterpriseId);
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token.getAccess_token() ;

        String jsonStr = FileUtil.file2String(jsonFilename, "utf-8");
        JSONObject jsonObject= JSONObject.parseObject(jsonStr);

        String jsonBody = jsonObject.toString();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("body", jsonBody));

        jsonStr = HttpUtil.postUrl(url, jsonBody);
        return JSON.parseObject(jsonStr, BaseResponse.class);
    }

    private JsonResult doSendTextMsg(final String jsonStr, final Token token) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token.getAccess_token() ;

        String returnStr = HttpUtil.postUrl(url, jsonStr);

        System.out.println("doSendTextMsg: " + returnStr);

        return JSON.parseObject(returnStr, JsonResult.class);
    }

    public Token getToken(int enterpriseId, int msgType) {
        return getToken(enterpriseId, msgType,false);
    }

    private Token getToken(final int enterpriseId, final int msgType, boolean forced) {
        WeixinEntConfig entConfig = weixinDao.getWeixinEntConfig(enterpriseId, msgType);

        Token token = weixinDao.retrieveWeixinToken(enterpriseId, Token.WORK_WEIXIN_TOKEN, msgType);
        long pastSeconds = 0;
        if (token != null) {
            pastSeconds = DateUtil.getPastSeconds(token.getCreateTime());
        }

        if (forced || token == null || pastSeconds >= token.getExpires_in()) {
            //去获取新token
            //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
            String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?";

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("corpid", entConfig.getCorpId()));
            params.add(new BasicNameValuePair("corpsecret", entConfig.getSecret()));

            String jsonStr = HttpUtil.getUrl(url, params);

            //判断返回结果
            JSONObject param = JSONObject.parseObject(jsonStr);

            token = new Token();
            token.setAccess_token((String) param.get("access_token"));
            token.setExpires_in((Integer) param.get("expires_in"));
            token.setEnterpriseId(enterpriseId);
            token.setWeixinType(Token.WORK_WEIXIN_TOKEN);
            token.setMsgType(msgType);

            weixinDao.createWeixinToken(token);
        }

        return token;
    }

    private int executeApiGetUserInfo(final int enterpriseId, WeixinUser user) {
        String url = null; //TODO PropertiesUtil.getProperty(AppConfig.API_WEIXIN_GET_USER);
        Token token = getToken(enterpriseId);

        String jsonStr="";


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));
        params.add(new BasicNameValuePair("openid", user.getWeixinOpenId()));

        jsonStr = HttpUtil.getUrl(url, params);
        //判断返回结果
        JSONObject jsonResult = JSONObject.parseObject(jsonStr);

        if (jsonResult.get("openid") != null) return 0;

        int subscribe = (Integer) jsonResult.get("subscribe");
        if (subscribe == 0) {
            user.setSubscribe(0);
            weixinDao.updateWeixinUser(user);
            return 0;
        }
        user.setSubscribe(subscribe);

        String nickname = (String) jsonResult.get("nickname");
        user.setNickname(nickname);
        String language = (String) jsonResult.get("language");
        user.setLanguage(language);
        String city = (String) jsonResult.get("city");
        user.setCity(city);
        String province = (String) jsonResult.get("province");
        user.setProvince(province);
        String country = (String) jsonResult.get("country");
        user.setCountry(country);
        String headimgurl = (String) jsonResult.get("headimgurl");
        user.setHeadimgurl(headimgurl);
        String unionid = (String) jsonResult.get("unionid");
        user.setUnionid(unionid);

        int sex = (Integer) jsonResult.get("sex");
        user.setSex(sex);
        long time = (Integer) jsonResult.get("subscribe_time");
        Date subscribeTime = createDate(time*1000);
        user.setSubscribe_time(subscribeTime);

        int status = weixinDao.updateWeixinUser(user);

        return status;
    }

    private static Date createDate(long ms) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(ms);
        return c.getTime();
    }


}
