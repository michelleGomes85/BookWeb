package br.tsi.daw.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

public class DAO<T> {
	
	private Class<T> classDao;
	
	public DAO(Class<T> classe) {
		this.classDao = classe;
	}
	
	public void add(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		
		em.getTransaction().begin();
		em.persist(t);
		em.getTransaction().commit();
		em.close();
	}
	
	public void update(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		
		em.getTransaction().begin();
		em.merge(t);
		em.getTransaction().commit();
		em.close();
	}
	
	public void remove(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		
		em.getTransaction().begin();
		em.remove(em.merge(t));
		em.getTransaction().commit();
		em.close();
	}
	
	public T buscaPorId(Long id) {
		EntityManager em = new JPAUtil().getEntityManager();
		return em.find(classDao, id);
	}
	
	public List<T> listAll(){
		EntityManager em = new JPAUtil().getEntityManager();
		
		CriteriaQuery<T> query = 
				em.getCriteriaBuilder().createQuery(classDao);
		
		query.select(query.from(classDao));
		
		List<T> list = em.createQuery(query).getResultList();
		
		return list;
	}
}

