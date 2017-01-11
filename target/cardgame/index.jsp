<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cardgame</title>
</head>
<body>
    <br/>Welcome to cardgame
    <br/>
    <p><textarea id="help" cols="150" rows="18" disabled>
        Добро пожаловать в игру, у которой пока даже нет названия.
    Проект находится в процессе разработки и представлен здесь только в ознакомительных целях.
    Вы запросто можете обнаружить здесь массу багов и недоработок
    (информацию о которых, при желании, можно отправлять на почту gut.professor@gmail.com).
    Кроме того, визуальная часть находится в зачаточном состоянии,
    потому красивых картинок и спецэффектов здесь не увидеть.

    Тем не менее, здесь можно зарегистрироваться, купить на выделенную сумму игровых денег карты,
    составить колоду и провести сражение с другим игроком (если таковой имеется) или с собой же,
    зарегистрировавшись и залогинившись с другого браузера (мультиводство пока не запрещено).
    Также после сражений будут начислены опыт и деньги.

    В игре есть небольшие справочные записи, вроде той, что читаете сейчас.

    Итак, это страница входа и, по совместительству, регистрации.
    Логин  - не должен повторяться (думаю, это логично).
    Пароль - не меньше 5 символов (других условий пока нет).
    Е-мейл - должен выглядеть как е-мейл (не обязательно существующий).
    </textarea></p>


    <br/>LOGIN
    <br/>

    <form action="login.do" method="post" enctype="application/x-www-form-urlencoded">
        ${error}
        <br/>
        <label for="login">Login:</label>
        <br><input name="login" id="login" type="text"/>
        <br><label for="password">Password:</label>
        <br><input name="password" id="password" type="password"/>
        <br><input type="submit" value="login"/>
    </form>

    <br/>OR
    <br/>

    <br/>REGISTER NEW USER
    <form action="register.do" method="post" enctype="application/x-www-form-urlencoded">

        <label for="regLogin">Login:</label>
        <br><input name="regLogin" id="regLogin" type="text" value="${login}"/>
        ${errorMap.login}

        <br><label for="regPassword">Password:</label>
        <br><input name="regPassword" id="regPassword" type="password" value="${password}"/>
        ${errorMap.password}

        <br><label for="confirmPassword">Confirm password:</label>
        <br><input name="confirmPassword" id="confirmPassword" type="password" value="${password}"/>
        ${errorMap.confirmPassword}

        <br><label for="regEmail">Email:</label>
        <br><input name="regEmail" id="regEmail" type="text" value="${email}"/>
        ${errorMap.email}


        <br><input type="submit" value="register"/>
    </form>

</body>
</html>
