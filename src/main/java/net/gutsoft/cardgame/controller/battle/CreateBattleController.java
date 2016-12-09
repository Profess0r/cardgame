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

        // нужно проверянь наличие активной колоды

        // вероятно, сущьность Player не нужна - видимо все таки нужна
        Battle battle = new Battle();
        Player player = new Player((Account) req.getSession().getAttribute("account"));
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
