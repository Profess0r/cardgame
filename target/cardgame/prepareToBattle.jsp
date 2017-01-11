<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Prepare to battle</title>
</head>
<body>

    <br/>PREPARE TO BATTLE SCREEN
    <br/>

    <p id="battleId" hidden="hidden">${battleId}</p>
    <p id="accountId" hidden="hidden">${me.accountId}</p>
    <p id="userName" hidden="hidden">${me.login}</p>
    <p id="userLevel" hidden="hidden">${me.level}</p>


    <%-- битву стартует либо создатель (если он вышел нужно передать право старта другому игроку) либо после нажатия всеми "start" ("turnReady") --%>


    <button id="leaveBattleSeparate" type="submit" onclick="leaveBattle()">leave battle</button>

    <br/><button id="startButton" type="submit" onclick="startBattle()" disabled>start battle</button>



<%-- на этой странице должен быть JS обновляющий состав участников битвы и отлавливающий момент старта,
    если битва установлена в состояние "началась", то перенаправляем текущего игрока на battlefield.jsp --%>


    <br/><br/>
    <h2>Players</h2>
    <div>
        <%-- для отображения начальных значений попробовать поставить здесь тот же код --%>
            <ul id="playerList">
                <c:forEach var="player" items="${battles[battleId].playerList}">
                    <li id="player ${player.accountId}">

                            <%--<br/>${player.image}--%>
                        name: ${player.login}, level: ${player.level}
                        <%--<br/>ready: ${player.readyForBattle}--%>
                    </li>
                </c:forEach>
            </ul>
    </div>

    <br/><br/>
    <h2>Chat</h2>
    <div>
        <p>
            <input type="text" placeholder="type and press enter to chat" id="message" />
        </p>

        <%-- нужно будет сделать чат с полосой прокрутки --%>
        <p>
            <textarea id="chat" cols="100" rows="10" readonly></textarea>
        </p>

    </div>


    <script src="jquery/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="scripts/prebattleHandler.js"></script>
    <script type="text/javascript" src="scripts/websocket.js"></script>


</body>
</html>
