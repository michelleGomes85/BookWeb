package br.tsi.daw.mb;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.model.Book;
import br.tsi.daw.model.ItemOrder;
import br.tsi.daw.model.Order;
import br.tsi.daw.utils.EmailTemplateUtils;
import br.tsi.daw.utils.SendEmailUtils;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named("orderMB")
public class OrderMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Order order = new Order();
	private Map<Long, Integer> spinnerValues = new HashMap<>();
	private List<ItemOrder> cart = new ArrayList<>();
	private Long bookId;

	private ItemOrder itemOrder;

	@Inject
	private UserMB userMB;

	@Inject
	private NavigationMB navigationMB;
	
	@PostConstruct
	public void init() {
		
	    spinnerValues = new HashMap<>();
	    
	    DAO<Book> dao = new DAO<>(Book.class);
	    List<Book> books = dao.listAll();
	    
	    for (Book book : books) {
	        spinnerValues.put(book.getId(), 1);
	    }
	}

	public Integer getSpinnerValue(Long bookId) {
	    Object value = spinnerValues.getOrDefault(bookId, 1);
	    
	    if (value instanceof String)
	        return Integer.parseInt((String) value);
	    
	    return (Integer) value; 
	}

	public Map<Long, Integer> getSpinnerValues() {
		return spinnerValues;
	}
	
	public void setSpinnerValues(Long bookId, Integer value) {
	    spinnerValues.put(bookId, value);
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public ItemOrder getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(ItemOrder itemOrder) {
		this.itemOrder = itemOrder;
	}

	public List<ItemOrder> getCart() {
		return cart;
	}

	public void setCart(List<ItemOrder> cart) {
		this.cart = cart;
	}

	public void addToCart(Long bookId) {
		
	    DAO<Book> dao = new DAO<>(Book.class);
	    Book book = dao.searchById(bookId);
	    
	    if (book == null || getSpinnerValue(bookId) < 1)
	        return;

	    Integer quantity = getSpinnerValue(bookId);

	    ItemOrder item = new ItemOrder();
	    item.setBook(book);
	    item.setAmount(quantity);
	    item.setUnitPrice(book.getPrice());
	    item.setTotalPrice(book.getPrice() * quantity);
	    item.setOrder(order);

	    cart.add(item);
	}

	public Integer getCartQuantity() {
		return cart.size();
	}

	public void updateQuantity(ItemOrder item) {
		item.setTotalPrice(item.getBook().getPrice() * item.getAmount());
	}

	public void removeFromCart(ItemOrder item) {
		cart.remove(item);
	}

	public Double getTotalCartValue() {
		return cart.stream().mapToDouble(ItemOrder::getTotalPrice).sum();
	}

	public String navigateToCart() {
		return navigationMB.toCart();
	}

	public String finalizeOrder() {
		
	    if (userMB.getUserSession() == null) {
	    	String sessionToken = generateSessionToken();
	        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionToken", sessionToken);
	        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("redirectTo", "cart");
	        return navigationMB.toLogin();
	    }

	    try {
	    	
	        for (ItemOrder item : cart) {
	        	
	            Book book = item.getBook();
	            
	            if (item.getAmount() > book.getQuantityStock()) {
	                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
	                    "Não foi possível finalizar o pedido. Quantidade insuficiente em estoque para o livro: " + book.getTitle(), null));
	                return null;
	            }
	        }

	        order = new Order();
	        order.setClient(userMB.getUserSession().getClient());
	        order.setTotalPrice(getTotalCartValue());
	        order.setDateOrder(new Date());

	        DAO<Order> orderDAO = new DAO<>(Order.class);
	        orderDAO.add(order);

	        for (ItemOrder item : cart) {
	            item.setOrder(order);

	            Book book = item.getBook();
	            book.setQuantityStock(book.getQuantityStock() - item.getAmount());
	            new DAO<>(Book.class).update(book);

	            DAO<ItemOrder> itemOrderDAO = new DAO<>(ItemOrder.class);
	            itemOrderDAO.add(item);
	        }

	        SendEmailUtils.sendEmail(
	            userMB.getUserSession().getClient().getEmail(),
	            "Seu Pedido foi Finalizado - Library Virtual",
	            EmailTemplateUtils.getOrderFinalizationEmail(
	                userMB.getUserSession().getClient().getName(),
	                order.getId(),
	                new SimpleDateFormat("dd/MM/yyyy HH:mm").format(order.getDateOrder()),
	                order.getTotalPrice(),
	                cart
	            )
	        );

	        cart.clear();

	        return navigationMB.toLibrary();
	    } catch (Exception e) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao finalizar o pedido: " + e.getMessage(), null));
	        return null;
	    }
	}
	
	
	private String generateSessionToken() {
	    return UUID.randomUUID().toString(); 
	}
}
