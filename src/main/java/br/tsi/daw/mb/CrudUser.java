package br.tsi.daw.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.primefaces.PrimeFaces;

import br.tsi.daw.dao.DAO;
import br.tsi.daw.enuns.Profile;
import br.tsi.daw.model.Client;
import br.tsi.daw.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CrudUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<User> users;

    private User selectedUser;

    private List<User> selectedUsers;

    private DAO<User> userService;

    @PostConstruct
    public void init() {
        this.userService = new DAO<>(User.class);
        List<User> allUsers = this.userService.listAll();
        this.selectedUsers = new ArrayList<>();
        
        this.users = allUsers.stream()
                .filter(user -> !Profile.ADMIN.equals(user.getProfile()))
                .collect(Collectors.toList());
    }

    public List<User> getUsers() {
        return users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUser) {
        this.selectedUsers = selectedUser;
    }

    public void openNew() {
        this.selectedUser = new User();
        this.selectedUser.setClient(new Client());
    }

    public void saveUser() {
        if (this.selectedUser.getId() == null) {
        	this.selectedUser.setProfile(Profile.FUNC);
        	this.selectedUser.setActivate(true);
            this.userService.add(this.selectedUser);
            this.users.add(this.selectedUser); 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário adicionado"));
        } else {
            this.userService.update(this.selectedUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário atualizado"));
        }

        PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
    }

    public void deleteUser() {
    	
        this.userService.remove(this.selectedUser);

        this.users.remove(this.selectedUser);
        this.selectedUsers.remove(this.selectedUser);
        this.selectedUser = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário removido"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedUsers()) {
            int size = this.selectedUsers.size();
            return size > 1 ? size + " usuário selecionado" : "1 usuário selecionado";
        }

        return "Delete";
    }

    public boolean hasSelectedUsers() {
        return this.selectedUsers != null && !this.selectedUsers.isEmpty();
    }

    public void deleteSelectedUsers() {
    	
        for (User user : selectedUsers) {
            this.userService.remove(user);
        }

        this.users.removeAll(this.selectedUsers);
        this.selectedUsers = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuários removidos"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
        PrimeFaces.current().executeScript("PF('dtFuncionarios').clearFilters()");
    }
}