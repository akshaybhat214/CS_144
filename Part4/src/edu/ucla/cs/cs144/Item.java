package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


import org.xml.sax.InputSource;

public class Item {
	private String itemId;
    private String name;
    private List<String> categories;
    private String currently;
    private String buyPrice;
    private String firstBid;
    private String noOfBids;
    private Map<String, Bid> sortedBids;
    private String location; 
    private String latitude; 
    private String longitude;
    private String country;
    private String started;
    private String ends;
    private User seller;
    private String description; 
    //private List<Bid> bids;

    public String getItemId(){return itemId;}
    public String getName(){return name;}
    public List<String> getCategories(){return categories;}
    public String getCurrently(){return currently;}
    public String getBuyPrice(){return buyPrice;}
    public String getFirstBid(){return firstBid;}
    public String getNoOfBids(){return noOfBids;}
    public Map<String, Bid> getBids(){return sortedBids;}
    public String getLocation(){return location;}
    public String getLatitude(){return latitude;}
    public String getLongitude(){return longitude;}
    public String getCountry(){return country;}
    public String getStarted(){return started;}
    public String getEnds(){return ends;}
    public User getSeller(){return seller;}
    public String getDescription(){return description;}

    static DocumentBuilder builder;
	static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }

    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    static String formatTime(String oldTime) {
        String retString = oldTime;
        try
        {
            SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat old_format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

            retString = new_format.format(old_format.parse(oldTime));

        }
        catch(ParseException pe)
        { System.out.println("Error:Could not convert time from Old format to new format");}

        return retString;
    }

    public Item(String xml) {

    	Document doc = null;
		try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            StringReader reader = new StringReader(xml);
            doc = builder.parse(new InputSource(reader));
            doc.getDocumentElement().normalize();
            Element itemElm = doc.getDocumentElement();

            this.itemId = itemElm.getAttribute("ItemID");
            this.name = getElementTextByTagNameNR(itemElm, "Name");
            categories= new ArrayList<String>();
            Element catArr[] = getElementsByTagNameNR(itemElm, "Category");
            for (Element e : getElementsByTagNameNR(itemElm,"Category")) {
                this.categories.add(getElementText(e));
            }
            this.currently = strip(getElementTextByTagNameNR(itemElm, "Currently"));
            this.buyPrice = strip(getElementTextByTagNameNR(itemElm, "Buy_Price"));
            this.firstBid = strip(getElementTextByTagNameNR(itemElm, "First_Bid"));
            this.noOfBids = getElementTextByTagNameNR(itemElm, "Number_of_Bids");

            Element locElement = getElementByTagNameNR(itemElm, "Location");
            this.location = getElementText(locElement);
            this.latitude = locElement.getAttribute("Latitude");
            this.longitude = locElement.getAttribute("Longitude");
            this.country= getElementTextByTagNameNR(itemElm, "Country");

            this.started = formatTime(getElementTextByTagNameNR(itemElm, "Started"));
            this.ends = formatTime(getElementTextByTagNameNR(itemElm, "Ends"));
            Element sellerElement = getElementByTagNameNR(itemElm, "Seller");
            String iDStr = sellerElement.getAttribute("UserID");
            String ratingStr = sellerElement.getAttribute("Rating");
            seller = new User(iDStr, ratingStr, "","");
            this.description = getElementTextByTagNameNR(itemElm, "Description");  
            if (description.length() > 4000)
                description = description.substring(0, 4000);
            
            

            //bids = new ArrayList<Bid>();
            Map<String, Bid> unsortedBids= new HashMap<String, Bid>();
            for (Element e : getElementsByTagNameNR(getElementByTagNameNR(itemElm, "Bids"), "Bid")) {
                Element bidderElm = getElementByTagNameNR(e, "Bidder");
                String bidderId=bidderElm.getAttribute("UserID");
                String bidderRating=bidderElm.getAttribute("Rating");
                String bidderLocation = getElementTextByTagNameNR(bidderElm, "Location");
                if (bidderLocation.isEmpty())
                    bidderLocation = "";
                String bidderCountry = getElementTextByTagNameNR(bidderElm, "Country");
                if (bidderCountry.isEmpty())
                    bidderCountry = "";
                User bidBidder = new User(bidderId,bidderRating,bidderLocation,bidderCountry);
                String bidTime = formatTime(getElementTextByTagNameNR(e, "Time"));
                String bidAmount = strip(getElementTextByTagNameNR(e, "Amount"));
                Bid b = new Bid(bidBidder, bidTime, bidAmount);
                //this.bids.add(b);
                unsortedBids.put(bidTime,b);
            }
            this.sortedBids= new TreeMap<String, Bid>(unsortedBids);


            // System.out.println(itemId);  
            // System.out.println(name);  
            // System.out.println(started);  
            // System.out.println(ends);  
            // System.out.println(description); 
            // System.out.println(currently);  
            // System.out.println(buyPrice);  
            // System.out.println(firstBid);  
            // System.out.println(noOfBids);  
            // System.out.println(categories); 
            // System.out.println(location);  
            // System.out.println(latitude);  
            // System.out.println(longitude); 
            // System.out.println(seller.getUserId());  
            // System.out.println(seller.getRating()); 
            // printMap(sortedBids);

            // for (Bid b : bids){
            //     System.out.println(b.getTime());
            //     System.out.println(b.getAmount());
            //     System.out.println(b.getBidder().getUserId());
            //     System.out.println(b.getBidder().getRating());
            //     System.out.println(b.getBidder().getLocation());
            //     System.out.println(b.getBidder().getCountry());
            // }
        }

         catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static void printMap(Map<String, Bid> map) {
        for (Map.Entry<String, Bid> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() 
                                      + " Value : " + entry.getValue().getBidder().getUserId());
        }
    }


    // public static void main (String[] args) {
    //     /* Initialize parser. */
    //  //String myXML="<Item ItemID=\"1497595357\"><Name>PINK FLOYD~Syd Barretts First Trip~SEALED~dvd</Name><Category>DVD</Category><Category>Movies &amp; Television</Category><Category>Music &amp; Concert</Category><Category>Video, Film</Category><Currently>$9.99</Currently><First_Bid>$9.99</First_Bid><Number_of_Bids>0</Number_of_Bids><Bids /><Location>PLEASE SEE MY OTHER AUCTIONS</Location><Country>USA</Country><Started>Dec-16-01 19:13:16</Started><Ends>Dec-23-01 19:13:16</Ends><Seller Rating=\"1024\" UserID=\"rarevideo4sale\" /><Description>YYY</Description></Item>";
    //  //String myXML="<Item ItemID=\"1043495702\"><Name>Precious Moments Fig-ANGEL OF MERCY- NURSE</Name><Category>Enesco</Category><Category>Precious Moments</Category><Currently>$28.00</Currently><First_Bid>$9.99</First_Bid><Number_of_Bids>6</Number_of_Bids><Bids><Bid><Bidder Rating=\"427\" UserID=\"nobody138\"><Location>GOD BLESS AMERICA, FROM SOUTH, MS</Location><Country>USA</Country></Bidder><Time>Dec-04-01 23:20:07</Time><Amount>$12.99</Amount></Bid><Bid><Bidder Rating=\"92\" UserID=\"mrwvh\"><Location>Bryan, TX</Location><Country>USA</Country></Bidder><Time>Dec-09-01 10:00:07</Time><Amount>$25.00</Amount></Bid><Bid><Bidder Rating=\"1\" UserID=\"danielhb2000\"><Location>Huntington Beach, Ca.</Location><Country>USA</Country></Bidder><Time>Dec-06-01 02:00:07</Time><Amount>$15.99</Amount></Bid></Bids><Location Latitude=\"38.638318\" Longitude=\"-90.427118\">Missouri The Show Me State</Location><Country>USA</Country><Started>Dec-03-01 20:40:07</Started><Ends>Dec-13-01 20:40:07</Ends><Seller Rating=\"813\" UserID=\"lwm123\" /><Description>Q</Description></Item>";

    //  Item newone= new Item(myXML);
    // }


}