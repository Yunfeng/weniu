package cn.buk.api.wechat.dao;

import cn.buk.api.wechat.entity.*;
import cn.buk.common.sc.CommonSearchCriteria;

import java.util.List;

public interface WeixinDao {

    /**
     * 保存Token
     * @param token
     * @return
     */
    int createWeixinOauthToken(WeixinOauthToken token);

    int createWeixinAccessTime(String weixinOpenId, int weixinId);

    /**
     * 获取当前有效的token
     *
     * @return
     */
    Token retrieveWeixinToken(int weixinId, int weixinType, int msgType);

    /**
     * 保存Token
     *
     * @param token
     * @return
     */
    int createWeixinToken(Token token);

    int createWeixinUser(WeixinUser user);

    List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc);

    WeixinUser searchWeixinUser(int enterpriseId, String openId);

    int createWeixinTemplate(WeixinTemplate t1);

    WeixinTemplate searchWeixinTemplate(int ownerId, String id);
    List<WeixinTemplate> searchWeixinTemplates(int ownerId);

    List<WeixinCustomMenu> searchCustomMenus(int ownerId);

    int deleteCustomMenu(int ownerId, int id);

    int createCustomMenu(WeixinCustomMenu o);

    int createWeixinMaterial(WeixinMaterial wm);
    int updateWeixinMaterial(WeixinMaterial wm);

    List<WeixinMaterial> searchMaterials(int enterpriseId, CommonSearchCriteria sc);
    List<WeixinMaterial> searchMaterials(int enterpriseId, String mediaId);

    WeixinMaterial searchWeixinMaterial(int weixinId, int id);

    List<WeixinNews> searchWeixinNews(int weixinId);

    int createWxNews(WeixinNews o);

    int deleteWxNews(int enterpriseId, int id);

    /**
     * 获取企业微信的设置参数
     * @param enterpriseId 企业ID
     * @param msgType 消息类型
     * @return
     */
    WeixinEntConfig getWeixinEntConfig(int enterpriseId, int msgType);
}
