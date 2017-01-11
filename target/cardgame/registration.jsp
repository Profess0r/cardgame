<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <br/>REGISTER NEW USER

    <br/><a href="./index.jsp">back</a>

    <form action="register.do" method="post" enctype="application/x-www-form-urlencoded">

        <label for="login">Login:</label>
        <br><input name="login" id="login" type="text" value="${login}"/>
        ${errorMap.login}

        <br><label for="password">Password:</label>
        <br><input name="password" id="password" type="password" value="${password}"/>
        ${errorMap.password}

        <br><label for="confirmPassword">Confirm password:</label>
        <br><input name="confirmPassword" id="confirmPassword" type="password" value="${password}"/>

        <br><label for="email">Email:</label>
        <br><input name="email" id="email" type="text" value="${email}"/>
        ${errorMap.email}

        <br><input type="submit" value="register"/>
    </form>
</body>
</html>
