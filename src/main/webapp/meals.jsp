<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<html lang="ru">
<head>
    <title>Моя еда</title>
</head>
<body>
<h3><a href="users.jsp">Users</a></h3>
<hr>
<table align="justify" border="1">
    <caption><b>Моя еда</b></caption>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <tr style="${meal.excess ? 'color:red' : 'color:green'}">
            <th>${f:formatLocalDateTime(meal.dateTime)}</th>
            <th>${meal.description}</th>
            <th>${meal.calories}</th>
        </tr>
    </c:forEach>
</table>
</body>
</html>