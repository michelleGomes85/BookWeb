package br.tsi.daw.mb;

import java.util.List;
import java.util.Map;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.dao.UserDAO;
import br.tsi.daw.model.ItemOrder;
import br.tsi.daw.model.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class ConfirmUserMB {

    private String token;
    
    @Inject
    private NavigationMB navigationMB;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String confirmUser() {
    	
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String token = params.get("token");
        String sessionToken = params.get("sessionToken");

        if (token == null || token.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Token inválido!", "O token de confirmação é inválido."));
            return navigationMB.toLogin();
        }

        UserDAO userDao = new UserDAO();
        User user = userDao.findByField("confirmationToken", token);

        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Token inválido!", "Nenhum usuário encontrado com esse token."));
            return navigationMB.toLogin();
        }

        DAO<User> userGeneric = new DAO<>(User.class);
        user.setActivate(true);
        userGeneric.update(user);

        // Restaura o carrinho da sessão original, se o sessionToken estiver presente
        if (sessionToken != null && !sessionToken.isEmpty()) {
            restoreCartFromSessionToken(sessionToken);
        }

        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Conta ativada!", "Sua conta foi ativada com sucesso! Faça login."));
        
        return navigationMB.toLogin();
    }
    
    private void restoreCartFromSessionToken(String sessionToken) {
    	
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        String originalSessionToken = (String) sessionMap.get("sessionToken");

        if (sessionToken.equals(originalSessionToken)) {
        	
            @SuppressWarnings("unchecked")
			List<ItemOrder> cart = (List<ItemOrder>) sessionMap.get("cart");
            if (cart != null)
                sessionMap.put("cart", cart);
        }

        sessionMap.remove("sessionToken");
    }
}
