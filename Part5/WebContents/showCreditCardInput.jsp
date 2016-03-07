<%@ page import="edu.ucla.cs.cs144.PaymentInfo" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html> 
<html>
<head>

    <title>P4 Credit Card Payment Page</title>
    <style type="text/css">

#top_form{
    padding: 5px;
}

#info {

    padding: 15px;
}

#topright {
    position: absolute;
    right: 0;
    top: 0;
    }
</style>

</head>
<body>
<div id=topright>
<a href="/eBay/keywordSearch.html" title="TopRightKey">Keyword Search</a><br>
<a href="/eBay/getItem.html" title="TopRightItem">Item Search</a></div>
<p><b>CS 144 eBay</b><br/></p>
<hr>
<% PaymentInfo itemInfo = (PaymentInfo)request.getAttribute("PaymentInfo");%>
<h3>Credit Card Input</h3>
Item ID: <%= itemInfo.getItemId()%><br>
Name: <%= itemInfo.getName()%><br>
Buy Price: <%= itemInfo.getBuyPrice()%><br>
<% String formAction= "https://" + request.getServerName() + ":8443" + request.getContextPath() + "/confirmation"; %>
Credit Card:
<form name="creditCardForm" action="<%= formAction %>" method="POST">
	<input type="text" name="creditCard" placeholder="Credit Card Number">
	<input type="hidden" name="itemId" value="<%= itemInfo.getItemId() %>">
	<input type="submit" value="Submit">
</form>
</body>
</html>