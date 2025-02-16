package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.model.Book;
import br.tsi.daw.model.Category;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CrudBook implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private List<Book> books;

    private Book selectedBook;

    private List<Book> selectedBooks;

    private DAO<Book> bookService;
    
    private List<Category> categories;
    private DAO<Category> categoryService;

    @PostConstruct
    public void init() {
        this.bookService = new DAO<>(Book.class);
        this.books = this.bookService.listAll();
        this.selectedBooks = new ArrayList<>();
        
        this.categoryService = new DAO<>(Category.class);
        this.categories = this.categoryService.listAll();
        this.selectedBooks = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    public List<Book> getSelectedBooks() {
        return selectedBooks;
    }

    public void setSelectedBooks(List<Book> selectedBook) {
        this.selectedBooks = selectedBook;
    }

    public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void openNew() {
        this.selectedBook = new Book();
        this.selectedBook.setCategory(new Category());
    }

	public void saveBook() {
	    if (this.selectedBook.getId() == null) {
	    	
	        Category selectedCategory = categoryService.searchById(this.selectedBook.getCategory().getId());
	        this.selectedBook.setCategory(selectedCategory);

	        this.bookService.add(this.selectedBook);
	        this.books.add(this.selectedBook); 
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Livro adicionado"));
	    } else {
	        Category selectedCategory = categoryService.searchById(this.selectedBook.getCategory().getId());
	        this.selectedBook.setCategory(selectedCategory);

	        this.bookService.update(this.selectedBook);
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Livro atualizado"));
	    }

	    this.categories = this.categoryService.listAll();

	    PrimeFaces.current().executeScript("PF('manageBookDialog').hide()");
	    PrimeFaces.current().ajax().update("form:messages", "form:dt-books", "form:dialogs:manage-book-content");
	}

    public void deleteBook() {
    	
        this.bookService.remove(this.selectedBook);

        this.books.remove(this.selectedBook);
        this.selectedBooks.remove(this.selectedBook);
        this.selectedBook = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Livro removido"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-books");
    }

    public String getDeleteButtonMessage() {
    	
        if (hasSelectedBooks()) {
            int size = this.selectedBooks.size();
            return size > 1 ? size + " livro selecionado" : "1 livro selecionado";
        }

        return "Delete";
    }

    public boolean hasSelectedBooks() {
        return this.selectedBooks != null && !this.selectedBooks.isEmpty();
    }

    public void deleteSelectedBooks() {
    	
        for (Book Book : selectedBooks)
            this.bookService.remove(Book);

        this.books.removeAll(this.selectedBooks);
        this.selectedBooks = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Livros removidos"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-books");
        PrimeFaces.current().executeScript("PF('dtLivros').clearFilters()");
    }
}
