<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>
        nn</title>
</head>
<body>
<form:form id="form3" modelAttribute="polynomialObject" action="fox" class="check-me">
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