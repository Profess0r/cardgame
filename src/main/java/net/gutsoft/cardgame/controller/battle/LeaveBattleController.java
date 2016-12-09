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

public class LeaveBattleController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // все это еще надо будет завернуть в try !!!

        // проверка не началась ли битва, предотвращение выхода в период между нажатием "старт" и началом битвы

        int accountId = ((Account) req.getSession().getAttribute("account")).getId();

        Map<Integer, Battle> battleMap = (Map<Integer, Battle>) getServletContext().getAttribute("battles");

        int battleId = (int) req.getSession().getAttribute("battleId");

        Battle battle = battleMap.get(battleId);

        if (battle != null) {
            battle.removePlayer(accountId);
            // если игроков не осталось - удалить битву
            if (battle.getPlayerList().size() == 0) {
                battleMap.remove(battleId);
                getServletContext().setAttribute("battles", battleMap);
            } else {
                battleMap.put(battleId, battle);
                getServletContext().setAttribute("battles", battleMap);
            }
        }

        // !!! не будет ли проблем с одновременным доступом к battleMap и battle и их изменением в ServletContext???

        req.getSession().removeAttribute("battleId");
        req.getRequestDispatcher("arena.jsp").forward(req, resp);

    }
}
