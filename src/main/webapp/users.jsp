<%@ page import="ru.javawebinar.topjava.AuthorizedUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Users</h2>


    Hi <c:out value="${loginUser.name}"/>!
    <br>




<form action="users" method="post" title="Login">
    <input type="hidden" name="action" value="login"/>

    <div>
        e-mail:
        <input type="email" name="email"/>
    </div>
    <div>
        password:
        <input type="password" name="password"/>
    </div>

    <div>
        <input type="submit" value="singin"/>
    </div>


</form>

<form action="users" method="post" title="Add user">
    <input type="hidden" name="action" value="register">

    <div>
        name:
        <input type="text" name="userName"/>
    </div>

    <div>
        e-mail:
        <input type="email" name="email"/>
    </div>

    <div>
        password:
        <input type="password" name="password"/>
    </div>

    <div>
        callories per day:
        <input type="number" name="cal"/>
    </div>

    <div>
        <input type="submit" value="singup"/>
    </div>

</form>
</body>
</html>