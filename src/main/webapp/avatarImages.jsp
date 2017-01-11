<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div>
    <ul>
        <c:forEach var="image" items="${imageIndexes}">
            <li class="avatarImage" id="${image}">
                <img src="${pageContext.request.contextPath}/images/avatars/${image}.png">
            </li>
        </c:forEach>
    </ul>
    <br/><button id="confirmAvatarChange" onclick="confirmAvatarChange()">Confirm change</button>

</div>