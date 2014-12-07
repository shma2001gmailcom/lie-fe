<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="resources.jsp" %>
<html>
<head>
    <title>fox</title>
</head>
<body>
<form:form id="form" modelAttribute="polynomialObject" action="download" class="check-me">
    <form:input type="hidden" id="answer" path="value" value="${answer}"/>
    <div id="answer" class='color-light-gray-yellow'>${answer}</div>
    <div id="error" class="error-message">${error}</div>
    <br/>
    <input type="submit" class="button" value="export"/>
</form:form>
<%@ include file="home-link.jsp" %>
</body>
</html>