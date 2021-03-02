package cn.buk.api.wechat.dao;

import cn.buk.api.wechat.entity.*;
import cn.buk.common.sc.CommonSearchCriteria;

import java.util.Date;
import java.util.List;

/**
 * @author yfdai
 */
public interface WeixinDao {

    /**
     * 保存Token
     */
    int createWeixinOauthToken(WeixinOauthToken token);

    int createWeixinAccessTime(String weixinOpenId, int weixinId);

    /**
     * 获取当前有效的token
     */
    Token retrieveWeixinToken(int weixinId, int weixinType, int msgType);

    /**
     * 保存Token
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
     */
    WeixinEntConfig getWeixinEntConfig(int enterpriseId, int msgType);

    List<WeixinGroup> listWeixinGroup(int weixinId);

    WeixinGroup getWeixinGroup(int weixinId, int groupId);

    List<WeixinUser> listWeixinUser(int weixinId);

    int updateWeixinGroup(WeixinGroup weixinGroup);

    WeixinUser getWeixinUser(int weixinId, String weixinOpenId);

    WeixinUser getWeixinUser(int weixinId, int userId);

    int updateWeixinUser(WeixinUser user);

    int createWeixinGroup(WeixinGroup group);

    int saveSuiteTicket(int enterpriseId, String suiteId, String suiteTicket, Date timeStamp);

    WwProviderTicket getSuiteTicket(int enterpriseId, String suiteId);

    WwProviderToken retrieveWwProviderToken(int enterpriseId, String suiteId);

    int saveWwProviderToken(int enterpriseId, String suiteId, String suiteAccessToken, int expiresIn);

    int saveWwpAuthCorpInfo(WwProviderAuthCorpInfo corpInfo);

    WwProviderAuthCorpInfo getWwpAuthCorpInfo(int enterpriseId, String suiteId, String corpId);

    int updateWwpAuthCorpAccessToken(int enterpriseId, String authCorpId, String access_token, int expires_in);

    int cancelSuiteAuthInfo(int enterpriseId, String suiteId, String authCorpId);

    /**
     * 获取微信服务号的配置信息
     */
    WeixinServiceConfig getWeixinServiceConfig(int enterpriseId);
}
