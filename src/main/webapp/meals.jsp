<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<section>
    <a href="meals?uuid=${meal.id}&action=add">Add Meal</a>
    <table border="1" cellpadding="8" cellspacing="0" style="margin: auto">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr>
                <td>
                    <span style="color: ${meal.excess ? "red":  "green"};"><%=TimeUtil.localDateTimeToHtml(meal.getDateTime())%></span>
                </td>
                <td>
                    <span style="color: ${meal.excess ? "red":  "green"};">${meal.description}</span>
                </td>
                <td>
                    <span style="color: ${meal.excess ? "red":  "green"};">${meal.calories}</span>
                </td>
                <td><a href="meals?id=${meal.id}&action=update">Update</a></td>
                <td><a href="meals?id=${meal.id}&action=delete">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>

</body>
</html>
