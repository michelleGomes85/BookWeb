package br.tsi.daw.dao;

import java.util.List;

import br.tsi.daw.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class UserDAO {

	public User findByLoginAndPassword(User user) {

		EntityManager em = new JPAUtil().getEntityManager();

		try {
			Query query = em.createQuery("from User u where u.login = :pLogin and u.password = :pPassword", User.class);

			query.setParameter("pLogin", user.getLogin());
			query.setParameter("pPassword", user.getPassword());

			@SuppressWarnings("unchecked")
			List<User> users = query.getResultList();

			if (!users.isEmpty())
				return users.get(0);
			else
				return null;

		} finally {
			em.close();
		}
	}
	
    public User findByField(String fieldName, String fieldValue) {
        EntityManager em = new JPAUtil().getEntityManager();

        try {
            String jpql = "SELECT u FROM User u WHERE u." + fieldName + " = :fieldValue";
            Query query = em.createQuery(jpql, User.class);
            query.setParameter("fieldValue", fieldValue);

            @SuppressWarnings("unchecked")
            List<User> users = query.getResultList();

            return users.isEmpty() ? null : users.get(0);

        } finally {
            em.close();
        }
    }
}
