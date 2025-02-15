package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.model.Book;
import br.tsi.daw.model.ItemOrder;
import br.tsi.daw.model.Order;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named("orderMB")
public class OrderMB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Order order = new Order();
	private Integer spinnerValue = 1;
    private List<ItemOrder> cart = new ArrayList<>();
	private Long bookId;
	
	private ItemOrder itemOrder;
	
    @Inject
    private UserMB userMB;

	public Integer getSpinnerValue() {
		return spinnerValue;
	}

	public void setSpinnerValue(Integer spinnerValue) {
		this.spinnerValue = spinnerValue;
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
        
        if (book == null || spinnerValue < 1) {
            return;
        }

        ItemOrder item = new ItemOrder();
        item.setBook(book);
        item.setAmount(spinnerValue);
        item.setTotalPrice(book.getPrice() * spinnerValue);
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
        return "cart?faces-redirect=true";
    }

    public String finalizeOrder() {
    	
        if (userMB.getUserSession() == null) {
        	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("redirectTo", "cart");
            return "login?faces-redirect=true";
        }

        order.setItensOrder(cart);
        order.setClient(userMB.getUserSession().getClient());
        order.setTotalPrice(getTotalCartValue());

        DAO<Order> dao = new DAO<>(Order.class);
        dao.add(order);

        for (ItemOrder item : cart) {
            Book book = item.getBook();
            book.setQuantityStock(book.getQuantityStock() - item.getAmount());
            new DAO<>(Book.class).update(book);
        }

        cart.clear();
        
        return "library?faces-redirect=true";
    }
}
