package br.tsi.daw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Book {
	
	@Id
	@SequenceGenerator(
			name="book_id", 
			sequenceName="book_seq",
			allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_id")
	private Long id;
	private String ISBN;
	private String title;
	private String description;
	private Integer quantityStock;
	private Boolean available;
	private String author;
	private Double price;
	
    @ManyToOne
    @JoinColumn(name = "category_id")
	private Category category = new Category();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantityStock() {
		return quantityStock;
	}

	public void setQuantityStock(Integer quantityStock) {
		this.quantityStock = quantityStock;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
