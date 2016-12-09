<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Battlefield</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>

    <div id="header">
        <br/>BATTLEFIELD
        <br/>

        <button id="leaveBattle" type="submit" onclick="leaveBattle()">leave battle (surrender)</button>
        <br/><button id="endTurn" type="submit" onclick="makeTurn()">end turn (do nothing)</button>

        <p id="battleId" hidden="hidden">${battleId}</p>
        <p id="accountId" hidden="hidden">${me.accountId}</p>
        <p id="userName" hidden="hidden">${me.login}</p>
    </div>


    <div id="battlefield">

    </div>


    <div id="chatDiv">
        <br/><br/>
        <h2>Chat</h2>
        <p>
            <input type="text" placeholder="type and press enter to chat" id="message" />
        </p>

        <%-- нужно будет сделать чат с полосой прокрутки --%>
        <p id="chat"></p>

    </div>



    <%-- здесь, наверняка, нужен JS.
    Нажатие на карту в руке делает ее активной
    (по возможности, курсор заменяется картой)
    и далее выбирается цель действия карты.
     после чего информация о ходе игрока сохраняется
     me.usedCard, me.target (?usedCard=...&target=...) --%>

    <%-- также работает JS отсчитывающий время хода (~90 секунд)
    и по их истечении запускает подсчет результатов хода
    makeTurn.do(?usedCard=...&target=...) (или прямо bastte.executeTurn())
    после чего другой скрипт проверяет значение "battle.turnEnded"
    и если да, то обновляет страницу (или ее часть)--%>


    <%-- данные скрипты работают только если расположены в конце --%>

    <script src="jquery/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="scripts/battleHandler.js"></script>
    <script type="text/javascript" src="scripts/websocket.js"></script>

</body>
</html>
