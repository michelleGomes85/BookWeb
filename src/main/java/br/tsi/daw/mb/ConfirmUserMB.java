package br.tsi.daw.mb;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.dao.UserDAO;
import br.tsi.daw.model.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named
@RequestScoped
public class ConfirmUserMB {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String confirmUser() {
    	
        if (token == null || token.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Token inválido!", "O token de confirmação é inválido."));
            return "login?faces-redirect=true";
        }

        UserDAO userDao = new UserDAO();
        
        User user = userDao.findByField("confirmationToken", token);

        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Token inválido!", "Nenhum usuário encontrado com esse token."));
            
            return "login?faces-redirect=true";
        }
        
        DAO<User> userGeneric = new DAO<>(User.class);

        // Confirmar o cadastro do usuário
        user.setActivate(true);
        userGeneric.update(user);

        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Conta ativada!", "Sua conta foi ativada com sucesso! Faça login."));
        
        return "login?faces-redirect=true";
    }
}
