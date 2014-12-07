<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="resources.jsp" %>
<head>
    <title>hello</title>
</head>
<html>
<body>
<h1 class="color-violet">${welcomeMessage}</h1>
<a class='color-blue' href="http://en.wikipedia.org/wiki/Free_Lie_algebra#Lyndon_basis">
    (click here for explanation)
</a>

<h3 class="color-light-brown">${polynomialMessage}</h3>
<%@include file="polynomial-input-form.jsp" %>
<br/>

<h3 class="color-light-brown">${endoMessage}</h3>
<%@include file="endo-input-form.jsp" %>
<br/>

<h3 class="color-light-brown">${expandMessage}</h3>
<%@include file="expand-input-form.jsp" %>
<br/>

<h3 class="color-light-brown">${foxMessage}</h3>
<%@include file="fox-input-form.jsp" %>
<br/>

<h3 class="color-light-brown">${jacobiMessage}</h3>
<%@include file="jacobi-input-form.jsp" %>
</body>
</html>