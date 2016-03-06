package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	try
    	{
	        String searchQuery = request.getParameter("query");
	        Integer numResultsToSkip = new Integer(request.getParameter("skips"));
	        Integer numResultsToReturn = new Integer(request.getParameter("toReturn"));

	        SearchResult[] result = AuctionSearchClient.basicSearch(searchQuery, numResultsToSkip, numResultsToReturn);
	        int num= result.length;
	        request.setAttribute("numberof", num);
	        request.setAttribute("searchResults", result);
	        request.setAttribute("skips", numResultsToSkip);
	        request.setAttribute("q", searchQuery);
	        request.getRequestDispatcher("/showSearch.jsp").forward(request, response);
    	}catch (Exception ex){
    		ex.printStackTrace();
    	}

    }
}