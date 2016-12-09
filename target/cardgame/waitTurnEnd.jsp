<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Wait turn end</title>

    <script type="text/javascript">
        function checkTurnEnd() {
            console.log(${battles[battleId].turnEnded});
            if (${battles[battleId].turnEnded}) {
                window.location.href="battlefield.jsp";
            } else {
                window.location.reload();
            }
        }
        setInterval("checkTurnEnd()",2000);
    </script>
</head>
<body>
    <br/>WAIT TURN END
    <br/>

    <%-- потом на этой странице можно будет отображать что происходит при подсчете хода --%>

    <br/>

    <%--<script type="text/javascript">--%>

        <%--function checkTurnEnd() {--%>
            <%--if (${battles[battleId].turnEnded}) {--%>
                <%--window.location.href="battlefield.jsp";--%>
            <%--} else {--%>
                <%--window.location.reload();--%>
            <%--}--%>
        <%--}--%>
        <%--setInterval("checkTurnEnd()",5000);--%>

    <%--</script>--%>

</body>
</html>
