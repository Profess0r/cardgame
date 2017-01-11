<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account</title>
    <link rel="stylesheet" href="styles/account.css"/>
</head>
<body>
    <br/>
    <br/>ACCOUNT

    <p><textarea id="help" cols="150" rows="3" disabled>
        Это информация о Вашем аккаунте.
    На данный момент единственное, что можно изменить - это картинка-аватарка.
    </textarea></p>

    <br/><a href="home.jsp">back</a>

    <div>
        <p id="avatar">
            <img src="${pageContext.request.contextPath}/images/avatars/${account.avatar}.png" alt="this is player image">
        </p>

        <br/><button id="showImages" onclick="showImages()">Change image</button>

        <br/>
        <div id="avatarImages" hidden>
        </div>

        <div id="info">
            <br/>Name: ${account.login}
            <br/>Email: ${account.email}

            <%--<br/>--%>
            <%--<br/>Change password--%>
            <%--<br><label for="oldPassword">Old password:</label>--%>
            <%--<br><input name="oldPassword" id="oldPassword" type="password" value="${password}"/>--%>
            <%--${errorMap.oldPassword}--%>

            <%--<br><label for="newPassword">New password:</label>--%>
            <%--<br><input name="newPassword" id="newPassword" type="password" value="${password}"/>--%>
            <%--${errorMap.newPassword}--%>

            <%--<br><label for="confirmPassword">Confirm new password:</label>--%>
            <%--<br><input name="confirmPassword" id="confirmPassword" type="password" value="${password}"/>--%>
            <%--<br/>${errorMap.confirmPassword}--%>
            <%--<button id="changePassword" onclick="changePassword()">Change password</button>--%>


            <br/>
            <br/>Level: ${account.level}
            <br/>Experience: ${account.experience}/${account.level*500 + 500}
            <br/>Health: ${account.health}
            <br/>Money: ${account.money}
        </div>

    </div>


    <script type="text/javascript" src="scripts/account.js"></script>


</body>
</html>
