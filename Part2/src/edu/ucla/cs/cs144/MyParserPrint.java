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
            
        System.out.println("----------------------------");

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
                Node nNode = ElementArr[i];
                        
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                        
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element curElement = (Element) nNode;

                    String ItemID=curElement.getAttribute("ItemID");
                    System.out.println("Id: " + ItemID);

                    String nameStr=curElement.getElementsByTagName("Name").item(0).getTextContent();
                    System.out.println("Name: " + nameStr);
                    String currentlyStr=strip(curElement.getElementsByTagName("Currently").item(0).getTextContent());
                    System.out.println("Currently: " + currentlyStr);

                    String buyPriceStr= null;
                    org.w3c.dom.NodeList nodeL=curElement.getElementsByTagName("Buy_Price");
                    if (nodeL.getLength() !=0){
                        buyPriceStr=strip(nodeL.item(0).getTextContent()); //STRIP
                        System.out.println("Buy_Price: " + buyPriceStr);
                    }

                    String firstBidStr =strip(curElement.getElementsByTagName("First_Bid").item(0).getTextContent());
                    System.out.println("First_Bid: " + firstBidStr);
                    String nobStr= curElement.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
                    System.out.println("Number_of_Bids: " + nobStr);
                    String countryStr=curElement.getElementsByTagName("Country").item(0).getTextContent();
                    System.out.println("Country: " + countryStr);
                    String startedStr=formatTime(curElement.getElementsByTagName("Started").item(0).getTextContent());
                    System.out.println("Started: " + startedStr);
                    String endStr=formatTime(curElement.getElementsByTagName("Ends").item(0).getTextContent());
                    System.out.println("Ends: " + endStr);

                    Element locElement = (Element) curElement.getElementsByTagName("Location").item(0);

                    String locStr=locElement.getTextContent();
                    System.out.println("Location: " + locStr);
                    String latStr=null;
                    latStr = locElement.getAttribute("Latitude");
                    System.out.println("Latitude: " + latStr);
                    String longStr=null;
                    longStr = locElement.getAttribute("Latitude");
                    System.out.println("Longitude: " + longStr);

                    Element sellerElement = (Element) curElement.getElementsByTagName("Seller").item(0);
                    String ratingStr=sellerElement.getAttribute("Rating");
                    System.out.println("Seller Rating: " + ratingStr);
                    String UserID=sellerElement.getAttribute("UserID");
                    System.out.println("Seller ID: " + UserID);

                    userWriter.println("\"" + UserID+"\"" + ","+ ratingStr);   //USERS////////

                    locationWriter.println("\"" + UserID+"\"" + ","+ "\"" + locStr +"\"" +"," + "\""+ countryStr+"\""); 

                    if (latStr!=null && longStr!=null && latStr!="" && longStr!="")
                        latlongWriter.println("\"" + UserID+"\"" + ","+ latStr +","+ longStr);//LatitudeLongitude/////

                    String catStr = "";
                    org.w3c.dom.NodeList catList = curElement.getElementsByTagName("Category");
                    for (int c = 0; c < catList.getLength(); c++) {
                        Node catNode = catList.item(c);
                        if (catNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element catElement = (Element) catNode;
                            catStr=catElement.getTextContent();
                            System.out.println("Category : " + catStr);

                            categoryWriter.println(ItemID + "," +"\"" + catStr+"\"");   //CATEGORY////////
                        }
                    }

                    Element bidsElement = (Element) curElement.getElementsByTagName("Bids").item(0);
                    org.w3c.dom.NodeList bidList = bidsElement.getElementsByTagName("Bid");
                    for (int c = 0; c < bidList.getLength(); c++) {
                        Node bidNode = bidList.item(c);
                        if (bidNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element bidElement = (Element) bidNode;

                            Element bidderElement = (Element) bidElement.getElementsByTagName("Bidder").item(0);
                            String BidderID=bidderElement.getAttribute("UserID");
                            System.out.println("Bidder ID: " + BidderID);
                            String bidderRatingStr=bidderElement.getAttribute("Rating");
                            System.out.println("Bidder Rating: " + bidderRatingStr);
                            
                            String bidderLocationStr = null;
                            String bidderCountryStr = null;
                            org.w3c.dom.NodeList bidderNodeL=bidderElement.getElementsByTagName("Location");
                            if (bidderNodeL.getLength() !=0){
                                bidderLocationStr=bidderNodeL.item(0).getTextContent();
                                System.out.println("Bidder Location: " + bidderLocationStr);
                            }
                            bidderNodeL=bidderElement.getElementsByTagName("Country");
                            if (bidderNodeL.getLength() !=0){
                                bidderCountryStr=bidderNodeL.item(0).getTextContent();
                                System.out.println("Bidder Country: " + bidderCountryStr);
                            }
                            
                            String timeStr=formatTime(bidElement.getElementsByTagName("Time").item(0).getTextContent());
                            System.out.println("Bid Time: " + timeStr);
                            String amountStr=strip(bidElement.getElementsByTagName("Amount").item(0).getTextContent());
                            System.out.println("Bid Amount: " + amountStr);
                            
                            bidWriter.println(ItemID + "," + "\""+ BidderID + "\""+ "," +timeStr+ "," +amountStr); //BIDS////////////////
                            userWriter.println("\"" + BidderID+"\"" + ","+ bidderRatingStr);                       //BIDDER USERS////////

                            if(locStr!= null || countryStr !=null)                          //BIDDER LOCATION(If either non-null)////////
                                locationWriter.println("\"" + BidderID+"\"" + ","+ "\"" + locStr +"\"" +"," + "\""+ countryStr+"\""); 

                        }
                    }

                    Element descriptionElement = (Element) curElement.getElementsByTagName("Description").item(0);
                    String fullDescription = descriptionElement.getTextContent();
                    String description4000 = fullDescription.substring(0, Math.min(fullDescription.length(), 4000)); 
                    System.out.println("Description: " + description4000);


                    itemWriter.println(ItemID + "," + "\""+ nameStr + "\""+ "," +"\"" +UserID+ "\"" + "," + currentlyStr + "," + buyPriceStr + ","
                      +firstBidStr + "," + nobStr + "," + startedStr + ","+ endStr + ","  +"\"" + description4000 + "\""); //ITEMS//////////////////
                }
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
