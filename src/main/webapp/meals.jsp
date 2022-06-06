<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://example.com/functions" prefix="f" %>
<html lang="ru">
<head>
    <title>Моя еда</title>
</head>
<body>
<h3><a href="index.html">На домашнюю страницу</a></h3>
<hr>
<table align="justify" border="1">
    <caption><b>Моя еда</b></caption>
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${meals}" var="meal">
        <tr style="${meal.excess ? 'color:red' : 'color:green'}">
            <td><c:out value="${f:formatLocalDateTime(meal.dateTime)}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=edit">Add User</a></p>
</body>
</html>