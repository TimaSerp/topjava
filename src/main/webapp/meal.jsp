<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Добавить еду</title>
</head>
<body>
<jsp:useBean id="meal" scope="request" type="java.lang.Object"/>
<form method="post" action="meals" name="frmAddMeal">
    ID : <label>
    <input type="text" readonly="readonly" name="mealId"
           value="<c:out value="${meal.id}"/>"/> <br />
</label>
    Дата и время :<label>
    <input type="datetime-local" name="dateTime" size="45"
           value="<c:out value="${meal.dateTime}"/>"/> <br />
</label>
    Описание : <label>
    <input type="text" name="description"
           value="<c:out value="${meal.description}"/>"/> <br />
</label>
    Калории : <label>
    <input type="text" name="calories"
           value="<c:out value="${meal.calories}"/>"/> <br />
</label>
    <input type="submit" value="Save"/>
</form>
</body>
</html>
