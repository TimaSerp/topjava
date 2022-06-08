<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <jsp:useBean id="status" scope="request" type="java.lang.Object"/>
    <title>${status=="edit"? "Обновить еду" : "Добавить еду"}</title>
</head>
<body>
<jsp:useBean id="meal" scope="request" type="java.lang.Object"/>
<form method="post" action="meals" name="frmSaveMeal">
    <label>
        <input type="number" name="id" readonly="readonly"
                  value="${meal.id}"></label> <br/>
    Дата и время :<label>
    <input type="datetime-local" name="dateTime"
           value="${meal.dateTime}"/> <br/>
</label>
    Описание : <label>
    <input type="text" name="description"
           value="${meal.description}"/> <br/>
</label>
    Калории : <label>
    <input type="number" name="calories"
           value="${meal.calories}"/> <br/>
</label>
    <input type="submit" value="Save"/>
</form>
<h3><a href="meals">К списку еды</a></h3>
</body>
</html>
