<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="playerInfo">
    <p class="target" id="me">
        <img src="${pageContext.request.contextPath}/images/cards/player.png" alt="this is player image">
        <%--<img src="${pageContext.request.contextPath}/images/cards/${me.login}.png">--%>
    </p>
    <br/>name: ${me.login}
    <br/>level: ${me.level}
    <br/>health: <span id="playerHealth">${me.currentHealth}</span> / ${me.maximumHealth}

    <br/>defend card:
    <p class="target" id="def">
        <c:choose>
            <c:when test="${me.defenceCard != null}">
                <img src="${pageContext.request.contextPath}/images/cards/${me.defenceCard.name}.png">
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/images/cards/emptyCard.png">
            </c:otherwise>
        </c:choose>
    </p>
    <br/>${me.defenceCard.name}
    <br/>rank: ${me.defenceCard.rank}
    <br/>health: ${me.defenceCard.currentHealth}/${me.defenceCard.maxHealth}
    <br/>power: ${me.defenceCard.power}
    <br/>defence: ${me.defenceCard.defence}
    <br/>${me.defenceCard.description}
</div>


<div class="cards" id="cardsInHand">
    <br/><br/>cards in hand:
    <ul>
        <c:forEach var="card" items="${me.cardsInHand}" varStatus="counter">
            <li>
                <br/>
                <p class="cardInHand target" id="card ${counter.index}">
                    <img src="${pageContext.request.contextPath}/images/cards/${card.name}.png">
                </p>
                <br/>name: ${card.name}
                <br/>rank: ${card.rank}
                <br/>health: ${card.currentHealth}/${card.maxHealth}
                <br/>power: ${card.power}
                <br/>defence: ${card.defence}
                <br/>description: ${card.description}
            </li>
        </c:forEach>
    </ul>
</div>


<div id="stockCards">
    <br/>Stock
    <p>
        <img src="${pageContext.request.contextPath}/images/cards/cover.png">
    </p>
    <br/>cards in stock: ${me.cardsInStock.size()}
</div>


<div id="dropCards">
    <br/>Drop
    <p class="target" id="drop">
        <img src="${pageContext.request.contextPath}/images/cards/cover.png">
    </p>
    <br/>cards in drop: ${me.cardsInDrop.size()}
</div>


<div class="players">
    <p></p>
    <br/><br/>
    <h2>Players</h2>
    <ul>
        <c:forEach var="player" items="${battles[battleId].playerList}" varStatus="counter">
            <c:if test="${player.accountId != me.accountId}">
                <li>
                    <br/>
                    <p class="target" id="player ${counter.index}">
                        <img src="${pageContext.request.contextPath}/images/cards/player.png" alt="this is player image">
                            <%--<img src="${pageContext.request.contextPath}/images/cards/${player.login}.png">--%>
                    </p>
                    <br/>name: ${player.login}
                    <br/>level: ${player.level}
                    <br/>health: ${player.currentHealth} / ${player.maximumHealth}
                    <br/>defend card:
                    <br/>
                    <c:choose>
                        <c:when test="${player.defenceCard != null}">
                            <img src="${pageContext.request.contextPath}/images/cards/${player.defenceCard.name}.png">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/images/cards/emptyCard.png">
                        </c:otherwise>
                    </c:choose>
                    <br/>${player.defenceCard.name}
                    <br/>rank: ${player.defenceCard.rank}
                    <br/>health: ${player.defenceCard.currentHealth}/${player.defenceCard.maxHealth}
                    <br/>power: ${player.defenceCard.power}
                    <br/>defence: ${player.defenceCard.defence}
                    <br/>${player.defenceCard.description}
                </li>
            </c:if>
        </c:forEach>
    </ul>
</div>
