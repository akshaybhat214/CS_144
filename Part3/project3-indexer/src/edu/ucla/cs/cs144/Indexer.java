package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index-directory50"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }    
   
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    public void rebuildIndexes() throws IOException {

        Connection conn = null;
        getIndexWriter(false); //changed
          // create a connection to the database to retrieve Items from MySQL
    try {
      System.out.println("here");
        conn = DbManager.getConnection(true);
        Statement s = conn.createStatement() ;
        ResultSet rs = s.executeQuery("SELECT ItemId, Name, Description FROM Items");
        String cur_itemId, cur_name, cur_description;
            while( rs.next() )
            {
              //System.out.println("here1");
                    Document doc = new Document();

                    /*Get string values of fields from SQL query results*/
                    cur_itemId = rs.getString("ItemId");
                    cur_name = rs.getString("Name");
                    cur_description =rs.getString("Description");
                    String categoryString = getCategoryList(conn, cur_itemId);
                    String fullSearchableText = (cur_name +" "+ categoryString+" "+ cur_description);
                    //System.out.println(fullSearchableText);

                    /*Add fields to the document before writing*/
                    doc.add(new StringField("itemid", cur_itemId, Field.Store.YES));
                    doc.add(new TextField("name", cur_name, Field.Store.YES));
                    doc.add(new TextField("description", cur_description, Field.Store.NO));
                    doc.add(new TextField("categories", categoryString, Field.Store.NO));
                    doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
                    indexWriter.addDocument(doc);
                    
                    //System.out.println(itemId + " has a name: " + description);
            }
        rs.close();
        s.close();
        conn.close();
        //System.out.println("Hellooo");

    } catch (SQLException ex){
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
       }


    /*
     * Add your code here to retrieve Items using the connection
     * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
     * 
     */

        // close the database connection
        closeIndexWriter();      

    }    

    public static void main(String args[]) {

    try {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
        } catch (Exception e) {
            System.out.println("Exception caught:"+ e.getMessage());
        }
    }

    private static String getCategoryList(Connection conn, String itemId) {
        String catList="";

        try {
            Statement s2 = conn.createStatement();
            ResultSet rs = s2.executeQuery("SELECT Category FROM Categories WHERE ItemId ="+ itemId);
              while(rs.next())
              {

                  catList +=" ";
                  catList += rs.getString("Category");
              }
            rs.close();
            s2.close();

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }

        //  System.out.println(catList+ "\n");
        return catList;
    }       
}