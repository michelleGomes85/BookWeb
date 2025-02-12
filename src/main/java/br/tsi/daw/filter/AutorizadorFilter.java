package br.tsi.daw.filter;

import java.io.IOException;

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

@WebFilter("*.xhtml") // Filtra todas as páginas JSF
public class AutorizadorFilter extends HttpFilter implements Filter {

    private static final long serialVersionUID = 1L;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        String loginURL = req.getContextPath() + "/login.xhtml";
        String requestURI = req.getRequestURI();

        boolean logado = (session != null && session.getAttribute("userLogin") != null);
        boolean isLoginPage = requestURI.equals(loginURL);
        
        // Redireciona para o login se não estiver autenticado
        if (!logado && !isLoginPage) {
            res.sendRedirect(loginURL); 
            return;
        }
        
        // Continua a requisição normalmente	
        chain.doFilter(request, response); 
    }
}