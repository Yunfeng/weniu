package cn.buk.api.wechat.dao;

import cn.buk.api.wechat.dto.CommonSearchCriteria;
import cn.buk.api.wechat.dto.Page;
import cn.buk.api.wechat.entity.*;
import cn.buk.util.DateUtil;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
public class WeixinDaoImpl extends AbstractDao implements WeixinDao {

    public int createWeixinOauthToken(WeixinOauthToken token) {
        int retCode = persist(token);
        return retCode == 1 ? token.getId(): retCode;
    }

    @Override
    public int createWeixinAccessTime(String weixinOpenId, int weixinId) {
        int retCode = 0;
        EntityManager em = createEntityManager();
        try {
            List<WeixinAccessTime> accessTimes = em.createQuery("select o from WeixinAccessTime o " +
                    " where o.weixinOpenId = :weixinOpenId and o.weixinId = :weixinId ")
                    .setParameter("weixinOpenId", weixinOpenId)
                    .setParameter("weixinId", weixinId)
                    .getResultList();

            em.getTransaction().begin();
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
            em.getTransaction().commit();

            retCode = accessTime.getId();
        } catch (Exception ex) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            ex.printStackTrace();
            retCode = -1;
        } finally {
            em.close();
        }

        return retCode;
    }

    @Override
    public Token retrieveWeixinToken(final int weixinId, final int weixinType) {
        EntityManager em = createEntityManager();
        try {
            List<Token> tokens = em.createQuery("select o from Token o " +
                    "where o.weixinId = :weixinId and o.weixinType = :weixinType " +
                    "order by o.id desc")
                    .setParameter("weixinId", weixinId)
                    .setParameter("weixinType", weixinType)
                    .setMaxResults(1)
                    .getResultList();
            if (tokens == null || tokens.size() == 0)
                return null;
            else
                return tokens.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public int createWeixinToken(Token token) {
        int retCode = persist(token);
        return retCode == 1 ? token.getId(): retCode;
    }

    @Override
    public int createWeixinUser(WeixinUser user) {
        int retCode = persist(user);
        return retCode == 1 ? user.getId(): retCode;
    }

    @Override
    public List<WeixinUser> searchSubscribers(int enterpriseId, CommonSearchCriteria sc) {
        List<WeixinUser> users = null;
        EntityManager em = createEntityManager();
        try {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<WeixinUser> cq = cb.createQuery(WeixinUser.class);
            Root<WeixinUser> root = cq.from(WeixinUser.class);

            Predicate where = cb.conjunction();
            where = cb.and(where, cb.equal(root.get(WeixinUser_.ownerId), enterpriseId));

            cq.where(where);
            List<javax.persistence.criteria.Order> orderByes = new ArrayList<>();
            orderByes.add(cb.asc(root.get("subscribe_time")));

            cq.orderBy(orderByes);

            //计算根据条件查询得出的数据总数
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<WeixinUser> countRoot = countQuery.from(WeixinUser.class);

            Predicate where0 = cb.conjunction();
            where0 = cb.and(where0, cb.equal(countRoot.get(WeixinUser_.ownerId), enterpriseId));


            countQuery.select(cb.count(countRoot)).where(where0);
            Long count = em.createQuery(countQuery).getSingleResult();

            int maxResults = count.intValue();
            if (maxResults > 0) {
                Page page = sc.getPage();
                page.setRowCount(maxResults);
                int i = (page.getPageNo() - 1) * page.getPageSize();

                if (i < 0)
                    i = 0;

                try {
                    users = em.createQuery(cq)
                            .setFirstResult(i)
                            .setMaxResults(page.getPageSize()).getResultList();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            em.close();
        }

        if (users == null) users = new ArrayList<>();

        return users;
    }

    @Override
    public WeixinUser searchWeixinUser(int enterpriseId, String openId) {
        EntityManager em = createEntityManager();
        try {
            List<WeixinUser> users =  em.createQuery("select o from WeixinUser o " +
                    "where o.ownerId = :enterpriseId and o.weixinOpenId = :openId")
                    .setParameter("enterpriseId",enterpriseId)
                    .setParameter("openId", openId)
                    .getResultList();

            return users.size() > 0 ? users.get(0) : null;

        } finally {
            em.close();
        }
    }

    @Override
    public int createWeixinTemplate(WeixinTemplate tpl) {
        int retCode = persist(tpl);
        return retCode == 1 ? tpl.getId(): retCode;
    }

    @Override
    public WeixinTemplate searchWeixinTemplate(int ownerId, String id) {
        EntityManager em = createEntityManager();
        try {
            List<WeixinTemplate> list =  em.createQuery("select o from WeixinTemplate o " +
                    "where o.ownerId = :ownerId and o.businessId = :id")
                    .setParameter("ownerId",ownerId)
                    .setParameter("id", id)
                    .getResultList();

            return list.size() > 0 ? list.get(0) : null;

        } finally {
            em.close();
        }
    }

    @Override
    public List<WeixinTemplate> searchWeixinTemplates(int ownerId) {
        EntityManager em = createEntityManager();
        try {
            List<WeixinTemplate> list =  em.createQuery("select o from WeixinTemplate o " +
                    "where o.ownerId = :ownerId ")
                    .setParameter("ownerId",ownerId)
                    .getResultList();

            return list;

        } finally {
            em.close();
        }
    }

    @Override
    public List<WeixinCustomMenu> searchCustomMenus(int ownerId) {
        EntityManager em = createEntityManager();
        try {
            return em.createQuery("select o from WeixinCustomMenu o " +
                    "where o.enterpriseId = :enterpriseId order by o.level, o.parentId, o.id")
                    .setParameter("enterpriseId", ownerId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public int deleteCustomMenu(int ownerId, int id) {
        int retCode = 0;
        EntityManager em = createEntityManager();
        try {
            em.getTransaction().begin();
            retCode = em.createQuery("delete from WeixinCustomMenu o " +
                    "where o.enterpriseId = :enterpriseId and o.id = :id")
                    .setParameter("enterpriseId", ownerId)
                    .setParameter("id", id)
                    .executeUpdate();

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            retCode = -100;
        } finally {
            em.close();
        }

        return retCode;
    }

    @Override
    public int createCustomMenu(WeixinCustomMenu o) {
        int retCode = persist(o);
        return retCode == 1 ? o.getId(): retCode;
    }

    @Override
    public int createWeixinMaterial(WeixinMaterial o) {
        int retCode = persist(o);
        return retCode == 1 ? o.getId(): retCode;
    }

    /**
     * 本地查找微信永久素材
     * @param enterpriseId
     * @param sc
     * @return
     */
    public List<WeixinMaterial> searchMaterials(int enterpriseId, CommonSearchCriteria sc) {
        List<WeixinMaterial> results = null;
        EntityManager em = createEntityManager();
        try {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<WeixinMaterial> cq = cb.createQuery(WeixinMaterial.class);
            Root<WeixinMaterial> root = cq.from(WeixinMaterial.class);

            Predicate where = cb.conjunction();
            where = cb.and(where, cb.equal(root.get(WeixinMaterial_.ownerId), enterpriseId));

            cq.where(where);
            List<javax.persistence.criteria.Order> orderByes = new ArrayList<>();
            orderByes.add(cb.desc(root.get("id")));

            cq.orderBy(orderByes);

            //计算根据条件查询得出的数据总数
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<WeixinMaterial> countRoot = countQuery.from(WeixinMaterial.class);

            Predicate where0 = cb.conjunction();
            where0 = cb.and(where0, cb.equal(countRoot.get(WeixinMaterial_.ownerId), enterpriseId));


            countQuery.select(cb.count(countRoot)).where(where0);
            Long count = em.createQuery(countQuery).getSingleResult();

            int maxResults = count.intValue();
            if (maxResults > 0) {
                Page page = sc.getPage();
                page.setRowCount(maxResults);

                try {
                    results = em.createQuery(cq)
                            .setFirstResult(page.getFirst())
                            .setMaxResults(page.getPageSize()).getResultList();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            em.close();
        }

        if (results == null) results = new ArrayList<>();

        return results;
    }

    @Override
    public WeixinMaterial searchWeixinMaterial(int weixinId, int id) {
        EntityManager em = createEntityManager();
        try {
            WeixinMaterial o = em.find(WeixinMaterial.class, id);
            if (o != null && o.getOwnerId() != weixinId)
                return null;
            else
                return o;
        } finally {
            em.close();
        }
    }

}
