<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shop</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
    <br/>SHOP
    <br/>

    <br/><a href="./main.jsp">back</a>

    <br/>your money: ${account.money}
    ${errorMap.money}

    <%--
    здесь можно сделать список имеющихся в наличии у игрока карт с возможностью их продажи (по сниженой цене)
    также уместно обновлять не всю страницу, а отправлять запрос JS и обновлять только список карт игрока
     (или же использовать web-socket...)
     --%>

    <br/><br/>
    <h2>Cards</h2>
    <div class="cards">
        <ul>
            <c:forEach var="card" items="${shop.cardList}">
                <li>
                    <br/><img src="${pageContext.request.contextPath}/images/cards/${card.name}.png">
                    <br/>name: ${card.name}
                    <br/>rank: ${card.rank}
                    <br/>health: ${card.maxHealth}
                    <br/>power: ${card.power}
                    <br/>defence: ${card.defence}
                    <br/>description: ${card.description}
                    <br/>price: ${card.price}
                    <br/><br/><a href="./buyCard.do?cardId=${card.id}">buy</a>
                </li>
            </c:forEach>
        </ul>
    </div>

</body>
</html>
