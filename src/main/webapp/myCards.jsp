<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My cards</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
    <br/>MY CARDS
    <br/>

    <br/><a href="./home.jsp">back</a>

    <br/><br/>
    <h2>Cards</h2>
    <div class="cards">
        <ul>
            <c:forEach var="card" items="${account.cardList}">
                <li>
                    <br/><img src="${pageContext.request.contextPath}/images/cards/${card.name}.png">
                    <br/>name: ${card.name}
                    <br/>rank: ${card.rank}
                    <br/>health: ${card.maxHealth}
                    <br/>power: ${card.power}
                    <br/>defence: ${card.defence}
                    <br/>initiative: ${card.initiative}
                    <br/>description: ${card.description}
                </li>
            </c:forEach>
        </ul>
    </div>


</body>
</html>
