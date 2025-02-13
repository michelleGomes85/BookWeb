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
}
