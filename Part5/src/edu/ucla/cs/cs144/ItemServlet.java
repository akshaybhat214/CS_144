package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;


public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String itemId = request.getParameter("itemid");
        String itemXML = AuctionSearchClient.getXMLDataForItemId(itemId);
        
        //Item Parser to be implemented
        // Currently justs prints out the ItemId
        Item item = new Item(itemXML);
         if (!item.getBuyPrice().isEmpty()){
            HttpSession session = request.getSession(true);
            PaymentInfo payInfo = new PaymentInfo(item.getItemId(), item.getName(), item.getBuyPrice());
            Map<String, PaymentInfo> buyPriceItems = null;
            if (session.isNew()) {
                buyPriceItems = new HashMap<String, PaymentInfo>();
            } 
            else {
                buyPriceItems = (HashMap<String, PaymentInfo>) session.getAttribute("buyPriceItems");
                if (buyPriceItems==null)
                    buyPriceItems = new HashMap<String, PaymentInfo>();  
            }
            buyPriceItems.put(item.getItemId(), payInfo);
            session.setAttribute("buyPriceItems", buyPriceItems);
         }
        request.setAttribute("id_att", itemId);
        request.setAttribute("item", item);

        request.getRequestDispatcher("/showItem.jsp").forward(request, response);
    }
}
  