
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>
<html>
<head>
    <title></title>
</head>
<body>


<form action="meals" method="post" name="addEditMeal">
    <c:set value="${param.get('meal')}" var="meal"/>
    
    <c:if test="${meal.id ne 0}">
        <div>
            <label>Id</label>
            <input type="text" value="${meal.id}" readonly="readonly" name="id"/>
            </br>
        </div>
    </c:if>
    
    <div>
        <label>Date and time</label>
        <javatime:format value="${meal.dateTime}" pattern="dd.MM.yyyy HH:mm" var="parsedDate"/>
        <input type="text" name="dateTime" value="${parsedDate}"/>
        </br>
    </div>

    <div>
        <label>Description:</label>
        <input type="text" name="description" value="${meal.description}">
        </br>
    </div>

    <div>
        <label>Calories:</label>
        <input type="text" name="calories" value="${meal.calories}">
        </br>
    </div>

    <input type="submit" value="Submit"/>
</form>

</body>
</html>
