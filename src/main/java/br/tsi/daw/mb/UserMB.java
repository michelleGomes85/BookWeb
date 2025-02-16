package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.dao.OrderDAO;
import br.tsi.daw.dao.UserDAO;
import br.tsi.daw.enuns.Profile;
import br.tsi.daw.model.Client;
import br.tsi.daw.model.Order;
import br.tsi.daw.model.User;
import br.tsi.daw.utils.EmailTemplateUtils;
import br.tsi.daw.utils.SendEmailUtils;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * 
 */
@SessionScoped
@Named("userMB")
public class UserMB implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;
    private User userSession;
    private boolean admin;
    
    private List<Order> orders = new ArrayList<>();
    
    @Inject
    private NavigationMB navigationMB;

    public UserMB() {
        this.user = new User();
        this.user.setClient(new Client());
    }

    public String addUser() {
    	
        DAO<User> userDao = new DAO<>(User.class);

        String sessionToken = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionToken");

        if (user.getId() == null) {
        	
            String token = UUID.randomUUID().toString();
            user.setConfirmationToken(token);

            user.setProfile((admin) ? Profile.FUNC : Profile.USER);
            userDao.add(user);

            String confirmLink = "http://localhost:8080/LibraryVirtual/pages/confirm.xhtml?token=" + token + "&sessionToken=" + sessionToken;

            StringBuilder emailContent = EmailTemplateUtils.getConfirmationEmail(user.getLogin(), user.getProfile().getDescription(), confirmLink);

            SendEmailUtils.sendEmail(user.getClient().getEmail(), "Confirmação de Cadastro", emailContent);

            this.user = new User();
            this.user.setClient(new Client());
        } else {
            userDao.update(user);
        }

        return navigationMB.toLogin();
    }
    
    private String redirectToLibrary() {
    	return navigationMB.toLibrary();
    }

    public String login() {
    	
    	UserDAO userDAO = new UserDAO();
        User loggedUser = userDAO.findByLoginAndPassword(user);

        if (loggedUser != null && loggedUser.isActivate()) {
        	
            userSession = loggedUser;
            user = loggedUser;
            
            if (user.getClient() == null)
                user.setClient(new Client());
            
            admin = (loggedUser.getProfile() != Profile.ADMIN) ? false : true; 
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userLogin", userSession);
            
            String redirectTo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("redirectTo");
            if (redirectTo != null && redirectTo.equals("cart"))
                return navigationMB.toCart();
            
            return redirectToLibrary();
        } else {
            user = new User();
            return navigationMB.toLogin();
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
        
        return navigationMB.toLogin();
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
		return navigationMB.toUserRegistration();
	}
	
	public List<Order> getOrders() {
		if (userSession != null && userSession.getClient() != null)
			this.orders = new OrderDAO().findOrderByUser(userSession);
		
		return this.orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}