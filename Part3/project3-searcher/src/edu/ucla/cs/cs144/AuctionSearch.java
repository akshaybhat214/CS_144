package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.SearchEngine;

import edu.ucla.cs.cs144.XmlDataString;

public class AuctionSearch implements IAuctionSearch {

    /* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
     * You may create helper functions or classes to simplify writing these
     * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
    
    public SearchResult[] basicSearch(String query, int numResultsToSkip, 
            int numResultsToReturn) {
            SearchResult[] ret=new SearchResult[numResultsToReturn]; 
            int added=0;
            try
            {
                SearchEngine se = new SearchEngine();
                int n = numResultsToReturn+numResultsToSkip;
                TopDocs topDocs = se.performSearch(query, n);
                System.out.println("Results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;
                for (int i = 0; i < hits.length; i++) 
                {
                    Document doc = se.getDocument(hits[i].doc);
                    if (i >= numResultsToSkip && added <numResultsToReturn){
                        ret[added]=new SearchResult(doc.get("itemid"), doc.get("name"));
                        added++;
                    }
                    //System.out.println(doc.get("itemid")+ " " + doc.get("name"));
                }
                

            }catch(IOException | ParseException e){
                e.printStackTrace();
            }
            SearchResult[] final_ret = new SearchResult[added];
            System.arraycopy(ret, 0, final_ret, 0, added); 
            System.out.println("performSearch done");
            return final_ret;
        }


    public SearchResult[] spatialSearch(String query, SearchRegion region,
            int numResultsToSkip, int numResultsToReturn) {

        Connection conn = null;
        Statement s = null;
        ArrayList<SearchResult> spatialOutput = new ArrayList<SearchResult>();
        try 
        {
            conn = DbManager.getConnection(true);
            s = conn.createStatement();
            SearchResult[] basicSearchRes = basicSearch(query, numResultsToSkip , 200000);

            String coord_string=""; //Raw string from sql
            String[] trimmed_coord = new String[2]; // Strings of {lat, long}
            Double[] parsed_coord = new Double[2]; //Doubles of above coord
            String item_id="";
            int count =0; //Use this to make sure we skip/return correct number of results.

            for(SearchResult result : basicSearchRes) {
                item_id = result.getItemId();
                ResultSet rs = s.executeQuery("SELECT ItemId, astext(Coord) FROM ItemLatLong WHERE ItemId = "+ item_id);
                if( rs.next() )
                {
                    coord_string = rs.getString("astext(Coord)");
                    String coord_substring = coord_string.substring(6, coord_string.length()-1);
                    trimmed_coord = coord_substring.split(" ");
                    parsed_coord[0]= Double.parseDouble(trimmed_coord[0]);
                    parsed_coord[1]= Double.parseDouble(trimmed_coord[1]);
                    System.out.println("Latitude: " + parsed_coord[0] + " Longitude: " + parsed_coord[1]);
                    if (parsed_coord[0]>= region.getLx() && parsed_coord[0]<=region.getRx() 
                        && parsed_coord[1]>= region.getLy() && parsed_coord[1]<=region.getRy())
                    {
                        if(count >= numResultsToSkip && count < numResultsToReturn )
                            spatialOutput.add(result);

                        System.out.println(coord_string);
                        count++;
                    }
                }
            }

        }catch (SQLException ex){
            System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        }

        try{
            s.close();
            conn.close();           
        }catch(SQLException se){
            se.printStackTrace();
        }

        SearchResult[] returnResults = new SearchResult[spatialOutput.size()];
        returnResults = spatialOutput.toArray(returnResults);

        return returnResults;
    }

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		XmlDataString getXMLData = new XmlDataString(itemId);

        return getXMLData.getXML();
	}
	
	public String echo(String message) {
		return message;
	}

}
