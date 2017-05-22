package cn.buk.api.wechat.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public abstract class AbstractDao {

	@PersistenceUnit(unitName = "mainEMF")
	private EntityManagerFactory emf;

	protected EntityManager createEntityManager() {
		return emf.createEntityManager();
	}


	/**
	 * 保存新对象
	 * @param object
	 * @return
	 * 1 表示成功
	 * -100 表示出错
	 */
	protected int persist(Object object) {
		int retCode;

		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(object);
			em.getTransaction().commit();

			retCode = 1;
		} catch (Exception ex) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();

			retCode = -100;
			ex.printStackTrace();
		} finally {
			em.close();
		}

		return retCode;
	}
}
