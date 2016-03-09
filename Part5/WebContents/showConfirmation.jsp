<%@ page import="edu.ucla.cs.cs144.PaymentInfo" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.ucla.cs.cs144.Item" %>

<!DOCTYPE html> 
<html>
<head>

<title>P4 Confirmation Page</title>
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

    tr{
        padding: 15px;
        text-align: left;
        border-bottom: 1px solid #ddd;
    }

    tr:nth-child(odd) {background-color: #f2f2f2}

    </style>
    </style>

</head>
<body>
    <% String formAction1= "http://" + request.getServerName() +":"+ session.getAttribute("port_no")  + request.getContextPath() + "/keywordSearch.html"; %>
    <% String formAction2= "http://" + request.getServerName() +":"+ session.getAttribute("port_no")  + request.getContextPath() + "/getItem.html"; %>
    <div id=topright>
    <a href= "<%= formAction1 %>" title="TopRightKey">Keyword Search</a><br>
    <a href= "<%= formAction2 %>" title="TopRightItem">Item Search</a></div>
    <p><b>CS 144 eBay</b><br/></p>
    <hr>

    <%  if(request.getAttribute("invalid_session").equals("true")) {  %>
        <p><b>The request was invalidated: session not secure</b><br/></p>
        <% return;  }  %>

    <% PaymentInfo itemInfo = (PaymentInfo)request.getAttribute("PaymentInfo");%>

    <%  if(itemInfo==null) {  %>
        <p><b>There was an error completing the transaction.</b><br/></p>
        <% return;  }  %>

    <h3>Order Confirmation</h3>
        <table id="results-table">
            <tr>    
                <td>Item ID:</td> 
                <td><%= request.getAttribute("itemid")%></td>
            </tr>
            <tr>    
                <td>Name:</td> 
                <td><%= itemInfo.getName()%></td>
            </tr>
            <tr>    
                <td>Buy Price:</td> 
                <td><%= itemInfo.getBuyPrice()%></td>
            </tr>
            <tr>    
                <td>Credit Card Number:</td> 
                <td><%= request.getAttribute("creditCard") %></td>
            </tr>
            <tr>    
                <td>Time:</td> 
                <td><%= request.getAttribute("order_ts")%></td>
            </tr>
        </table>
</body>
</html>