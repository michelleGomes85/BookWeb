package br.tsi.daw.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import br.tsi.daw.model.User; 
import br.tsi.daw.enuns.Profile;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("*.xhtml")
public class AutorizadorFilter extends HttpFilter implements Filter {

    private static final long serialVersionUID = 1L;

    // Lista de páginas que precisam de autenticação
    private static final List<String> PROTECTED_PAGES = Arrays.asList(
            "/books.xhtml",
            "/categories.xhtml",
            "/employee.xhtml"
    );

    // Páginas permitidas para o perfil FUNC
    private static final List<String> FUNC_ALLOWED_PAGES = Arrays.asList(
            "/books.xhtml",
            "/categories.xhtml"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String requestURI = req.getRequestURI();

        boolean isPageProtected = PROTECTED_PAGES.stream().anyMatch(requestURI::endsWith);

        if (!isPageProtected) {
            chain.doFilter(request, response);
            return;
        }

        boolean logged = (session != null && session.getAttribute("userLogin") != null);

        if (!logged) {
            res.sendRedirect(req.getContextPath() + "/pages/login.xhtml");
            return;
        }

        User userLogin = (User) session.getAttribute("userLogin");

        // Verifica as permissões com base no perfil do usuário
        if (userLogin != null) {
        	
            Profile userProfile = userLogin.getProfile();

            switch (userProfile) {
                case ADMIN:
                    chain.doFilter(request, response);
                    break;

                case FUNC:
                    if (FUNC_ALLOWED_PAGES.stream().anyMatch(requestURI::endsWith))
                        chain.doFilter(request, response);
                    else 
                    	res.sendRedirect(req.getContextPath() + "/pages/login.xhtml");
                    break;
                case USER:
                	res.sendRedirect(req.getContextPath() + "/pages/login.xhtml");
                    break;
            }
        } else {
        	res.sendRedirect(req.getContextPath() + "/pages/login.xhtml");
        }
    }
}