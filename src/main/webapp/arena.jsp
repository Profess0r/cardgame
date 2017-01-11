<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Arena</title>
</head>
<body>
    <br/>ARENA
    <br/>

    <p><textarea id="help" cols="150" rows="6" disabled>
        Это арена.
    Здесь можно создать свое сражение ("Create battle")
    либо присоединиться к уже существующему ("Join battle").
    Если не отображается длинный список существующих сражений , то их просто нет.
    </textarea></p>

    <%-- здесь также должен быть скрипт обновляющий список доступных битв --%>

    <%-- при создании или присоединении к битве (матчу) нужно проверянь наличие активной колоды --%>
    <br/><a href="./createBattle.jsp">Create battle</a>
    <br/><a href="./main.jsp">back</a>

    <br/><br/>
    <h2>Battles</h2>
    <ul>
        <c:forEach var="battle" items="${battles}">
            <c:if test="${!battle.value.started}">
                <li>
                    <br/>
                    <br/>name: ${battle.value.name}
                    <br/>players: ${battle.value.playerList.size()}/${battle.value.maxPlayers}
                    <br/>max level: ${battle.value.maxLevel}
                    <br/><button class="joinBattle" onclick="joinBattle(${battle.value.id})">Join battle</button>
                </li>
            </c:if>
        </c:forEach>
    </ul>

    <script type="text/javascript" src="scripts/arena.js"></script>

</body>
</html>
