<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Edit meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo" scope="request"/>
<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>DateTime</dt>
            <dd>
                <input type="datetime" name="dateTime" size=150
                       value="<%=TimeUtil.localDateTimeToHtml(meal.getDateTime())%>">
            </dd>
            <dd>
            <dt>Description</dt>
            <dd>
                <input type="text" name="description" size=150
                       value="${meal.description}">
            </dd>
            <dt>Calories</dt>
            <dd>
                <input type="text" name="calories" size=100
                       value="${meal.calories}">
            </dd>

        </dl>

    <button type="submit">Save</button>
    <button type="reset">Cancel</button>
    <button onclick="location.href = 'http://localhost:8080/topjava/meals';" id="myButton" class="float-left submit-button" >Back</button>
    </form>
</section>

</body>
</html>
