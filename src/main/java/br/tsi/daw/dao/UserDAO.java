package br.tsi.daw.dao;



import br.tsi.daw.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class UserDAO {
	
	public boolean exists(User user) {
		EntityManager em = new JPAUtil().getEntityManager();
	
		Query query = em.createQuery
			("from User u where u.login = :pLogin and u.password = :pPassword");
		
		query.setParameter("pLogin", user.getLogin());
		query.setParameter("pPassword", user.getPassword());
		boolean encontrado = !query.getResultList().isEmpty();
		em.close();
		
		return encontrado;
	}
}
