<%@ page import="edu.ucla.cs.cs144.Item" %>
<%@ page import="edu.ucla.cs.cs144.User" %>
<%@ page import="edu.ucla.cs.cs144.Bid" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html> 
<html>
<head>

    <title>P4 Item Result Page</title>
    <style type="text/css">

/** {
 font-size: 100%;
 font-family: Arial;
}
*/
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
<body onload="initialize()">
<div id=topright>
<a href="/eBay/keywordSearch.html" title="TopRightKey">Keyword Search</a><br>
<a href="/eBay/getItem.html" title="TopRightItem">Item Search</a></div>
<p><b>CS 144 eBay</b><br/></p>
<hr>

    <form action="/eBay/item" method="GET" id="top_form">
    <input type="text" name="itemid">
    <input type="submit" value="Search">
    </form> 

<div>

<h3><b>&nbsp;&nbsp;This is the result for item <%= request.getAttribute("id_att")%></b></h3>

<% Item item = (Item)request.getAttribute("item");%>
    <% if(item.getItemId()== null){ %> 
        There is no item with that ItemId. Please search below.
        <% return;} %>
<p id="info">
    Name: <%= item.getName()%><br>
    <% String cats="";
    for(String cat: item.getCategories()){
    	cats+=cat;
    	cats+=" | ";
	}%>
	Categories: <%= cats %><br>
    Currently: <%= item.getCurrently()%><br>
    Buy Price: <%= item.getBuyPrice()%>
    <% String formAction= "/eBay/creditcard?itemid="+ item.getItemId(); %>
    <% if(!item.getBuyPrice().isEmpty()){ %>
    <a href=<%=formAction%>>Pay Now</a>
    <% } %>
    <br>
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


</p>
</div>
<%  String Location = (String) item.getLocation(); 
    String Country =(String)item.getCountry(); 
	String item_id = (String) item.getItemId(); %>

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 

<style type="text/css"> 
  html { height: 100% } 
  body { height: 100%; margin: 0px; padding: 0px } 
  #map_canvas { 
    height: 100%;
    }
</style> 

<script type="text/javascript" 
    src="http://maps.google.com/maps/api/js?sensor=false"> 
</script> 

<script type="text/javascript"> 
  function initialize() { 
    geocoder = new google.maps.Geocoder();
    var address = "<%=Location + " " + Country%>";
    //var address = "PAYPAL OK";
   	var latlng = new google.maps.LatLng(34.063509,-118.44541); 
    var myOptions = { 
      zoom: 14, // default is 8  
      center: latlng, 
      mapTypeId: google.maps.MapTypeId.ROADMAP 
    }; 
    var map = new google.maps.Map(document.getElementById("map_canvas"), 
        myOptions); 
    geocoder.geocode( { 'address': address}, function(results, status) {
    	if (status == google.maps.GeocoderStatus.OK) {
			  map.setCenter(results[0].geometry.location);
			  map.setZoom(13);
			  var marker = new google.maps.Marker({position:results[0].geometry.location,title:"Item <%= item_id %>"});
			  marker.setMap(map);
		}
    });
  } 

</script> 
<div id="map_canvas" style="width:100%; height:70%"></div> 

</body>
</html>