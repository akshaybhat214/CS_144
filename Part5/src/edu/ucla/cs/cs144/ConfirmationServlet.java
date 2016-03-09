package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpSession;


public class ConfirmationServlet extends HttpServlet implements Servlet {
       
    public ConfirmationServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        if (session.isNew()) {
            request.setAttribute("invalid_session", "true");
            //session.invalidate();
        }
        else if(!request.isSecure())
        {
            request.setAttribute("invalid_session", "true");
        }
        else
        {
            String creditCard = (String) request.getParameter("creditCard");
            request.setAttribute("creditCard", creditCard); 
            Date currentDate = new Date();
            request.setAttribute("order_ts", currentDate); 

            String itemId = (String) request.getParameter("itemId");
            request.setAttribute("itemid", itemId);
            Map<String, PaymentInfo> buyPriceItems = (HashMap<String, PaymentInfo>) session.getAttribute("buyPriceItems");
            if (buyPriceItems == null || !buyPriceItems.containsKey(itemId)) {
                response.sendRedirect("/eBay/");
            return;
            }
            PaymentInfo payInfo = buyPriceItems.get(itemId);
            request.setAttribute("PaymentInfo", payInfo);

            request.setAttribute("p_info", buyPriceItems);
            request.setAttribute("invalid_session", "false");  
        } 

        request.getRequestDispatcher("/showConfirmation.jsp").forward(request, response);
    }

        protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }
}