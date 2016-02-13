package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XmlDataString{

	private Connection conn;
   	private String xmlData;
   	private String itemId;

   	public XmlDataString (String itemid) {
      this.xmlData = "";
      try {
         	conn = DbManager.getConnection(true);
         	this.buildXML(itemId);
        	conn.close();
      	} catch (SQLException ex) {
         	ex.printStackTrace();
      }
   	}

   	public String getXML(){
   		return xmlData;
   	}
   	private void buildXML(String itemid) throws SQLException{
   		Statement s2 = conn.createStatement();
   		ResultSet rs = s2.executeQuery("SELECT * FROM Items WHERE ItemId="+ itemId);
   		while(rs.next()) {
   			xmlData+=String.format("<Item ItemID=\"%s\">\n", itemid);
	   		xmlData+=String.format("<Name>%s</Name>\n", replaceEscaped(rs.getString("name")));
	        // xmlData+=getCategoriesXML();

	        // xmlData+=String.format("<Currently>$%s</Currently>\n", rs.getString("currently"));
	        // String buy_price = rs.getString("buy_price");
	        // if (buy_price != null) {
	        // 	xmlData+=String.format("<Buy_Price>$%s</Buy_Price>\n", buy_price);
	        // }
	        // xmlData+=String.format("<First_Bid>$%s</First_Bid>\n", rs.getString("first_bid"));

	        // int number_bids = rs.getInt("number_of_bids");
	        // xmlData+=String.format("<Number_of_Bids>%d</Number_of_Bids>\n", number_bids);
	        // xmlData+=getBidsXML(number_bids);

	        // String latitude = rs.getString("latitude");
	        // String longitude = rs.getString("Longitude");
	        // if (latitude != null && longitude != null) {
	        //    	xmlData+=String.format("<Location Latitude=\"%s\" Longitude=\"%s\">%s</Location>\n",
	        //           latitude, longitude, replaceEscaped(rs.getString("location")));
	        // } else {
	        //     xmlData+=String.format("<Location>%s</Location>\n", replaceEscaped(rs.getString("location")));
	        // }

	        // xmlData+=String.format("<Country>%s</Country>\n", replaceEscaped(rs.getString("country")));
	        // xmlData+=String.format("<Started>%s</Started>\n", sqlToXMLDate(rs.getString("started")));
	        // xmlData+=String.format("<Ends>%s</Ends>\n", sqlToXMLDate(rs.getString("ends")));
	        // xmlData+=getUserXML(rs.getString("seller_id"), SELLER, null);
	        // xmlData+=String.format("<Description>%s</Description>\n", replaceEscaped(rs.getString("description")));
	        // xmlData+=String.format("</Item>");
   	
   		}
   	}

   	private String replaceEscaped(final String result) {
      if (result == null) {
         return null;
      }
      String text=result;
      
      text=text.replace("<","&lt;");
      text=text.replace(">","&gt;");
      text=text.replace("&","&amp;");
      text=text.replace("\"","&quot;");
      text=text.replace("\'","&apos;");

      return text;
   	}




}