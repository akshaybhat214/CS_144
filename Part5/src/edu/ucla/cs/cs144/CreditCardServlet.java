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


public class CreditCardServlet extends HttpServlet implements Servlet {
       
    public CreditCardServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        
        if (session.isNew()) {
            response.sendRedirect("/eBay/");
            return;
        }

        String itemId = request.getParameter("itemid");
        Map<String, PaymentInfo> buyPriceItems = (HashMap<String, PaymentInfo>) session.getAttribute("buyPriceItems");
        if (buyPriceItems == null || !buyPriceItems.containsKey(itemId)) {
            response.sendRedirect("/eBay/");
            return;
        }

        PaymentInfo payInfo = buyPriceItems.get(itemId);
        request.setAttribute("PaymentInfo", payInfo);
        session.setAttribute("port_no", request.getServerPort());  
        request.getRequestDispatcher("/showCreditCardInput.jsp").forward(request, response);
    }
}