package br.tsi.daw.dao;

import java.util.List;

import br.tsi.daw.model.Order;
import br.tsi.daw.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class OrderDAO {

    @SuppressWarnings("unchecked")
    public List<Order> findOrderByUser(User user) {
        EntityManager em = new JPAUtil().getEntityManager();

        try {
            // Consulta para carregar os pedidos e os itens dos pedidos
            Query query = em.createQuery(
                "SELECT DISTINCT o FROM Order o " +
                "JOIN FETCH o.client c " +
                "JOIN FETCH o.itensOrder i " +
                "WHERE c.id = :clientId", Order.class);

            query.setParameter("clientId", user.getClient().getId());

            return query.getResultList();
        } finally {
            em.close();
        }
    }
}