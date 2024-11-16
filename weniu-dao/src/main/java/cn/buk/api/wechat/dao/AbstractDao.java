package cn.buk.api.wechat.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;

/**
 * @author yfdai
 */
public abstract class AbstractDao {

	@PersistenceContext(unitName = "weniu")
	protected EntityManager em;

	/**
	 * 保存新对象
	 * @return
	 * 1 表示成功
	 * -100 表示出错
	 */
	protected int persist(Object object) {
		int retCode;

			em.persist(object);

			retCode = 1;

		return retCode;
	}
}
