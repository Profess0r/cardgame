<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manage deck</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
    <br/>MANAGE DECK
    <br/>

    <p><textarea id="help" cols="150" rows="6" disabled>
        Здесь можно добавлять и удалять карты из колоды соответствующими кнопками.
    Также можно поменять название колоды и сделать ее активной.
    Активная колода - это та которая используется в сражении.
    Активной может быть лишь одна колода.
    </textarea></p>

    <%-- remove from session --%>
    <br/><a href="leaveDeckManagement.do">back</a>

    <br/><label for="name">Deck name: </label>
    <br><input id="name" type="text" value="${managedDeck.name}"/>

    <br/><label for="active">Make this deck active</label>
    <input type="checkbox" id="active">

    <br/><button id="saveChanges" onclick="saveChanges()">save changes</button>

    <br/><br/>
    <h2>Cards in deck</h2>
    <div class="cards">
        <ul id="deckCards">
            <c:forEach var="card" items="${managedDeck.cardList}">
                <li class="card ${card.id}">
                    <br/><img src="${pageContext.request.contextPath}/images/cards/${card.name}.png">
                    <br/>name: ${card.name}
                    <br/>rank: ${card.rank}
                    <br/>health: ${card.maxHealth}
                    <br/>power: ${card.power}
                    <br/>defence: ${card.defence}
                    <br/>initiative: ${card.initiative}
                    <br/>description: ${card.description}
                    <br/><button class="addCardButton" onclick="addCard(${card.id})" hidden="hidden">add to deck</button>
                    <button class="removeCardButton" onclick="removeCard(${card.id})">remove from deck</button>
                        <%--<br/><a href="">add modificator to card</a>--%>
                </li>
            </c:forEach>
        </ul>
        <br/>
    </div>


    <div class="cards">
        <h2>Available cards</h2>
        <ul id="availableCards">
            <c:forEach var="card" items="${availableCards}">
                <li class="card ${card.id}">
                    <br/><img src="${pageContext.request.contextPath}/images/cards/${card.name}.png">
                    <br/>name: ${card.name}
                    <br/>rank: ${card.rank}
                    <br/>health: ${card.maxHealth}
                    <br/>power: ${card.power}
                    <br/>defence: ${card.defence}
                    <br/>initiative: ${card.initiative}
                    <br/>description: ${card.description}
                    <br/><button class="addCardButton" onclick="addCard(${card.id})">add to deck</button>
                    <button class="removeCardButton" onclick="removeCard(${card.id})" hidden="hidden">remove from deck</button>
                </li>
            </c:forEach>
        </ul>
    </div>


    <script src="jquery/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="scripts/manageDeck.js"></script>


</body>
</html>
