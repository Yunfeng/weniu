package cn.buk.api.wechat.dao;

import cn.buk.api.wechat.entity.*;
import cn.buk.common.sc.CommonSearchCriteria;
import cn.buk.common.sc.Page;
import cn.buk.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Transactional
public class WeixinDaoImpl extends AbstractDao implements WeixinDao {

  @Override
  public int createWeixinOauthToken(WeixinOauthToken token) {
    int retCode = persist(token);
    return retCode == 1 ? token.getId() : retCode;
  }

  @Override
  public int createWeixinAccessTime(String weixinOpenId, int weixinId) {
    int retCode = 0;

    List<WeixinAccessTime> accessTimes = em.createQuery("select o from WeixinAccessTime o " +
                    " where o.weixinOpenId = :weixinOpenId and o.weixinId = :weixinId ", WeixinAccessTime.class)
            .setParameter("weixinOpenId", weixinOpenId)
            .setParameter("weixinId", weixinId)
            .getResultList();

    WeixinAccessTime accessTime;
    if (accessTimes.size() > 0) {
      accessTime = accessTimes.get(0);
      accessTime.setAccessTime(DateUtil.getCurDateTime());

      em.merge(accessTime);
    } else {
      accessTime = new WeixinAccessTime();
      accessTime.setWeixinOpenId(weixinOpenId);
      accessTime.setAccessTime(DateUtil.getCurDateTime());
      accessTime.setWeixinId(weixinId);

      em.persist(accessTime);
    }

    retCode = accessTime.getId();


    return retCode;
  }

  @Override
  public Token retrieveWeixinToken(final int weixinId, final int weixinType, final int msgType) {
    List<Token> tokens = em.createQuery("select o from Token o " +
                    "where o.enterpriseId = :weixinId and o.weixinType = :weixinType and o.msgType = :msgType " +
                    "order by o.id desc", Token.class)
            .setParameter("weixinId", weixinId)
            .setParameter("weixinType", weixinType)
            .setParameter("msgType", msgType)
            .setMaxResults(1)
            .getResultList();
    if (tokens == null || tokens.isEmpty()) {
      return null;
    } else {
      return tokens.get(0);
    }
  }

  @Override
  public int createWeixinToken(Token token) {
    int retCode = persist(token);
    return retCode == 1 ? token.getId() : retCode;
  }

  @Override
  public int createWeixinUser(WeixinUser user) {
    int retCode = persist(user);
    return retCode == 1 ? user.getId() : retCode;
  }

