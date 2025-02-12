package br.tsi.daw.mb;

import java.io.Serializable;

import br.tsi.daw.dao.UserDAO;
import br.tsi.daw.model.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@SessionScoped
@Named("userMB")
public class UserMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private User user = new User();

	public String login() {
		
		UserDAO userDao = new UserDAO();
		
		boolean exists = userDao.exists(user);
		
		if (exists) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userLogin", this.user);
			return "library?faces-redirect=true";
			
		}else {
			user = new User();
			return "login?faces-redirect=true";
		}
	}
	
	public boolean isLogged() {
		return user.getLogin() != null;
	}
	
	public String logout() {
		
		user = new User();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userLogin", null);
		return "login?faces-redirect=true";
		
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
