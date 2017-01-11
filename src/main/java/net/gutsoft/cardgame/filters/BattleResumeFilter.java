package net.gutsoft.cardgame.filters;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Battle;
import net.gutsoft.cardgame.entity.Player;
import net.gutsoft.cardgame.util.ServletContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class BattleResumeFilter extends BaseFilter implements Filter {

    public void doHttpFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        if (req.getSession().getAttribute("battleId") != null) {
            int accountId = ((Account) req.getSession().getAttribute("account")).getId();
            Map<Integer, Battle> battleMap = (Map<Integer, Battle>) ServletContextHolder.getServletContext().getAttribute("battles");
            Battle battle = battleMap.get(req.getSession().getAttribute("battleId"));

            if (battle != null) {
                boolean playerInBattle = false;
                for (Player player: battle.getPlayerList()) {
                    if (player.getAccountId() == accountId) {
                        playerInBattle = true;
                        break;
                    }
                }

                if (playerInBattle && battle.isStarted() && !req.getRequestURI().contains("battlefield")) {
                    req.getRequestDispatcher("battlefield.jsp").forward(req, resp);
//                    chain.doFilter(req, resp);
                } else {
                    chain.doFilter(req, resp);
                }
            } else {
                req.getSession().removeAttribute("battleId");
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }


    }
}
