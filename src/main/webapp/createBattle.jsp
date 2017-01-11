<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create battle</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
    <br/>CREATE BATTLE
    <br/>

    <br/><a href="arena.jsp">back</a>

    <form action="createBattle.do" method="get" enctype="application/x-www-form-urlencoded">

        <label for="name">Battle name: </label>
        <br><input name="name" id="name" type="text" value="battle"/>

        <br><label for="maxPlayers">max players (2-10): </label>
        <br><input name="maxPlayers" id="maxPlayers" type="number" value="2"/>

        <br><label for="maxLevel">max level: </label>
        <br><input name="maxLevel" id="maxLevel" type="number" value="1"/>

        <br><input type="submit" value="Create"/>

    </form>

</body>
</html>
