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
<form:form id="form4" modelAttribute="endoObject" action="jacobi-result" class="check-me">
    <div>
        <label>
            <form:input type="text" class="text-field" width="150" size="150px" path="value"
                        value="(- a - 2[b, c] + 2[c, [a, c]]; + b + 2[a, c] - 2[c, [b, c]] + 2[c, [c, [a, c]]]; + c)"/>
        </label>
    </div>
    <br/>

    <div>
        <input class="button" type="submit" value="submit"/>
    </div>
</form:form>
</body>
</html>