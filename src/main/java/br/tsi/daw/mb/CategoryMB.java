package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.model.Category;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named("categoryMB")
public class CategoryMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Category selectedCategory;
	
	private List<Category> categories = new ArrayList<>();
	
    @PostConstruct
    public void init() {
    	categories = listAll();
    	selectedCategory = new Category();
    }
		
	public Category getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(Category selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

    public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void onRowEdit(RowEditEvent<Category> event) {
        Category editedCategory = event.getObject();
        saveOrUpdateCategory(editedCategory);
        
        PrimeFaces.current().executeScript("PF('msgs').renderMessage({'summary':'Categoria atualizado', 'detail':'', 'severity':'info'})");
    }

    public void onRowCancel(RowEditEvent<Category> event) {
        PrimeFaces.current().executeScript("PF('msgs').renderMessage({'summary':'Edição cancelada', 'detail':'', 'severity':'warn'})");
    }

    public void onAddNew() {
        selectedCategory = new Category();
        categories.add(selectedCategory);
        
        PrimeFaces.current().executeScript("PF('msgs').renderMessage({'summary':'Edite os dados para salvar na base de dados', 'detail':'', 'severity':'info'})");
    }

    public void saveCategory() {
        saveOrUpdateCategory(selectedCategory); 
        selectedCategory = new Category();
        categories = listAll();

        FacesMessage msg = new FacesMessage("Livro Salvo", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void saveOrUpdateCategory(Category category) {
        DAO<Category> categoryDAO = new DAO<>(Category.class);

        if (category.getId() == null)
        	categoryDAO.add(category);
        else
        	categoryDAO.update(category);
    }

    public List<Category> listAll() {
        DAO<Category> categoryDAO = new DAO<>(Category.class);
        return categoryDAO.listAll();
    }
}