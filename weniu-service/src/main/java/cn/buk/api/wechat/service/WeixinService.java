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

//    void testImgUrl();

    /**
     * 验证消息来源是否微信发送的
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机字符
     * @return
     */
    boolean verifyWeixinSource(String signature, String timestamp, String nonce);

    /**
     * 处理微信消息
     * @param request
     * @param response
     */
    void processWeixinMessage(HttpServletRequest request, HttpServletResponse response);

    void processWeixinEvent(HttpServletResponse response, WxData rq);

    String getAppid();

    JsSdkParam getJsSdkConfig(String jsapi_url);

    /**
     * 微信网页授权
     * @param weixinOauthCode 授权的code
     * @return
     */
    WeixinOauthToken getOauthToken(String weixinOauthCode);


    /**
     * 基础支持-获取access_token：从数据库中获取，必要时重新获取
     * @param enterpriseId
     * @return
     */
    Token searchAccessToken(int enterpriseId);


    //自定义菜单

    /**
     * 微信公众号自定义菜单查询接口
     */
    String getCustomMenu();

    /**
     * 微信公众号自定义菜单创建接口
     */
    String createCustomMenu();


    /**
     * 发送客服消息接口
     */
    String sendCustomMessage(final String touser, final String msgType, final String content, List<Object> articles);

    /**
     * 获取模板消息的模板列表，并保存到本地
     */
    List<WeixinTemplate> syncTemplates();


    /**
     * 发送模板消息
     */
    String sendTemplateMsg(WxTemplateSend wxTplRq);

    /**
     * 获取微信用户列表
     * @return
     */
    int syncUserList();


    /**
     * 根据openid获取用户基本信息
     * @param openid 微信用户的唯一标示
     * @return
     */
    WeixinUserInfo getUserInfo(String openid);


    /**
     * 获取素材总数
     * @return
     */
    WxMaterialSummary getMaterialSummary();

    /**
     * 获取素材列表
     * @return
     */
    WxMaterials getMaterials(final String mediaType, final int offset, final int count);

    /**
     * 新增永久素材(非图文素材）
     * @return
     */
    WxMediaResponse addMaterial(String filePath, String mediaType);

    /**
     * 新增永久图文素材
     * @return
     */
    String addMaterialNews(WxNewsRequest request);

    /**
     * 上传图文消息内的图片获取URL
     * @param filePath
     * @return
     */
    WxMediaResponse uploadNewsImage(String filePath);

    /**
     * 获取永久素材, 暂时只支持图文素材
     */
    String getMaterial(String mediaType, String mediaId);

    /**
     *删除永久素材
     */
    WxMediaResponse delMaterial(String mediaId);




    //database operation

    List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc);

    /**
     * 查找本地保存的永久素材列表
     * @param enterpriseId
     * @param sc
     * @return
     */
    List<WeixinMaterial> searchMaterials(int enterpriseId, CommonSearchCriteria sc);

    /**
     * 保存永久素材信息在本地
     * @param mediaType
     * @param mediaId
     * @param url
     * @return
     */
    int createWeixinMaterial(String mediaType, String mediaId, String url);

    WeixinMaterial searchWeixinMaterial(int id);


    WeixinTemplate searchWeixinTemplate(String id);

    List<WeixinTemplate> searchTemplates(int enterpriseId);

    List<WeixinCustomMenu> searchCustomMenus(int enterpriseId);

    int deleteCustomMenu(int enterpriseId, int id);

    int createCustomMenu(int enterpriseId, String name, String type, String url, String key, int level, int parentId);

}
