package cn.buk.api.wechat.service;


import cn.buk.api.wechat.dto.*;
import cn.buk.api.wechat.entity.Token;
import cn.buk.api.wechat.entity.WeixinOauthToken;
import cn.buk.api.wechat.entity.WeixinTemplate;
import cn.buk.api.wechat.entity.WeixinUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by yfdai on 2017/2/6.
 */
public interface WeixinService {


    String getAppid();

    JsSdkParam getJsSdkConfig(String jsapi_url);

    WeixinOauthToken getOauthToken(String weixinOauthCode);

    boolean verifyWeixinSource(String signature, String timestamp, String nonce);

    /**
     * 获取微信服务号自定义菜单
     */
    String getCustomMenu();

    /**
     * 创建微信服务号自定义菜单
     */
    String createCustomMenu();

    String getMaterials();

    WeixinUserInfo getUserInfo(String openid);

    void processWeixinEvent(HttpServletResponse response, WxData rq);

    JsonResult syncUserList();

    List<WeixinTemplate> syncTemplates();

    String sendTemplateMsg(WxTemplateSend wxTplRq);


    void processWeixinMessage(HttpServletRequest request, HttpServletResponse response);

    List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc);

    Token searchAccessToken(int enterpriseId);
}
