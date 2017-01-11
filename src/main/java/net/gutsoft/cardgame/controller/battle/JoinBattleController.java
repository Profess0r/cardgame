package net.gutsoft.cardgame.controller.battle;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Battle;
import net.gutsoft.cardgame.entity.Player;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JoinBattleController extends DependencyInjectionServlet {

    // TODO: 14.10.2016 проверка на требования к участникам

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // все это еще надо будет завернуть в try !!!

        Player player = new Player((Account) req.getSession().getAttribute("account"));

        Map<Integer, Battle> battleMap = (Map<Integer, Battle>) getServletContext().getAttribute("battles");
        int battleId = Integer.parseInt(req.getParameter("battleId"));
        Battle battle = battleMap.get(battleId);

        // потом нужно сделать это через errorMap
        if (player.getDeck() == null) {
            resp.sendRedirect("arena.jsp");
            return;
        }
        if (battle.isStarted()) {
            resp.sendRedirect("arena.jsp");
            return;
        }
        if (battle.getPlayerList().size() > (battle.getMaxPlayers() - 1)) {
            resp.sendRedirect("arena.jsp");
            return;
        }
        if (player.getLevel() > battle.getMaxLevel()) {
            resp.sendRedirect("arena.jsp");
            return;
        }

        battle.addPlayer(player);

        // !!! не будет ли проблем с одновременным доступом к battleMap и battle и их изменением в ServletContext???
        battleMap.put(battleId, battle);

        getServletContext().setAttribute("battles", battleMap);
        req.getSession().setAttribute("me", player);
        req.getSession().setAttribute("battleId", battleId);
        resp.sendRedirect("prepareToBattle.jsp");
//        req.getRequestDispatcher("prepareToBattle.jsp").forward(req, resp);
    }
}
