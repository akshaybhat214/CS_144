package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String itemId = request.getParameter("itemid");
        String itemXML = AuctionSearchClient.getXMLDataForItemId(itemId);
        
        //Item Parser to be implemented
        // Currently justs prints out the ItemId
        //Item item = new Item(itemXML);
        //request.setAttribute("xml", itemXML);
        //request.setAttribute("xml", curr);

        request.setAttribute("id_att", itemId);
        request.getRequestDispatcher("/showItem.jsp").forward(request, response);
    }
}
