<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2014. Misha's property, all rights reserved.
  --%>
<html>
<head>
    <title></title>
</head>
<body>
<form:form id="form" modelAttribute="polynomialObject" action="hall-result" class="check-me">
    <div>
        <label>
            <form:input type="text" class="text-field" width="150" size="150px" path="value"
                        value="+ [[[[c, b], a], a], a] + [[[[b, a], a], a], c] - [[[[c, a], a], a], b]"/>
        </label>
    </div>
    <br/>

    <div>
        <input class="button" type="submit" value="submit"/>
    </div>
</form:form>
</body>
</html>