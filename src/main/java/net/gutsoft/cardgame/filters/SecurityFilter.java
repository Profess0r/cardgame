package net.gutsoft.cardgame.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter extends BaseFilter implements Filter {

    public void doHttpFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        String url = req.getRequestURI();

        // !!! при любом запросе добавляется большое количество проверок,
        // !!! что существенно снижает производительность вцелом и время отклика вчастности

        if (req.getSession() != null && req.getSession().getAttribute("account") != null) {
            chain.doFilter(req, resp);
        } else if (url.equals("cardgame/") || url.equals("/") || url.contains("index") || url.contains("register") || url.contains("login")) {
            chain.doFilter(req, resp);
        } else {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
