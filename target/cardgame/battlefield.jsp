<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Battlefield</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>

    <div id="main">
        <div id="header">
            <br/>BATTLEFIELD
            <br/>

            <p><textarea id="help" cols="130" rows="10" disabled>
        Это поле боя.
    Сражение происходит следующим образом:
    - У каждого игрока есть полторы минуты (таймер временно не отображается) для того,
    чтобы сделать ход, после чего подсчитываются результаты.
    Результаты хода выводятся в лог сражения.
    - в свой ход игрок может выбрать одну из карт, которая есть у него на руках и указать цель
        (при нажатии на выбранную карту, а также на цель Вы пока не увидите никаких признаков того,
        что игра восприняла Ваш выбор, придется просто поверить).
    Целью может быть:
        1.другая карта на руках (например, для лечения)
        2.сам игрок (также для лечения)
        3.отбой (сбросить карту)
        4.поле для карты защиты (при атаке игрока сначала необходимо уничтожить карту защиты)
        5.другой игрок (атака игрока)
    - если у игрока на руках не осталось карт, он может вытянуть две карты из сброса нажав соответствующую кнопку
    - если игрок не успевает сделать ход в отведенное время либо указывает недопустимую для выбранной карты цель,
        то его ход становится равносильным пропуску хода (равносильно нажатию "end turn (do nothing)")
    - если два игрока указали целью друг друга, то происходит столкновение карт и, если одна из карт повержена,
        то победившая карта продолжает атаку на игрока (или на его карту защиты, если таковая есть), но в половину силы
            </textarea></p>

            <button id="leaveBattle" type="submit" onclick="leaveBattle()">leave battle (surrender)</button>
            <br/><button id="endTurn" type="submit" onclick="makeTurn()">end turn (do nothing)</button>

            <p id="battleId" hidden="hidden">${battleId}</p>
            <p id="accountId" hidden="hidden">${me.accountId}</p>
            <p id="userName" hidden="hidden">${me.login}</p>
        </div>


        <div id="battlefield">
        </div>


        <div id="chatDiv">
            <br/>
            <h2>Chat</h2>
            <p>
                <input type="text" placeholder="type and press enter to chat" id="message" />
            </p>

            <%-- нужно будет сделать чат с полосой прокрутки --%>
            <p>
                <textarea id="chat" cols="100" rows="10" readonly></textarea>
            </p>
        </div>
    </div>


    <div id="battleLog">
        <br/>
        <h2>Battle log</h2>

        <p>
            <textarea id="log" cols="50" rows="50" disabled></textarea>
        </p>
    </div>

    <%-- данные скрипты работают только если расположены в конце --%>

    <script src="jquery/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="scripts/battleHandler.js"></script>
    <script type="text/javascript" src="scripts/websocket.js"></script>

</body>
</html>
