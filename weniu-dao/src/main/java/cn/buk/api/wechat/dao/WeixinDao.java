package cn.buk.api.wechat.dao;

import cn.buk.api.wechat.dto.CommonSearchCriteria;
import cn.buk.api.wechat.entity.*;

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
    Token retrieveWeixinToken(int weixinId, int weixinType);

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
}
