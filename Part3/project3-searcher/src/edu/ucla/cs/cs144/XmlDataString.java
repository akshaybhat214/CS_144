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
   	//private String itemId;

   	public XmlDataString (String itemid) {
      xmlData = "";
      System.out.println("hello");
      try {
         	conn = DbManager.getConnection(true);
         	this.buildXML(itemid);
         	conn.close();
      	} catch (SQLException ex) {
         	ex.printStackTrace();
         }
   	}

   	public String getXML(){
   		return xmlData;
   	}
   	private void buildXML(String itemid) throws SQLException{
         System.out.println(itemid);
   		Statement s2 = conn.createStatement();
   		ResultSet rs = s2.executeQuery("SELECT * FROM Items WHERE ItemId="+ itemid);
   		while(rs.next()) {
            System.out.println("hello2");
            xmlData+=String.format("<Item ItemID=\"%s\">\n", itemid);
            xmlData+=String.format("<Name>%s</Name>\n", replaceEscaped(rs.getString("name")));
            xmlData+=getCategoriesXML(itemid);

            xmlData+=String.format("<Currently>$%s</Currently>\n", rs.getString("Currently"));
            String BuyPrice = rs.getString("BuyPrice");
            if (BuyPrice != null) {
               xmlData+=String.format("<Buy_Price>$%s</Buy_Price>\n", BuyPrice);
            }
            xmlData+=String.format("<First_Bid>$%s</First_Bid>\n", rs.getString("FirstBid"));

	        int NumOfBids = rs.getInt("NumOfBids");
	        xmlData+=String.format("<Number_of_Bids>%d</Number_of_Bids>\n", NumOfBids);
	        xmlData+=getBidsXML(itemid,NumOfBids);

	        String lat = rs.getString("Latitiude");
	        String Longitude = rs.getString("Longitude");
	        if (lat != null && Longitude != null) {
	           	xmlData+=String.format("<Location Latitude=\"%s\" Longitude=\"%s\">%s</Location>\n",
	                  lat, Longitude, replaceEscaped(rs.getString("Location")));
	        } else {
	            xmlData+=String.format("<Location>%s</Location>\n", replaceEscaped(rs.getString("Location")));
	        }

	        xmlData+=String.format("<Country>%s</Country>\n", replaceEscaped(rs.getString("Country")));
	        xmlData+=String.format("<Started>%s</Started>\n", sqlToXMLDate(rs.getString("Started")));
	        xmlData+=String.format("<Ends>%s</Ends>\n", sqlToXMLDate(rs.getString("Ends")));
	        xmlData+=getUserXML(rs.getString("SellerId"), 0);
	        //xmlData+=String.format("<Description>%s</Description>\n", replaceEscaped(rs.getString("Description")));
	        xmlData+=String.format("</Item>");
   	
   		}
   	}

      private String getBidsXML(String itemid, int bid_count) throws SQLException {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT BidderId, Time, Amount FROM Bids WHERE ItemId="+ itemid);
         String bids_xml = "";
         if (bid_count > 0) {
            bids_xml += "<Bids>\n";
            while(rs.next()) {
               bids_xml += "<Bid>\n";
               String userid = rs.getString("BidderId");
               bids_xml += getUserXML(userid, 1);
               bids_xml += String.format("<Time>%s</Time>\n", sqlToXMLDate(rs.getString("Time")));
               bids_xml += String.format("<Amount>$%s</Amount>\n", rs.getString("Amount"));
               bids_xml += "</Bid>\n";
            }
            bids_xml += "</Bids>\n";
         } else {
            bids_xml += "<Bids />\n";
         }
         return bids_xml;
      }

      private String getUserXML(String userid, int type) throws SQLException {
         String user_xml = "";
         if (type == 0) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Rating FROM Users WHERE UserId ='"+userid+"'");
            if (rs.next()) {
               user_xml += String.format("<Seller Rating=\"%s\" UserID=\"%s\" />\n", rs.getString("Rating"), userid);
            } else {
               System.err.format("No rating found for seller \"%s\"\n", userid);
            }
         } else if (type == 1) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Rating FROM Users WHERE UserId ='"+userid+"'");
            if (rs.next()) {
               String rating=rs.getString("Rating");
               Statement stmt1 = conn.createStatement();
               ResultSet rs1 = stmt.executeQuery("SELECT Location, Country FROM UserLocation WHERE UserId ='"+userid+"'");
               if (rs1.next()) {
                  user_xml += String.format("<Bidder Rating=\"%s\" UserID=\"%s\">\n", rating , userid);
                  String location = replaceEscaped(rs1.getString("Location"));
                  String country = replaceEscaped(rs1.getString("Country"));
                  if (location != null) {
                     user_xml += String.format("<Location>%s</Location>\n", location);
                  }
                  if (country != null) {
                     user_xml += String.format("<Country>%s</Country>\n", country);
                  }
                  user_xml += "</Bidder>\n";
               } else {
                  System.err.format("No user found for id \"%s\"\n", userid);
               }
            } else {
               System.err.format("No rating found for seller \"%s\"\n", userid);
            }
         }
         return user_xml;
      }

      private String getCategoriesXML(String itemid) throws SQLException {
         Statement s2 = conn.createStatement();
         ResultSet rs = s2.executeQuery("SELECT Category FROM Categories WHERE ItemId="+ itemid);
         String categories= "";
         while (rs.next()) {
            categories+= String.format("<Category>%s</Category>\n", replaceEscaped(rs.getString("Category")));
         }
            return categories;
      }

      private String sqlToXMLDate( String date) {
         DateFormat sql_format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
         DateFormat xml_format = new SimpleDateFormat("MMM-dd-yy kk:mm:ss");
         try {
            Date d = sql_format.parse(date);
            return xml_format.format(d);
         } catch ( ParseException ex) {
            ex.printStackTrace();
            System.out.format("Error parsing date \"%s\"!\n", date);
            return "";
         }
      }
   	private String replaceEscaped(String result) {
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