  @Override
  public List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc) {
    List<WeixinUser> users = null;
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<WeixinUser> cq = cb.createQuery(WeixinUser.class);
    Root<WeixinUser> root = cq.from(WeixinUser.class);
    root.alias("o");


    List<Predicate> predicates = new ArrayList<>();
    Predicate where = cb.conjunction();
    predicates.add(where);

    predicates.add(cb.equal(root.get(WeixinUser_.enterpriseId), enterpriseId));

    if (!StringUtils.isBlank(sc.getIdno())) {
      predicates.add(cb.equal(root.get(WeixinUser_.weixinOpenId), sc.getIdno()));
    }


    //计算根据条件查询得出的数据总数
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<WeixinUser> countRoot = countQuery.from(WeixinUser.class);
    countRoot.alias("o");

    countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[predicates.size()]));
    Long count = em.createQuery(countQuery).getSingleResult();

    int maxResults = count.intValue();
    if (maxResults > 0) {

      cq.where(predicates.toArray(new Predicate[predicates.size()]));

      List<javax.persistence.criteria.Order> orderByes = new ArrayList<>();
      if (sc.getOrderBy() == 1) {
        orderByes.add(cb.desc(root.get("subscribe_time")));
      } else {
        orderByes.add(cb.asc(root.get("subscribe_time")));
      }

      cq.orderBy(orderByes);

      Page page = sc.getPage();
      page.setRowCount(maxResults);
      int i = (page.getPageNo() - 1) * page.getPageSize();

      if (i < 0) {
        i = 0;
      }

      try {
        users = em.createQuery(cq)
                .setFirstResult(i)
                .setMaxResults(page.getPageSize()).getResultList();

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    if (users == null) {
      users = new ArrayList<>();
    }

    return users;
  }

  @Override
  public WeixinUser searchWeixinUser(int enterpriseId, String openId) {
    List<WeixinUser> users = em.createQuery("select o from WeixinUser o " +
                    "where o.enterpriseId = :enterpriseId and o.weixinOpenId = :openId", WeixinUser.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("openId", openId)
            .getResultList();

    return users.isEmpty() ? null: users.get(0);
  }

  @Override
  public int createWeixinTemplate(WeixinTemplate tpl) {
    int retCode = persist(tpl);
    return retCode == 1 ? tpl.getId() : retCode;
  }

  @Override
  public WeixinTemplate searchWeixinTemplate(int ownerId, String id) {
    List<WeixinTemplate> list = em.createQuery("select o from WeixinTemplate o " +
                    "where o.enterpriseId = :ownerId and o.businessId = :id", WeixinTemplate.class)
            .setParameter("ownerId", ownerId)
            .setParameter("id", id)
            .getResultList();

    return list.isEmpty() ? null:  list.get(0);
  }

  @Override
  public List<WeixinTemplate> searchWeixinTemplates(int ownerId) {
    return em.createQuery("select o from WeixinTemplate o " +
                    "where o.enterpriseId = :ownerId ", WeixinTemplate.class)
            .setParameter("ownerId", ownerId)
            .getResultList();
  }

  @Override
  public List<WeixinCustomMenu> searchCustomMenus(int ownerId) {
    return em.createQuery("select o from WeixinCustomMenu o " +
                    "where o.enterpriseId = :enterpriseId " +
                    "order by o.level, o.parentId, o.orderNum, o.id", WeixinCustomMenu.class)
            .setParameter("enterpriseId", ownerId)
            .getResultList();
  }

  @Override
  public int deleteCustomMenu(int ownerId, int id) {
    int retCode = 0;
    retCode = em.createQuery("delete from WeixinCustomMenu o " +
                    "where o.enterpriseId = :enterpriseId and o.id = :id")
            .setParameter("enterpriseId", ownerId)
            .setParameter("id", id)
            .executeUpdate();

    return retCode;
  }

  @Override
  public int createCustomMenu(WeixinCustomMenu o) {
    int retCode = persist(o);
    return retCode == 1 ? o.getId() : retCode;
  }

  @Override
  public int createWeixinMaterial(WeixinMaterial o) {
    int retCode = persist(o);
    return retCode == 1 ? o.getId() : retCode;
  }

  @Override
  public int updateWeixinMaterial(WeixinMaterial wm) {
    int retCode;

    em.merge(wm);

    retCode = 1;

    return retCode;
  }

  /**
   * 本地查找微信永久素材
   */
  public List<WeixinMaterial> searchMaterials(int enterpriseId, CommonSearchCriteria sc) {
    List<WeixinMaterial> results = null;

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<WeixinMaterial> cq = cb.createQuery(WeixinMaterial.class);
    Root<WeixinMaterial> root = cq.from(WeixinMaterial.class);

    Predicate where = cb.conjunction();
    where = cb.and(where, cb.equal(root.get(WeixinMaterial_.enterpriseId), enterpriseId));

    cq.where(where);
    List<javax.persistence.criteria.Order> orderByes = new ArrayList<>();
    orderByes.add(cb.desc(root.get("id")));

    cq.orderBy(orderByes);

    //计算根据条件查询得出的数据总数
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<WeixinMaterial> countRoot = countQuery.from(WeixinMaterial.class);

    Predicate where0 = cb.conjunction();
    where0 = cb.and(where0, cb.equal(countRoot.get(WeixinMaterial_.enterpriseId), enterpriseId));


    countQuery.select(cb.count(countRoot)).where(where0);
    Long count = em.createQuery(countQuery).getSingleResult();

    int maxResults = count.intValue();
    if (maxResults > 0) {
      Page page = sc.getPage();
      page.setRowCount(maxResults);

      try {
        results = em.createQuery(cq)
                .setFirstResult(page.getFirstPosition())
                .setMaxResults(page.getPageSize()).getResultList();

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    if (results == null) results = new ArrayList<>();

    return results;
  }

  public List<WeixinMaterial> searchMaterials(int enterpriseId, String mediaId) {
    return em.createQuery("select o from WeixinMaterial o " +
                    "where o.enterpriseId = :ownerId and o.mediaId = :mediaId", WeixinMaterial.class)
            .setParameter("ownerId", enterpriseId)
            .setParameter("mediaId", mediaId)
            .getResultList();
  }

  @Override
  public WeixinMaterial searchWeixinMaterial(int weixinId, int id) {
    WeixinMaterial o = em.find(WeixinMaterial.class, id);
    if (o != null && o.getEnterpriseId() != weixinId) {
      return null;
    } else {
      return o;
    }
  }

  @Override
  public List<WeixinNews> searchWeixinNews(int weixinId) {
    return em.createQuery("select o from WeixinNews o " +
                    "where o.enterpriseId = :enterpriseId order by o.displayOrder", WeixinNews.class)
            .setParameter("enterpriseId", weixinId)
            .getResultList();
  }

  @Override
  public int createWxNews(WeixinNews o) {
    return persist(o);
  }

  @Override
  public int deleteWxNews(int enterpriseId, int id) {
    int retCode = 0;
    retCode = em.createQuery("delete from  WeixinNews o  where o.enterpriseId = :enterpriseId and o.id = :id")
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("id", id)
            .executeUpdate();

    return retCode;
  }

  @Override
  public WeixinEntConfig getWeixinEntConfig(int enterpriseId, int msgType) {
    List<WeixinEntConfig> list = em.createQuery("select o from WeixinEntConfig o " +
                    " where o.enterpriseId = :enterpriseId and o.msgType = :msgType", WeixinEntConfig.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("msgType", msgType)
            .getResultList();

    if (list == null || list.isEmpty()) {
      return null;
    } else {
      return list.get(0);
    }
  }

  @Override
  public List<WeixinGroup> listWeixinGroup(int weixinId) {
    List<WeixinGroup> groups = em.createQuery("select o from WeixinGroup o " +
                    " where o.enterpriseId = :weixinId ", WeixinGroup.class)
            .setParameter("weixinId", weixinId)
            .getResultList();

    return groups == null ? new ArrayList<>() : groups;
  }

  @Override
  public WeixinGroup getWeixinGroup(int weixinId, int groupId) {
    List<WeixinGroup> groups = em.createQuery("select o from WeixinGroup o " +
                    " where o.enterpriseId = :weixinId " +
                    " and o.groupId = :groupId", WeixinGroup.class)
            .setParameter("weixinId", weixinId)
            .setParameter("groupId", groupId)
            .getResultList();

    return groups == null ? null : groups.get(0);
  }

  @Override
  public List<WeixinUser> listWeixinUser(int weixinId) {
    List<WeixinUser> users = em.createQuery("select o from WeixinUser o " +
                    " where o.enterpriseId = :weixinId ", WeixinUser.class)
            .setParameter("weixinId", weixinId)
            .getResultList();

    return users == null ? new ArrayList<>() : users;
  }

  @Override
  public int updateWeixinGroup(WeixinGroup weixinGroup) {
    int retCode = 0;
    em.merge(weixinGroup);

    retCode = 1;

    return retCode;
  }

  @Override
  public WeixinUser getWeixinUser(int weixinId, String weixinOpenId) {
    List<WeixinUser> users = em.createQuery("select o from WeixinUser o " +
                    " where o.enterpriseId = :weixinId " +
                    " and o.weixinOpenId = :weixinOpenId", WeixinUser.class)
            .setParameter("weixinId", weixinId)
            .setParameter("weixinOpenId", weixinOpenId)
            .getResultList();

    return users == null ? null : users.get(0);
  }

  @Override
  public WeixinUser getWeixinUser(int weixinId, int userId) {
    List<WeixinUser> users = em.createQuery("select o from WeixinUser o " +
                    " where o.enterpriseId = :weixinId " +
                    " and o.id = :userId", WeixinUser.class)
            .setParameter("weixinId", weixinId)
            .setParameter("userId", userId)
            .getResultList();
    WeixinUser user = users == null ? null : users.get(0);
    return user;
  }

  @Override
  public int updateWeixinUser(WeixinUser user) {
    int retCode = 0;
    user.setLastUpdate(DateUtil.getCurDateTime());
    em.merge(user);

    retCode = 1;

    return retCode;
  }

  @Override
  public int createWeixinGroup(WeixinGroup group) {
    int retCode = 0;
    em.persist(group);

    retCode = 1;

    return retCode;
  }

  @Override
  public int saveSuiteTicket(int enterpriseId, String suiteId, String suiteTicket, Date timeStamp) {
    int retCode = 0;

    WwProviderTicket ticket;
    List<WwProviderTicket> tickets = em.createQuery("select o from WwProviderTicket o " +
                    "where o.enterpriseId = :enterpriseId and o.suiteId = :suiteId", WwProviderTicket.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("suiteId", suiteId)
            .getResultList();
    if (tickets.size() > 0) {
      ticket = tickets.get(0);
      ticket.setSuiteTicket(suiteTicket);
      ticket.setTimeStamp(timeStamp);
      ticket.setLastUpdate(DateUtil.getCurDateTime());
      em.merge(ticket);
    } else {
      ticket = new WwProviderTicket();
      ticket.setEnterpriseId(enterpriseId);
      ticket.setSuiteId(suiteId);
      ticket.setSuiteTicket(suiteTicket);
      ticket.setTimeStamp(timeStamp);
      em.persist(ticket);
    }


    retCode = ticket.getId();

    return retCode;
  }

  @Override
  public WwProviderTicket getSuiteTicket(int enterpriseId, String suiteId) {
    List<WwProviderTicket> tickets = em.createQuery("select o from WwProviderTicket o " +
                    "where o.enterpriseId = :enterpriseId and o.suiteId = :suiteId", WwProviderTicket.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("suiteId", suiteId)
            .getResultList();
    return tickets.size() > 0 ? tickets.get(0) : null;
  }

  @Override
  public WwProviderToken retrieveWwProviderToken(int enterpriseId, String suiteId) {
    List<WwProviderToken> tokens = em.createQuery("select o from WwProviderToken o " +
                    "where o.enterpriseId = :enterpriseId and o.suiteId = :suiteId", WwProviderToken.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("suiteId", suiteId)
            .getResultList();
    return tokens.size() > 0 ? tokens.get(0) : null;
  }

  @Override
  public int saveWwProviderToken(int enterpriseId, String suiteId, String suiteAccessToken, int expiresIn) {
    int retCode = 0;

    WwProviderToken token;
    List<WwProviderToken> tokens = em.createQuery("select o from WwProviderToken o " +
                    "where o.enterpriseId = :enterpriseId and o.suiteId = :suiteId", WwProviderToken.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("suiteId", suiteId)
            .getResultList();
    if (tokens.size() > 0) {
      token = tokens.get(0);
      token.setAccessToken(suiteAccessToken);
      token.setExpiresIn(expiresIn);
      token.setLastUpdate(DateUtil.getCurDateTime());
      em.merge(token);
    } else {
      token = new WwProviderToken();
      token.setEnterpriseId(enterpriseId);
      token.setSuiteId(suiteId);
      token.setAccessToken(suiteAccessToken);
      token.setExpiresIn(expiresIn);
      token.setLastUpdate(DateUtil.getCurDateTime());

      em.persist(token);
    }


    retCode = token.getId();

    return retCode;
  }

  /**
   * 保存授权企业的信息
   */
  public int saveWwpAuthCorpInfo(WwProviderAuthCorpInfo corpInfo) {
    int retCode = 0;

    WwProviderAuthCorpInfo info;
    List<WwProviderAuthCorpInfo> infos = em.createQuery("select o from WwProviderAuthCorpInfo o " +
                    "where o.enterpriseId = :enterpriseId and o.suiteId = :suiteId and o.corpId = :corpId", WwProviderAuthCorpInfo.class)
            .setParameter("enterpriseId", corpInfo.getEnterpriseId())
            .setParameter("suiteId", corpInfo.getSuiteId())
            .setParameter("corpId", corpInfo.getCorpId())
            .getResultList();
    if (infos.size() > 0) {
      info = infos.get(0);
      info.setStatus(corpInfo.getStatus());
      info.setAccessToken(corpInfo.getAccessToken());
      info.setExpiresIn(corpInfo.getExpiresIn());
      info.setPermanentCode(corpInfo.getPermanentCode());
      info.setLastUpdate(DateUtil.getCurDateTime());

      em.merge(info);
    } else {

      em.persist(corpInfo);
      info = corpInfo;
    }


    retCode = info.getId();

    return retCode;
  }

  @Override
  public WwProviderAuthCorpInfo getWwpAuthCorpInfo(int enterpriseId, String suiteId, String corpId) {
    List<WwProviderAuthCorpInfo> infos = em.createQuery("select o from WwProviderAuthCorpInfo o " +
                    "where o.enterpriseId = :enterpriseId and o.suiteId = :suiteId and o.corpId = :corpId", WwProviderAuthCorpInfo.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("suiteId", suiteId)
            .setParameter("corpId", corpId)
            .getResultList();
    return infos.size() > 0 ? infos.get(0) : null;
  }

  @Override
  public int updateWwpAuthCorpAccessToken(int enterpriseId, String authCorpId, String access_token, int expires_in) {
    int retCode = 0;
    WwProviderAuthCorpInfo token;
    List<WwProviderAuthCorpInfo> tokens = em.createQuery("select o from WwProviderAuthCorpInfo o " +
                    "where o.enterpriseId = :enterpriseId and o.corpId = :corpId", WwProviderAuthCorpInfo.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("corpId", authCorpId)
            .getResultList();
    if (tokens.size() > 0) {
      token = tokens.get(0);
      token.setAccessToken(access_token);
      token.setExpiresIn(expires_in);
      token.setLastUpdate(DateUtil.getCurDateTime());
      em.merge(token);
    } else {
      token = new WwProviderAuthCorpInfo();
      token.setEnterpriseId(enterpriseId);
      token.setCorpId(authCorpId);
      token.setAccessToken(access_token);
      token.setExpiresIn(expires_in);
      token.setLastUpdate(DateUtil.getCurDateTime());

      em.persist(token);
    }


    retCode = token.getId();

    return retCode;
  }

  @Override
  public int cancelSuiteAuthInfo(int enterpriseId, String suiteId, String authCorpId) {
    int retCode = 0;

    List<WwProviderAuthCorpInfo> infos = em.createQuery("select o from WwProviderAuthCorpInfo o " +
                    "where o.enterpriseId = :enterpriseId and o.suiteId = :suiteId and o.corpId = :corpId", WwProviderAuthCorpInfo.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("suiteId", suiteId)
            .setParameter("corpId", authCorpId)
            .getResultList();
    if (infos.size() == 1) {
      WwProviderAuthCorpInfo info = infos.get(0);
      info.setStatus(0);
      info.setLastUpdate(DateUtil.getCurDateTime());

      em.merge(info);
    }


    retCode = 1;

    return retCode;
  }

  /**
   * 获取微信服务号的API配置
   */
  @Override
  public WeixinServiceConfig getWeixinServiceConfig(int enterpriseId) {
      List<WeixinServiceConfig> list = em.createQuery("select o from WeixinServiceConfig o " +
                      "where o.enterpriseId = :enterpriseId and o.msgType = :msgType", WeixinServiceConfig.class)
              .setParameter("enterpriseId", enterpriseId)
              .setParameter("msgType", 0)
              .getResultList();
      return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public int saveWeixinServiceConfig(WeixinServiceConfig config) {
      int retCode = 0;
      List<WeixinServiceConfig> list = em.createQuery("select o from WeixinServiceConfig o " +
                      "where o.enterpriseId = :enterpriseId and o.msgType = :msgType", WeixinServiceConfig.class)
              .setParameter("enterpriseId", config.getEnterpriseId())
              .setParameter("msgType", config.getMsgType())
              .getResultList();
      if (list.isEmpty()) {
        config.setId(0);
        em.persist(config);

        retCode = config.getId();
      } else {
        WeixinServiceConfig old = list.get(0);
        old.setAppId(config.getAppId());
        old.setAppSecret(config.getAppSecret());
        old.setToken(config.getToken());
        old.setEncodingKey(config.getEncodingKey());

        old.setLastUpdate(DateUtil.getCurDateTime());
        em.merge(old);

        retCode = old.getId();
      }

      return retCode;
  }

  @Override
  public int saveWorkWeixinConfig(WeixinEntConfig config) {
    int retCode = 0;
    List<WeixinEntConfig> list = em.createQuery("select o from WeixinEntConfig o " +
                    "where o.enterpriseId = :enterpriseId and o.msgType = :msgType", WeixinEntConfig.class)
            .setParameter("enterpriseId", config.getEnterpriseId())
            .setParameter("msgType", config.getMsgType())
            .getResultList();
    if (list.isEmpty()) {
      config.setId(0);
      em.persist(config);

      retCode = config.getId();
    } else {
      WeixinEntConfig old = list.get(0);
      old.setCorpId(config.getCorpId());
      old.setAgentId(config.getAgentId());
      old.setSecret(config.getSecret());
      old.setToken(config.getToken());
      old.setEncodingAESKey(config.getEncodingAESKey());

      old.setLastUpdate(DateUtil.getCurDateTime());
      em.merge(old);

      retCode = old.getId();
    }


    return retCode;
  }

  @Override
  public WeixinEntConfig getWorkWeixinConfig(int enterpriseId, int msgType) {
    List<WeixinEntConfig> list = em.createQuery("select o from WeixinEntConfig o " +
                    "where o.enterpriseId = :enterpriseId and o.msgType = :msgType", WeixinEntConfig.class)
            .setParameter("enterpriseId", enterpriseId)
            .setParameter("msgType", msgType)
            .getResultList();
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public List<WeixinEntConfig> searchWorkWeixinConfigs(int enterpriseId) {
    return em.createQuery("select o from WeixinEntConfig o " +
                    "where o.enterpriseId = :enterpriseId order by o.msgType, o.agentId", WeixinEntConfig.class)
            .setParameter("enterpriseId", enterpriseId)
            .getResultList();
  }

}
