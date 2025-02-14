package br.tsi.daw.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    private static final List<String> paginasProtegidas = Arrays.asList(
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

        // Verifica se a página acessada está na lista de páginas protegidas
        boolean isPaginaProtegida = paginasProtegidas.stream().anyMatch(requestURI::endsWith);

        // Se a página não for protegida, permite o acesso
        if (!isPaginaProtegida) {
            chain.doFilter(request, response);
            return;
        }

        // Verifica se o usuário está logado
        boolean logado = (session != null && session.getAttribute("userLogin") != null);

        if (!logado) {
            res.sendRedirect(req.getContextPath() + "/pages/login.xhtml");
            return;
        }

        chain.doFilter(request, response);
    }
}
