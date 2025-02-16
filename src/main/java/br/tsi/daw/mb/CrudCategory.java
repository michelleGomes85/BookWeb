package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.model.Category;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CrudCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Category> categories;

    private Category selectedCategory;

    private List<Category> selectedCategories;

    private DAO<Category> categoryService;

    @PostConstruct
    public void init() {
        this.categoryService = new DAO<>(Category.class);
        this.categories = this.categoryService.listAll();
        this.selectedCategories = new ArrayList<>();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public void openNew() {
        this.selectedCategory = new Category();
    }

    public void saveCategory() {
        if (this.selectedCategory.getId() == null) {
            this.categoryService.add(this.selectedCategory);
            this.categories.add(this.selectedCategory);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categoria adicionada"));
        } else {
            this.categoryService.update(this.selectedCategory);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categoria atualizada"));
        }

        PrimeFaces.current().executeScript("PF('manageCategoryDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-categories", "form:dialogs:manage-category-content");
    }

    public void deleteCategory() {
        this.categoryService.remove(this.selectedCategory);

        this.categories.remove(this.selectedCategory);
        this.selectedCategories.remove(this.selectedCategory);
        this.selectedCategory = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categoria removida"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-categories", "form:delete-categories-button");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedCategories()) {
            int size = this.selectedCategories.size();
            return size > 1 ? size + " categorias selecionadas" : "1 categoria selecionada";
        }

        return "Delete";
    }

    public boolean hasSelectedCategories() {
        return this.selectedCategories != null && !this.selectedCategories.isEmpty();
    }

    public void deleteSelectedCategories() {
        for (Category category : selectedCategories) {
            this.categoryService.remove(category);
        }

        this.categories.removeAll(this.selectedCategories);
        this.selectedCategories = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categorias removidas"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-categories", "form:delete-categories-button");
        PrimeFaces.current().executeScript("PF('dtCategories').clearFilters()");
    }
}