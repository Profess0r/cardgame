package net.gutsoft.cardgame.controller.battle;

import net.gutsoft.cardgame.entity.Battle;
import net.gutsoft.cardgame.entity.Card;
import net.gutsoft.cardgame.entity.Player;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MakeTurnController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // все это еще надо будет завернуть в try !!!

        Map<Integer, Battle> battleMap = (Map<Integer, Battle>) getServletContext().getAttribute("battles");
        int battleId = (int) req.getSession().getAttribute("battleId");
        Battle battle = battleMap.get(battleId);

        battle.setTurnEnded(false);

        // !!! все это будет работать только при условии, что "me" и  battle.playerList.player ссылаются на один обьект !!!

        // получить игрока
        Player me = (Player) req.getSession().getAttribute("me");

        // получить данные о ходе игрока

        int usedCardIndex = -1;
        String target;

        // если карта не определена - ход "do nothing"
        if (req.getParameter("card").split(" ")[0].equals("undefined")) {
            System.out.println("card undefined");
            target = null;
        } else {
            // также, если для хода указана недопустимая цель - "do nothing"
            // возможно эту проверку лучше делать не здесь
            // другие варианты: на странице (JS), при выполнении хода
            usedCardIndex = Integer.parseInt(req.getParameter("card").split(" ")[1]);
            Card usedCard = me.getCardsInHand().get(usedCardIndex);
            target = req.getParameter("target");

            if (!usedCard.isTargetAvailable(target.split(" ")[0])) {
                System.out.println("target not available for this card");
                target = null;
            }
        }


        // записать ход игрока
        me.setUsedCardIndex(usedCardIndex);
        me.setTarget(target);
        System.out.println(me.getLogin() + " card: " + usedCardIndex);
        System.out.println(me.getLogin() + " target: " + target);


        // отмечаем игрока как походившего
        me.setTurnReady(true);
        System.out.println(me.getLogin() + " - turn ready");


        // часть идущих ниже проверок и действий можно (и скорее даже нужно) вынести вметоды обьекта Battle

        // проверяем все ли игроки похдили
        for (Player player: battle.getPlayerList()) {
            if(!player.isTurnReady()) {
                // если нашолся хоть один неготовый, то отправляем текущего игрока на страницу waitTurnEnd.jsp
                resp.sendRedirect("waitTurnEnd.jsp");
                return;
            }
        }

        // если все игроки походили, то выполняем просчет
        System.out.println("Execute turn...");
        battle.executeTurn();

        // имеет смысл исключять из списка, начислять положенные деньги? и опыт? и отпускать игрока (отправлять на страницу арены)
        for (Player player: battle.getPlayerList()) {
            if (player.isDefeated()) {
                battle.getPlayerList().remove(player);
            }
        }

        // если игрок остался один, то конец битвы
        if (battle.getPlayerList().size() < 2) {
            battleMap.remove(battleId, battle);
            req.getSession().removeAttribute("battleId");
            req.getRequestDispatcher("arena.jsp").forward(req, resp);
            return;
        }

        // сдать карты каждому игроку
        for (Player player: battle.getPlayerList()) {
            player.dealCards();
        }

        battle.setTurnEnded(true);
        System.out.println("turn ended - true");

        req.getRequestDispatcher("battlefield.jsp").forward(req, resp);

    }
}
