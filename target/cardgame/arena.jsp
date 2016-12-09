<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Arena</title>
</head>
<body>
    <br/>ARENA
    <br/>

    <%-- здесь также должен быть скрипт обновляющий список доступных битв --%>

    <%-- при создании или присоединении к битве (матчу) нужно проверянь наличие активной колоды --%>
    <br/><a href="./createBattle.do">create battle</a>
    <br/><a href="./main.jsp">back</a>

    <br/><br/>
    <h2>Battles</h2>
    <ul>
        <c:forEach var="battle" items="${battles}">
            <li>
                <br/>
                <%--<br/>name: ${battle.name}--%>
                <%-- ограничения на участие (уровень) --%>
                <br/>name: battle - ${battle.value.id}
                <br/>players: ${battle.value.playerList.size()}/10
                <br/><a href="./joinBattle.do?battleId=${battle.value.id}">join battle</a>
            </li>
        </c:forEach>
    </ul>

</body>
</html>
