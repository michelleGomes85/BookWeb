package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.model.Book;
import br.tsi.daw.model.Category;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ViewScoped
@Named("bookMB")
public class BookMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private Book book = new Book(); 
    private List<Book> listBooks = new ArrayList<Book>();
    
    private Long idCategory;
    
    @Inject
    private CategoryMB categoryMB;
    
    @PostConstruct
    public void init() {
        listBooks = listAll();
    }
    
    public void addBook() {
        DAO<Book> bookDAO = new DAO<>(Book.class);
        DAO<Category> categoryDAO = new DAO<>(Category.class);
        book.setCategory(categoryDAO.searchById(idCategory));
        
        if (book.getId() == null)
            bookDAO.add(book);
        else
            bookDAO.update(book);
        
        book = new Book(); // Reset the book object after adding/updating
        listBooks = listAll(); // Refresh the list
    }
    
    public List<Book> listAll() {
        DAO<Book> bookDAO = new DAO<>(Book.class);
        this.listBooks = bookDAO.listAll();
        return this.listBooks;
    }
    
    public void cancel() {
        book = new Book();
    }
    
    // Getters and Setters
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Book> getListBooks() {
        return listBooks;
    }

    public void setListBooks(List<Book> listBooks) {
        this.listBooks = listBooks;
    }

    public CategoryMB getCategoryMB() {
        return categoryMB;
    }

    public void setCategoryMB(CategoryMB categoryMB) {
        this.categoryMB = categoryMB;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
}