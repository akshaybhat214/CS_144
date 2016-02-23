<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
    <title>Adi</title>
</head>
<body>
    Hello, world. <%= request.getAttribute("numberof") %>
    <% SearchResult[] results = (SearchResult[])  request.getAttribute("searchResults"); %>
</body>
</html>