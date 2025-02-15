package br.tsi.daw.mb;

import java.io.Serializable;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.dao.UserDAO;
import br.tsi.daw.enuns.Profile;
import br.tsi.daw.model.Client;
import br.tsi.daw.model.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@SessionScoped
@Named("userMB")
public class UserMB implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;
    private User userSession;
    private boolean admin;

    public UserMB() {
        this.user = new User();
        this.user.setClient(new Client());
    }

    public String addUser() {
    	
        DAO<User> userDao = new DAO<>(User.class);

        if (user.getId() == null) {
            user.setProfile((admin) ? Profile.FUNC : Profile.USER);
            userDao.add(user);
        } else
            userDao.update(user);
        
        return redirectToLibrary();
    }
    
    private String redirectToLibrary() {
    	return "library?faces-redirect=true";
    }

    public String login() {
    	
    	UserDAO userDAO = new UserDAO();
        User loggedUser = userDAO.findByLoginAndPassword(user);

        if (loggedUser != null) {
        	
            userSession = loggedUser;
            user = loggedUser;
            
            if (user.getClient() == null)
                user.setClient(new Client());
            
            admin = (loggedUser.getProfile() != Profile.ADMIN) ? false : true; 
            
            // Armazena o usuário logado na sessão
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userLogin", userSession);
            
            // Verifica se há um redirecionamento armazenado na sessão
            String redirectTo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("redirectTo");
            if (redirectTo != null && redirectTo.equals("cart"))
                return "cart?faces-redirect=true";
            
            return redirectToLibrary();
        } else {
            user = new User();
            return "login?faces-redirect=true";
        }
    }

    public boolean isLogged() {
        return userSession != null && userSession.getLogin() != null;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        userSession = new User();
        user = new User();
        return "login?faces-redirect=true";
    }

    public String cancelEdit() {
    	return redirectToLibrary();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUserSession() {
        return userSession;
    }

    public void setUserSession(User userSession) {
        this.userSession = userSession;
    }
    
	public String userData() {
		return "register_user?faces-redirect=true";
	}
}