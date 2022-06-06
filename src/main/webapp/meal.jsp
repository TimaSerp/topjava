<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Добавить еду</title>
</head>
<body>
<jsp:useBean id="meal" scope="request" type="java.lang.Object"/>
<form method="post" action="meals" name="frmAddMeal">
    ID : <label>
    <input type="text" readonly="readonly" name="mealId"
    value="<c: out value="${meal.id}"/>/> <br/>
</label>
    Время :<label>
    <input type="datetime-local" name="dateTime"
           value="<c:out value="${meal.dateTime}"/>"/>
</label>
    Описание : <label>
    <input type="text" name="description"
           value="<c:out value="${meal.description}"/>"/>
</label>
    Калории : <label>
    <input type="text" name="calories"
           value="<c:out value="${meal.calories}"/>"/>
</label>
    <input type="submit" value="Save"/>
</form>
</body>
</html>
