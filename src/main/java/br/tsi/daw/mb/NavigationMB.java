package br.tsi.daw.mb;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named("navigationMB")
public class NavigationMB {

    public String toLibrary() {
        return "library?faces-redirect=true";
    }

    public String toCart() {
        return "cart?faces-redirect=true";
    }

    public String toLogin() {
        return "login?faces-redirect=true";
    }

    public String toUserRegistration() {
        return "register_user?faces-redirect=true";
    }
    
    public String toCategories() {
    	return "categories?faces-redirect=true";
    }
    
    public String toBooks() {
    	return "books?faces-redirect=true";
    }
    
    public String toEmployee() {
    	return "employee?faces-redirect=true";
    }
}