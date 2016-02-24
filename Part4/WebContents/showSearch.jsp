<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
    <title>P4 Search Results</title>
</head>
<header><b>These are the results from your search</b></header>
<body>
    <!--Number of results to return: 
    <%= request.getAttribute("numberof") %> -->

    <% SearchResult[] results = (SearchResult[])  request.getAttribute("searchResults"); %>

    <!-- Can change this to display different results using next/previous-->
   	<table id="results-table">
    <%
    	int page_beginning =0; String item_id; String item_name;


	    	for(SearchResult currentResult: results)
	    	{
	    		item_id= currentResult.getItemId();
	    		item_name = currentResult.getName();
    %>
    		<tr>
    			<td> <%= item_id %> </td> 
    			<td><a href= "/eBay/item?id=<%= item_id %>"><%= item_name %></a></td>
    		</tr>

    <%		} 	
    %>
   	</table>
</body>
</html>