<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>
</head>
<body>
    <br/>MAIN PAGE
    <br/>

    <p><textarea id="help" cols="150" rows="6" disabled>
        Это главная страница.
    Отсюда можно разлогиниться, а также перейти в:
    1.Home - для манипуляций над своим аккаунтом и имуществом (на данный момент это карты)
    2.Arena - для проведения сражений
    3.Shop - для покупки карт
    </textarea></p>

    <br/>Name: ${account.login} <a href="logout.do">logout</a>
    <br/>
    <br/><a href="./home.jsp">Home</a>
    <br/><a href="./arena.jsp">Arena</a>
    <br/><a href="./shop.jsp">Shop</a>


</body>
</html>
