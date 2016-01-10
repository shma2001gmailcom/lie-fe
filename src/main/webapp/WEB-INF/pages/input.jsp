<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="resources.jsp" %>
<%--
  ~ Copyright (c) 2014. Misha's property, all rights reserved.
  --%>

<head>
    <title>hello</title>
</head>
<html>
<body>
<div class="error-message"></div>
<h1 class="color-violet"><c:out value="${welcomeMessage}"/></h1>
<a class='color-blue' href="http://en.wikipedia.org/wiki/Free_Lie_algebra#Lyndon_basis">
    (click here for explanation)
</a>

<h3 class="color-light-brown"><c:out value="${polynomialMessage}"/>
    <a href="http://en.wikipedia.org/wiki/Free_Lie_algebra#Lyndon_basis">
        <c:out value="${hallBase}"/>
    </a>
</h3>
<%@include file="polynomial-input-form.jsp" %>
<br/>

<h3 class="color-light-brown"><c:out value="${endoMessage}"/></h3>
<%@include file="endo-input-form.jsp" %>
<br/>

<h3 class="color-light-brown"><c:out value="${expandMessage}"/></h3>
<%@include file="expand-input-form.jsp" %>
<br/>

<h3 class="color-light-brown"><c:out value="${foxMessage}"/></h3>
<%@include file="fox-input-form.jsp" %>
<br/>

<h3 class="color-light-brown"><c:out value="${jacobiMessage}"/></h3>
<%@include file="jacobi-input-form.jsp" %>
<jsp:include page="include.jsp" flush="true"/>
</body>
</html>