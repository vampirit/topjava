<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <jsp:include page="fragments/headTag.jsp"/>

    <body>

        <jsp:include page="fragments/bodyHeader.jsp"/>
        <section>
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
            <c:choose>
                <c:when test="${meal.id eq null}">
                    <h2><spring:message code="meal.newMeal"/> </h2>
                </c:when>
                <c:otherwise>
                    <h2><spring:message code="meal.editMeal"/> </h2>
                </c:otherwise>
            </c:choose>

            <hr>

            <form method="post" action="/meals/save">
                <input type="hidden" name="id" value="${meal.id}">
                <dl>
                    <dt><spring:message code="meal.dateTime"/>:</dt>
                    <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
                </dl>
                <dl>
                    <dt><spring:message code="meal.description"/>:</dt>
                    <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
                </dl>
                <dl>
                    <dt><spring:message code="meal.calories"/>:</dt>
                    <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
                </dl>
                <button type="submit"><spring:message code="meal.save"/></button>
                <button onclick="window.history.back()" type="button"><spring:message code="meal.cancel"/></button>
            </form>
        </section>

        <jsp:include page="fragments/footer.jsp"/>
    </body>
</html>
