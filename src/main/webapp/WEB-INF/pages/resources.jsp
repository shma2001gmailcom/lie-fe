<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  ~ Copyright (c) 2014. Misha's property, all rights reserved.
  --%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <c:set var="context" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript"
            src="<c:out value="${context}"/>/resources/jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript"
            src="<c:out value="${context}"/>/resources/js/bad-input.js"></script>
    <script type="text/javascript"
            src="<c:out value="${context}"/>/resources/js/monomial-validator.js"></script>
    <link type="text/css" href="<c:out value="${context}"/>/resources/css/styles.css"
          rel="stylesheet"/>
</head>
<body></body>
</html>