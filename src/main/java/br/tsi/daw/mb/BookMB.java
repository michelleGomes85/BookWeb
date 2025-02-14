package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;

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
	
    private Book selectedBook;
    private Long idCategory;
    
    private String searchTerm = "";
    private String selectedCategory = "";
    
    @PostConstruct
    public void init() {
        books = listAll();
        filteredBooks = books;
        selectedBook = new Book();
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

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
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

    public void onRowEdit(RowEditEvent<Book> event) {
        Book editedBook = event.getObject();
        saveOrUpdateBook(editedBook);
        
        // Adiciona uma mensagem de sucesso
        PrimeFaces.current().executeScript("PF('msgs').renderMessage({'summary':'Livro atualizado', 'detail':'', 'severity':'info'})");
    }

    public void onRowCancel(RowEditEvent<Book> event) {
        PrimeFaces.current().executeScript("PF('msgs').renderMessage({'summary':'Edição cancelada', 'detail':'', 'severity':'warn'})");
    }

    public void onAddNew() {
        selectedBook = new Book();
        books.add(selectedBook);
        
        // Adiciona uma mensagem de sucesso
        PrimeFaces.current().executeScript("PF('msgs').renderMessage({'summary':'Edite os dados para salvar na base de dados', 'detail':'', 'severity':'info'})");
    }

    public void saveBook() {
        saveOrUpdateBook(selectedBook); 
        selectedBook = new Book();
        books = listAll();

        FacesMessage msg = new FacesMessage("Livro Salvo", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void saveOrUpdateBook(Book book) {
        DAO<Book> bookDAO = new DAO<>(Book.class);
        DAO<Category> categoryDAO = new DAO<>(Category.class);

        if (idCategory != null) {
            book.setCategory(categoryDAO.searchById(idCategory));
        }

        if (book.getId() == null)
            bookDAO.add(book);
        else
            bookDAO.update(book);
    }

    public List<Book> listAll() {
        DAO<Book> bookDAO = new DAO<>(Book.class);
        return bookDAO.listAll();
    }
}