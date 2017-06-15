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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.buk.api.wechat.util.HttpUtil.downloadFile;
import static cn.buk.api.wechat.util.HttpUtil.postUrl;
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

    // 地址
    private static final String URL = "http://www.csdn.net";
    // 编码
    private static final String ECODING = "UTF-8";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";



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
     * 获取HTML内容
     *
     * @param url
     * @return
     * @throws Exception
     */
    private String getHTML(String url) throws Exception {
        URL uri = new URL(url);
        URLConnection connection = uri.openConnection();
        InputStream in = connection.getInputStream();
        byte[] buf = new byte[1024];
        int length = 0;
        StringBuffer sb = new StringBuffer();
        while ((length = in.read(buf, 0, buf.length)) > 0) {
            sb.append(new String(buf, ECODING));
        }
        in.close();
        return sb.toString();
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


    public void testImgUrl() {
        //获得html文本内容
        String HTML = null;
        try {
            HTML = this.getHTML(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取图片标签
        List<String> imgUrl = this.getImageUrl(HTML);
        //获取图片src地址
        List<String> imgSrc = this.getImageSrc(imgUrl);
        //下载图片
        //this.Download(imgSrc);
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
        final String url = "https://api.weixin.qq.com/cgi-bin/menu/get?";

        Token token = getToken();

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
     *获取微信素材列表
     * @param mediaType 素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count 返回素材的数量，取值在1到20之间
     * @return
     */
    public WxMaterials getMaterials(final String mediaType, final int offset, final int count) {
        WeixinMediasRequest request = new WeixinMediasRequest();
        request.setType(mediaType);
        request.setOffset(offset);
        request.setCount(count);

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        Token token = getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + token.getAccess_token();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        String result = HttpUtil.postUrl(url, jsonBody);

        try {
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
    public WxMediaResponse addMaterial(final String filePath, final String mediaType) {
        final String API_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?";

        Token token = getToken();
        String url = API_URL + "access_token=" + token.getAccess_token() + "&type=" + mediaType;

        String result = postFile(url, filePath);

//        {
// "media_id":"D66eMY48x0SJaUhRU4Ggil4iBLv367MC1aHjvKLCvj4",
// "url":"http:\/\/mmbiz.qpic.cn\/mmbiz_jpg\/5JPNn1NvhAo6NicG9Ne8oPVNNThVSQyMEhwcxpia1d8iclaibPLrZibNFvku3fpLiaG11RnIEILUJMm6KFRT5J8VUibicw\/0?wx_fmt=jpeg"}

        WxMediaResponse rs = JSON.parseObject(result, WxMediaResponse.class);

        if (rs.getErrcode() <= 0) {
            //上传成功
            WeixinMaterial wm = new WeixinMaterial();
            wm.setOwnerId(this.weixinId);
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
    public String addMaterialNews(WxNewsRequest request) {
        //检查内容中是否有图片链接，有的话把图片上传到微信，并替换链接
        replaceImageUrl(request.getArticles());

//        http请求方式: POST，https协议
//        ?access_token=ACCESS_TOKEN
        final String API_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?";

        Token token = getToken();
        String url = API_URL + "access_token=" + token.getAccess_token();

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        String result = postUrl(url, jsonBody);

        logger.info(result);
//        {
//            "media_id":MEDIA_ID
//        }
        return result;
    }

    private void replaceImageUrl(List<WxNews> articles) {
        for(WxNews article: articles) {
            List<String> images = getImageUrl(article.getContent());
            List<String> urls = getImageSrc(images);
            for(String url: urls) {
                //1.下载该图片
                logger.info(url);
                String filename = HttpUtil.download(url, null);

                logger.info(filename);
                if (filename != null) {
                    //2.调用接口上传图片
                    WxMediaResponse rs = uploadNewsImage(filename);
                    //3.用返回的新url替换
                    if (rs != null && rs.getUrl() != null) {
                        article.setContent(article.getContent().replace(url, rs.getUrl()));
                    }
                }
            }
        }
    }

    /**
     * 上传图文消息内的图片获取URL
     本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
     * @param filePath
     * @return
     */
    public WxMediaResponse uploadNewsImage(String filePath) {
        final String API_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?";

        Token token = getToken();
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
    public String getMaterial(final String mediaType, final String mediaId) {
        WxMediaRequest request = new WxMediaRequest();
        request.setMedia_id(mediaId);

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        Token token = getToken();
        final String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=" + token.getAccess_token();


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
    public WxMediaResponse delMaterial(String mediaId) {
        WxMediaRequest request = new WxMediaRequest();
        request.setMedia_id(mediaId);

        String jsonBody = JSON.toJSON(request).toString();
        logger.debug(jsonBody);

        Token token = getToken();
        final String url = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=" + token.getAccess_token();


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("body", jsonBody));

        String result = HttpUtil.postUrl(url, jsonBody);

//        try {
//            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
//
            WxMediaResponse rs = JSON.parseObject(result, WxMediaResponse.class);
//            if (rs != null) {
//                logger.info(rs.getTotal_count());
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        return rs;
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
     * 获取素材总数
     * 1.永久素材的总数，也会计算公众平台官网素材管理中的素材
     2.图片和图文消息素材（包括单图文和多图文）的总数上限为5000，其他素材的总数上限为1000
     3.调用该接口需https协议
     * @return
     */
    public WxMaterialSummary getMaterialSummary() {
        final String url = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?";

        Token token = getToken();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));

        String result = HttpUtil.getUrl(url, params);

        WxMaterialSummary summary =  JSON.parseObject(result, WxMaterialSummary.class);

        return summary;
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
        final String url = "https://api.weixin.qq.com/cgi-bin/user/get?";

        Token token = getToken();

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

        //jsonStr = "total: " + total + ", count: " + count;

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
        return weixinDao.searchSubscribers(enterpriseId, sc);
    }

    @Override
    public List<WeixinMaterial> searchMaterials(int enterpriseId, CommonSearchCriteria sc) {
        return weixinDao.searchMaterials(enterpriseId, sc);
    }

    @Override
    public int createWeixinMaterial(String mediaType, String mediaId, String url) {
        WeixinMaterial wm = new WeixinMaterial();
        wm.setOwnerId(this.weixinId);
        wm.setMaterialType(mediaType);
        wm.setMediaId(mediaId);
        wm.setUrl(url);

        return weixinDao.createWeixinMaterial(wm);
    }

    @Override
    public WeixinMaterial searchWeixinMaterial(int id) {
        return weixinDao.searchWeixinMaterial(this.weixinId, id);
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
    public String sendCustomMessage(final String touser, final String msgType, final String content, List<Object> articles) {
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
