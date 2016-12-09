<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

    <br/>LOGIN

    <br/><a href="./index.jsp">back</a>

    <%-- необходимо предотвратить отображение пароля в url, но метод post здесь вызывает ошибку --%>
    <form action="login.do" method="post" enctype="application/x-www-form-urlencoded">
        <label for="login">Login:</label>
        <br><input name="login" id="login" type="text"/>
        <br><label for="password">Password:</label>
        <br><input name="password" id="password" type="password"/>
        <br><input type="submit" value="login"/>
    </form>
</body>
</html>
