<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="resources.jsp" %>
<html>
<head>
    <title>expand</title>
</head>
<body>
<form:form id="form" modelAttribute="polynomialObject" action="download" class="check-me">
    <%--put ${answer} into DownloadService scope--%>
    <form:input type="hidden" id="answer" path="value" value="${given}=${answer}"/>
    <div id="given" class='text'><c:out value="${given}"/></div>
    <br>

    <div class='text'>=</div>
    <div id="answer" class='text'><c:out value="${answer}"/></div>
    <div id="error" class="error-message"><c:out value="${error}"/></div>

    <input type="submit" class="button" value="export"/>
</form:form>
<%@ include file="home-link.jsp" %>
<jsp:include page="include.jsp" flush="true"/>
</body>
</html>