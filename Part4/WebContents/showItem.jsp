<%@ page import="edu.ucla.cs.cs144.Item" %>
<%@ page import="edu.ucla.cs.cs144.User" %>
<%@ page import="edu.ucla.cs.cs144.Bid" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title>P4 Item Results</title>
</head>
<header><b>This is the result for Item <%= request.getAttribute("id_att")%></b></header>
<body>
    <% Item item = (Item)request.getAttribute("item"); %>

    Name: <%= item.getName()%><br>
    <% String cats="";
    for(String cat: item.getCategories()){
    	//cats+=" | ";
    	cats+=cat;
    	cats+=" | ";
	}%>
	Categories: <%= cats %><br>
    Currently: <%= item.getCurrently()%><br>
    Buy Price: <%= item.getBuyPrice()%><br>
    First Bid: <%= item.getFirstBid()%><br>
    No. Of Bids: <%= item.getNoOfBids()%><br>
    <% int cnt=1;
    String bid="";
    Map<String, Bid> map= (Map<String, Bid>) item.getBids();
    for (Map.Entry<String, Bid> entry : map.entrySet()) {
    	bid=bid+"&nbsp;&nbsp;Bid "+cnt+"<br>";
    	bid=bid+"&nbsp;&nbsp;&nbsp;&nbsp;Bidder:<br>";
    	bid=bid+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Id: "+entry.getValue().getBidder().getUserId()+"<br>";
    	bid=bid+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Rating: "+entry.getValue().getBidder().getRating()+"<br>";
    	bid=bid+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Location: "+entry.getValue().getBidder().getLocation()+"<br>";
    	bid=bid+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Country: "+entry.getValue().getBidder().getCountry()+"<br>";
    	bid=bid+"&nbsp;&nbsp;&nbsp;&nbsp;Time: "+entry.getValue().getTime()+"<br>";
    	bid=bid+"&nbsp;&nbsp;&nbsp;&nbsp;Amount: "+entry.getValue().getAmount()+"<br>";
    	cnt++;
	}%>
	Bids: <br> <%= bid%>
    Location: <%= item.getLocation()%><br>
    Latitude: <%= item.getLatitude()%><br>
    Longitude: <%= item.getLongitude()%><br>
    Country: <%= item.getCountry()%><br>
    Started: <%= item.getStarted()%><br>
	Ends: <%= item.getEnds()%><br>
	<% 
    String sellerStr="";
    User seller= (User) item.getSeller();
    sellerStr=sellerStr+"&nbsp;&nbsp;&nbsp;&nbsp;Id: "+seller.getUserId()+"<br>";
    sellerStr=sellerStr+"&nbsp;&nbsp;&nbsp;&nbsp;Rating: "+seller.getRating()+"<br>";
    %>
    Seller: <br> <%= sellerStr%>
    Description: <%= item.getDescription()%><br>

</body>
</html>