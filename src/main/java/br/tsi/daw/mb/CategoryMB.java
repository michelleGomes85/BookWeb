package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.model.Category;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named("categoryMB")
public class CategoryMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Category category = new Category();
	private List<Category> categories = new ArrayList<>();
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public void addCategory() {
		DAO<Category> categories = new DAO<>(Category.class);
		categories.add(category);
	}
	
	public List<Category> listAll() {
		DAO<Category> categories = new DAO<>(Category.class);
		this.categories = categories.listAll();
		return this.categories;
	}
	
	public void cancel() {
		category = new Category();
	}
}