<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                <input type="datetime-local" name="dateTime" size="100" value="${meal.dateTime}">
            </dd>
            <dd>
            <dt>Description</dt>
            <dd>
                <input type="text" name="description" size=150
                       value="${meal.description}">
            </dd>
            <dt>Calories</dt>
            <dd>
                <input type="number" name="calories" size=100
                       value="${meal.calories}">
            </dd>

        </dl>

        <button type="submit">Save</button>
        <button type="reset">Cancel</button>
        <button onclick="history.back()">Back</button>
    </form>
</section>

</body>
</html>
