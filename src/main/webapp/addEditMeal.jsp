
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>
<html>
<head>
    <title></title>
</head>
<body>


<form action="meals" method="post" name="addEditMeal">

    <c:if test="${meal.id ne 0}">
        <div>
            <label>Id</label>
            <input type="number" value="${meal.id}" readonly="readonly" name="id"/>
            </br>
        </div>
    </c:if>
    
    <div>
        <label>Date and time</label>
        <input type="datetime-local" name="dateTime" value="${meal.dateTime}"/>
        </br>
    </div>

    <div>
        <label>Description:</label>
        <input type="text" name="description" value="${meal.description}">
        </br>
    </div>

    <div>
        <label>Calories:</label>
        <input type="number" name="calories" value="${meal.calories}">
        </br>
    </div>

    <input type="submit" value="Submit"/>
    <input type="reset" value="Reset"/>
</form>

<a href="meals"> Назад </a>

</body>
</html>
