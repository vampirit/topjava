<%--
  Created by IntelliJ IDEA.
  User: HP pavilion
  Date: 04.11.2017
  Time: 17:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>
<html>
<head>
    <title>Meal</title>
</head>
<br>
<h3><a href="index.html">Home</a></h3>
<h3><a href="users">Users</a></h3>
<h2>Meals</h2>


<table border="1px">
    <thead>
        <tr>
            <td width="50">id</td>
            <td width="300">Time</td>
            <td width="200">Desc</td>
            <td width="50">Callories</td>
            <td width="50">Edit</td>
            <td width="50">Delete</td>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${requestScope.get('meals')}" var="meal">
            <c:if test="${meal.exceed eq true}">
                <c:set value="#ff9999" var="color"/>
            </c:if>
            <c:if test="${meal.exceed eq false}">
                <c:set value="#b3ffb3" var="color"/>
            </c:if>

            <tr bgcolor="${color}">
                <javatime:format value="${meal.dateTime}" pattern="dd.MM.yyyy HH:mm" var="parsedDate" />

                <td><c:out value="${meal.id}"/></td>
                <td><c:out value="${parsedDate}"/></td>
                <td><c:out value="${meal.description}"/></td>
                <td><c:out value="${meal.calories}"/></td>

                <c:set value="${meal.id}" var="id"/>
                <td>
                    <a href="meals?action=edit&id=${id}">edit</a>
                </td>
                <td>
                    <a href="meals?action=delete&id=${id}">delete</a>
                </td>
            </tr>



        </c:forEach>
    
    </tbody>
</table>

<div>
    <a href="meals?action=addMeal" methods="GET"> add meal </a>
</div>
</body>
</html>
