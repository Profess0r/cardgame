<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My decks</title>
</head>
<body>
    <br/>MY DECKS
    <br/>

    <p><textarea id="help" cols="150" rows="6" disabled>
        На этой странице представлены имеющиеся у Вас колоды.
    Чтобы создать новую колоду нажмите на "Create new deck".
    Чтобы редактировать колоду нажмите на название соответствующей колоды.
    Чтобы удалить колоду нажмите на "Delete deck" под названием колоды, которую хотите удалить.
    </textarea></p>

    <br/><a href="./home.jsp">back</a>

    <br/><a href="createNewDeck.do">Create new deck</a>

    <br/><br/>
    <h2>Decks</h2>
    <ul>
        <c:forEach var="deck" items="${account.deckList}">
            <li>
                <%-- deckDetails можно(нужно) сделать на этой же странице с боку(или снизу) --%>
                <br/>name: <a href="manageDeck.do?deckId=${deck.id}">${deck.name}</a>
                <br/>activeness: ${deck.active}
                <br/><a href="./deleteDeck.do?deckId=${deck.id}">Delete deck</a>
            </li>
        </c:forEach>
    </ul>

</body>
</html>
