<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<jsp:useBean id="userText" type="java.lang.String" scope="request"/>
<h2>${userText}</h2>
<ul style="font-size: large">
    <li><a href="meals">Ваша еда</a></li>
</ul>
</body>
</html>