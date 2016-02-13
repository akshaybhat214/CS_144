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
            try
            {
                SearchEngine se = new SearchEngine();
                int n=numResultsToReturn+numResultsToSkip;
                TopDocs topDocs = se.performSearch(query, n);
                int added=0;
                System.out.println("Results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;
                for (int i = 0; i < hits.length; i++) {
                    Document doc = se.getDocument(hits[i].doc);
                    if (i >= numResultsToSkip && added <numResultsToReturn){
                        ret[added]=new SearchResult(doc.get("itemid"), doc.get("name"));
                        added++;
                    }
                    System.out.println(doc.get("itemid")+ " " + doc.get("name"));
                }
            }catch(IOException | ParseException e){
                e.printStackTrace();
            }

            System.out.println("performSearch done");
            return ret;
        }



	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		return new SearchResult[0];
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
