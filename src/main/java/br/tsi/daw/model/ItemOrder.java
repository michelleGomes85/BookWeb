package br.tsi.daw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_order")
public class ItemOrder {
	
	@Id
	@SequenceGenerator(
			name="itemOrder_id", 
			sequenceName="itemOrder_seq",
			allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="itemOrder_id")
	private Long id;
	
	private int amount;
	private double totalPrice;
	
    @ManyToOne
    @JoinColumn(name = "book_id")
	private Book book;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
