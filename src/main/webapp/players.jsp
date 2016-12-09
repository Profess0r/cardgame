<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <ul>
        <c:forEach var="player" items="${battles[battleId].playerList}">
            <li>
                <br/>
                    <%--<br/>${player.image}--%>
                <br/>name: ${player.login}
                <br/>level: ${player.level}
                <br/>ready: ${player.readyForBattle}
            </li>
        </c:forEach>
    </ul>

