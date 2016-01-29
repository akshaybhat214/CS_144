/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

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


class MyParserPrint {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
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
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
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


    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);

        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        //org.w3c.dom.NodeList nList = doc.getElementsByTagName("Item");
        Element ElementArr[] = getElementsByTagNameNR(root, "Item");
            
        System.out.println("----------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!");

        try
        {
            PrintWriter userWriter = new PrintWriter("user-data.csv", "UTF-8");
            PrintWriter itemWriter = new PrintWriter("item-data.csv", "UTF-8");
            PrintWriter categoryWriter = new PrintWriter("category-data.csv", "UTF-8");
            PrintWriter bidWriter = new PrintWriter("bid-data.csv", "UTF-8");
            PrintWriter latlongWriter = new PrintWriter("latlong-data.csv", "UTF-8");
            PrintWriter locationWriter = new PrintWriter("location-data.csv", "UTF-8");

            for (int i = 0; i < ElementArr.length ; i++) {

                //Node nNode = nList.item(i);
                //Node nNode = ElementArr[i];

                String itemStr = ElementArr[i].getAttribute("ItemID");
                System.out.println("Id: " + itemStr);

                String nameStr= getElementTextByTagNameNR(ElementArr[i], "Name");
                System.out.println("Name: " + nameStr);
                String currentlyStr= strip(getElementTextByTagNameNR(ElementArr[i], "Currently"));
                System.out.println("Currently: " + currentlyStr);
                String buyPriceStr= strip(getElementTextByTagNameNR(ElementArr[i], "Buy_Price"));
                System.out.println("Buy_Price: " + buyPriceStr);
                if(buyPriceStr == ""){
                    buyPriceStr=null;
                }
                String firstBidStr= strip(getElementTextByTagNameNR(ElementArr[i], "First_Bid"));
                System.out.println("First_Bid: " + firstBidStr);
                String nobStr= getElementTextByTagNameNR(ElementArr[i], "Number_of_Bids");
                System.out.println("Number_of_Bids: " + nobStr);
                String countryStr= getElementTextByTagNameNR(ElementArr[i], "Country");
                System.out.println("Country: " + countryStr);
                String startedStr= formatTime(getElementTextByTagNameNR(ElementArr[i], "Started"));
                System.out.println("Started: " + startedStr);
                String endStr= formatTime(getElementTextByTagNameNR(ElementArr[i], "Ends"));
                System.out.println("Ends: " + endStr);

                Element locElement = getElementByTagNameNR(ElementArr[i], "Location");
                String locStr = getElementText(locElement);
                String latStr = locElement.getAttribute("Latitude");
                String longStr = locElement.getAttribute("Longitude");
                System.out.println("Location: " + locStr);
                System.out.println("Latitude: " + latStr);
                System.out.println("Longitude: " + longStr);

                        
                Element sellerElement = getElementByTagNameNR(ElementArr[i], "Seller");
                String UserID = sellerElement.getAttribute("UserID");
                String ratingStr = sellerElement.getAttribute("Rating");
                System.out.println("Seller UserID: " + UserID);
                System.out.println("Seller Rating: " + ratingStr);

                userWriter.println("\"" + UserID+"\"" + ","+ ratingStr); 
                locationWriter.println("\"" + UserID+"\"" + ","+ "\"" + locStr +"\"" +"," + "\""+ countryStr+"\""); 

                if (latStr!="" && longStr!="")
                    latlongWriter.println("\"" + UserID+"\"" + ","+ latStr +","+ longStr);//LatitudeLongitude/////


                Element catArr[] = getElementsByTagNameNR(ElementArr[i], "Category");
                for (int c = 0; c < catArr.length ; c++) {
                    String catStr = getElementText(catArr[c]);
                    System.out.println("Category : " + catStr);
                    categoryWriter.println(itemStr + "," +"\"" + catStr+"\"");   //CATEGORY////////

                }

                Element bidsElement = getElementByTagNameNR(ElementArr[i], "Bids");
                Element bidArr[] = getElementsByTagNameNR(bidsElement, "Bid");
                for (int c = 0; c < bidArr.length ; c++) {
                    Element bidderElement = getElementByTagNameNR(bidArr[c], "Bidder");
                    String BidderID = bidderElement.getAttribute("UserID");
                    String bidderRatingStr = bidderElement.getAttribute("Rating");
                    String bidderLocationStr= getElementTextByTagNameNR(bidderElement, "Location");
                    String bidderCountryStr= getElementTextByTagNameNR(bidderElement, "Country");
                    System.out.println("Bidder ID: " + BidderID);
                    System.out.println("Bidder Rating: " + bidderRatingStr);
                    System.out.println("Bidder Location: " + bidderLocationStr);
                    System.out.println("Bidder Country: " + bidderCountryStr);
                    if(bidderCountryStr == ""){
                        bidderCountryStr=null;
                    }
                    if(bidderLocationStr == ""){
                        bidderLocationStr=null;
                    }

                    String timeStr = formatTime(getElementTextByTagNameNR(bidArr[c], "Time"));
                    String amountStr = strip(getElementTextByTagNameNR(bidArr[c], "Amount"));
                    System.out.println("Bid Time: " + timeStr);
                    System.out.println("Bid Amount: " + amountStr);

                    bidWriter.println(itemStr + "," + "\""+ BidderID + "\""+ "," +timeStr+ "," +amountStr); //BIDS////////////////
                    userWriter.println("\"" + BidderID+"\"" + ","+ bidderRatingStr);                       //BIDDER USERS////////

                    if(locStr!= null || countryStr !=null)                          //BIDDER LOCATION(If either non-null)////////
                        locationWriter.println("\"" + BidderID+"\"" + ","+ "\"" + locStr +"\"" +"," + "\""+ countryStr+"\""); 

                }

                String fullDescription= getElementTextByTagNameNR(ElementArr[i], "Description");
                String description4000=fullDescription.substring(0, Math.min(fullDescription.length(), 4000)); 
                System.out.println("Ends: " + description4000);

                itemWriter.println(itemStr + "," + "\""+ nameStr + "\""+ "," +"\"" +UserID+ "\"" + "," + currentlyStr + "," + buyPriceStr + ","
                      +firstBidStr + "," + nobStr + "," + startedStr + ","+ endStr + ","  +"\"" + description4000 + "\""); //ITEMS//////////////////
            
                System.out.println("----------------------------");

            }

            userWriter.close();
            bidWriter.close();
            itemWriter.close();
            categoryWriter.close();
            latlongWriter.close();
            locationWriter.close();           
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


        /* Fill in code here (you will probably need to write auxiliary
            methods). */
     
        /**************************************************************/
        
       // recursiveDescent(doc, 0);
    }
    
    public static void recursiveDescent(Node n, int level) {
        // adjust indentation according to level
        for(int i=0; i<4*level; i++)
            System.out.print(" ");
        
        // dump out node name, type, and value  
        String ntype = typeName[n.getNodeType()];
        String nname = n.getNodeName();
        String nvalue = n.getNodeValue();
        
        System.out.println("Type = " + ntype + ", Name = " + nname + ", Value = " + nvalue);
        
        // dump out attributes if any
        org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
        if(nattrib != null && nattrib.getLength() > 0)
            for(int i=0; i<nattrib.getLength(); i++)
                recursiveDescent(nattrib.item(i),  level+1);
        
        // now walk through its children list
        org.w3c.dom.NodeList nlist = n.getChildNodes();
        
        for(int i=0; i<nlist.getLength(); i++)
            recursiveDescent(nlist.item(i), level+1);
    }  
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
