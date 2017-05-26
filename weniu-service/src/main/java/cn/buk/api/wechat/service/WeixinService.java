package cn.buk.api.wechat.service;


import cn.buk.api.wechat.dto.*;
import cn.buk.api.wechat.entity.*;

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

    int syncUserList();

    List<WeixinTemplate> syncTemplates();

    String sendTemplateMsg(WxTemplateSend wxTplRq);


    void processWeixinMessage(HttpServletRequest request, HttpServletResponse response);

    List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc);

    Token searchAccessToken(int enterpriseId);


    //database operation

    WeixinTemplate searchWeixinTemplate(String id);

    List<WeixinTemplate> searchTemplates(int enterpriseId);

    List<WeixinCustomMenu> searchCustomMenus(int enterpriseId);

    int deleteCustomMenu(int enterpriseId, int id);

    int createCustomMenu(int enterpriseId, String name, String type, String url, String key, int level, int parentId);

}
