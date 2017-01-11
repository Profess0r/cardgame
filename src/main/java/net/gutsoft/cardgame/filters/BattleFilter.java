package net.gutsoft.cardgame.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BattleFilter extends BaseFilter implements Filter {

    public void doHttpFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        if (req.getSession().getAttribute("me") == null || req.getSession().getAttribute("battleId") == null) {
            resp.sendRedirect("arena.jsp");
        } else {
            chain.doFilter(req, resp);
        }

        // чтобы предотвратить перескакивание с одной битвы в другую
        // неплохо бы еще делать проверку принадлежности игрока к той битве,
        // к которой он собирается подключиться

    }
}
