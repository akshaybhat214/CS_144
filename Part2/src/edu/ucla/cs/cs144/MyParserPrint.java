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

        org.w3c.dom.NodeList nList = doc.getElementsByTagName("Item");
            
        System.out.println("----------------------------");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);
                    
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
                    
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                String str="";

                str=eElement.getAttribute("ItemID");
                System.out.println("Id: " + str);
                str=eElement.getElementsByTagName("Name").item(0).getTextContent();
                System.out.println("Name: " + str);
                str=eElement.getElementsByTagName("Currently").item(0).getTextContent();
                System.out.println("Currently: " + str);

                org.w3c.dom.NodeList nodeL=eElement.getElementsByTagName("Buy_Price");
                if (nodeL.getLength() !=0){
                    str=nodeL.item(0).getTextContent();
                    System.out.println("Buy_Price: " + str);
                }

                str=eElement.getElementsByTagName("First_Bid").item(0).getTextContent();
                System.out.println("First_Bid: " + str);
                str=eElement.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
                System.out.println("Number_of_Bids: " + str);
                str=eElement.getElementsByTagName("Country").item(0).getTextContent();
                System.out.println("Country: " + str);
                str=eElement.getElementsByTagName("Started").item(0).getTextContent();
                System.out.println("Started: " + str);
                str=eElement.getElementsByTagName("Ends").item(0).getTextContent();
                System.out.println("Ends: " + str);
                Element locElement = (Element) eElement.getElementsByTagName("Location").item(0);
                str=locElement.getTextContent();
                System.out.println("Location: " + str);
                str=locElement.getAttribute("Latitude");
                System.out.println("Latitude: " + str);
                str=locElement.getAttribute("Latitude");
                System.out.println("Longitude: " + str);
                Element sellerElement = (Element) eElement.getElementsByTagName("Seller").item(0);
                str=sellerElement.getAttribute("Rating");
                System.out.println("Seller Rating: " + str);
                str=sellerElement.getAttribute("UserID");
                System.out.println("Seller ID: " + str);

                org.w3c.dom.NodeList catList = eElement.getElementsByTagName("Category");
                for (int c = 0; c < catList.getLength(); c++) {
                    Node catNode = catList.item(c);
                    if (catNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element catElement = (Element) catNode;
                        str=catElement.getTextContent();
                        System.out.println("Category : " + str);
                    }
                }

                Element bidsElement = (Element) eElement.getElementsByTagName("Bids").item(0);
                org.w3c.dom.NodeList bidList = bidsElement.getElementsByTagName("Bid");
                for (int c = 0; c < bidList.getLength(); c++) {
                    Node bidNode = bidList.item(c);
                    if (bidNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element bidElement = (Element) bidNode;

                        Element bidderElement = (Element) bidElement.getElementsByTagName("Bidder").item(0);
                        str=bidderElement.getAttribute("UserID");
                        System.out.println("Bidder ID: " + str);
                        str=bidderElement.getAttribute("Rating");
                        System.out.println("Bidder Rating: " + str);
                        
                        org.w3c.dom.NodeList bidderNodeL=bidderElement.getElementsByTagName("Location");
                        if (bidderNodeL.getLength() !=0){
                            str=bidderNodeL.item(0).getTextContent();
                            System.out.println("Bidder Location: " + str);
                        }
                        bidderNodeL=bidderElement.getElementsByTagName("Country");
                        if (bidderNodeL.getLength() !=0){
                            str=bidderNodeL.item(0).getTextContent();
                            System.out.println("Bidder Country: " + str);
                        }
                        
                        str=bidElement.getElementsByTagName("Time").item(0).getTextContent();
                        System.out.println("Bid Time: " + str);
                        str=bidElement.getElementsByTagName("Amount").item(0).getTextContent();
                        System.out.println("Bid Amount: " + str);


                    }
                }
            }
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
