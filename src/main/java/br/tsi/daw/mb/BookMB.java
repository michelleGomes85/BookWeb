package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.model.Book;
import br.tsi.daw.model.Category;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named("bookMB")
public class BookMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Book> books;
	private List<Book> filteredBooks;
	private List<Category> categories;
    
    private String searchTerm = "";
    private String selectedCategory = "";
    
    @PostConstruct
    public void init() {
        books = listAll();
        filteredBooks = books;
        categories = getCategories();
    }
    
    public List<Category> getCategories() {
        
    	if (categories == null) {
            DAO<Category> categoryDAO = new DAO<>(Category.class);
            categories = categoryDAO.listAll();
        }
        
        return categories;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Book> getFilteredBooks() {
		return filteredBooks;
	}

	public void setFilteredBooks(List<Book> filteredBooks) {
		this.filteredBooks = filteredBooks;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void filterBooks() {
        filteredBooks = books.stream()
            .filter(book -> book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
            .filter(book -> selectedCategory.isEmpty() || book.getCategory().getName().equals(selectedCategory))
            .collect(Collectors.toList());
    }

	public List<Book> listAll() {
	    DAO<Book> bookDAO = new DAO<>(Book.class);
	    return bookDAO.listAll().stream()
	            .filter(book -> book.getAvailable())
	            .collect(Collectors.toList());
	}
}