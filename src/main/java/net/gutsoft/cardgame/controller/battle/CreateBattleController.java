package net.gutsoft.cardgame.controller.battle;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Battle;
import net.gutsoft.cardgame.entity.Player;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateBattleController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // все это еще надо будет завернуть в try !!!

        Player player = new Player((Account) req.getSession().getAttribute("account"));

        if (player.getDeck() == null) {
            resp.sendRedirect("arena.jsp");
            return;
        }

        String name = req.getParameter("name");
        if (name.equals("")) {
            name = "battle";
        }
        int maxPlayers = Integer.parseInt(req.getParameter("maxPlayers"));
        int maxLevel = Integer.parseInt(req.getParameter("maxLevel"));

        Battle battle = new Battle();
        battle.setName(name);
        battle.setMaxPlayers(maxPlayers);
        battle.setMaxLevel(maxLevel);
        battle.addPlayer(player);

        Map<Integer, Battle> battleMap = (Map<Integer, Battle>) getServletContext().getAttribute("battles");

        // поиск первого свободного id
        // стоит подумать о многопоточности, вероятно, нужно вынести в отдельный synchronized-метод
        // или сделать весь метод synchronized
        int battleId;
        int i = 0;
        while (battleMap.containsKey(i)) {
            i++;
        }
        battleId = i;

        battle.setId(battleId);

        battleMap.put(battleId, battle);

        getServletContext().setAttribute("battles", battleMap);
        req.getSession().setAttribute("me", player);
        req.getSession().setAttribute("battleId", battleId);
        resp.sendRedirect("prepareToBattle.jsp");
//        req.getRequestDispatcher("prepareToBattle.jsp").forward(req, resp);

    }
}
