package br.tsi.daw.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Category {
	
	@Id
	@SequenceGenerator(
			name="category_id", 
			sequenceName="category_seq",
			allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="category_id")
	private Long id;
	private String name;
	
    @OneToMany(mappedBy = "category")
	List<Book> books = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	
}
