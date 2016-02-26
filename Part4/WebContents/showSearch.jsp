<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
    <title>P4 Search Results</title>

    <style type="text/css">

    #results-table{
    	width: 60%;
    	border-collapse: collapse;
	}

    tr{
    padding: 15px;
    text-align: left;
    border-bottom: 1px solid #ddd;
	}

	* {
	 font-size: 100%;
	 font-family: Arial;
	}

    tr:nth-child(even) {background-color: #f2f2f2}

    </style>
</head>
<body>

<form action=" http://localhost:1448/eBay/search" method="GET">
    <input type="text" name="query">
    <input type="submit" value="Search">
    <input type="hidden" name="skips" value="0">
	<input type="hidden" name="toReturn" value="15">
</form>

	<!--Number of results to skip: <%= request.getAttribute("skips") %> -->

    <% String query = (String) request.getAttribute("q"); %> 
    <% Integer retStr = (Integer) request.getAttribute("numberof"); %> 
    <% SearchResult[] results = (SearchResult[])  request.getAttribute("searchResults"); %>
		<header><b>These are the <%=results.length %> results on this page</b></header> <br>

	   	<table id="results-table">

		<% if(results.length > 0) {	%>

	   		   	<tr>
	    			<td> <b>Item Id </b> </td> 
	    			<td><b>Item Name </b></td>
	    		</tr>

	    <% }	%>

	    <%
	    	int page_start =(Integer) request.getAttribute("skips"); 
	    	String item_id; String item_name;

		    	for(SearchResult currentResult: results)
		    	{
		    		item_id= currentResult.getItemId();
		    		item_name = currentResult.getName();
	    %>

	    		<tr>
	    			<td><a href= "/eBay/item?itemid=<%= item_id %>"> <%= item_id %> </td> 
	    			<td><%= item_name %></a></td>
	    		</tr>

	    <%		} 	
	    %>
	   	</table><br>

	<%if (page_start>0) 
		{	%>
	   	<div>
	   	<a href= "/eBay/search?query=<%= query %>&skips=<%= page_start-15 %>&toReturn=<%= 15 %>">Previous <%=retStr %> Results
	   	</a></div>
    <%	} 	 
    	else{
    	%>
    			<br>
    <%	}	%>

	<%if (results.length >= 0) 
		{	%>
	   	<div>
	   	<a href= "/eBay/search?query=<%= query %>&skips=<%= page_start+15 %>&toReturn=<%= 15 %>">Next <%=retStr %> Results
	   	</a></div>
    <%	} 	 %>

</body>
</html>