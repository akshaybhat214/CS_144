package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URL;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	BufferedReader reader =null;
    	PrintWriter writer = response.getWriter();
        try
        {
        	response.setContentType("text/xml");
        	String curr_query = request.getParameter("query");
        	if(curr_query==null)
        		return;

        	URL suggestURL= new URL("http://google.com/complete/search?output=toolbar&q="+curr_query);

        	HttpURLConnection url_conn = (HttpURLConnection) suggestURL.openConnection();

        	reader = new BufferedReader(new InputStreamReader(url_conn.getInputStream()));
			StringBuilder builder = new StringBuilder();

			String aux = "";
			while ((aux = reader.readLine()) != null) {
			    builder.append(aux+ '\n');
			}
			String text = builder.toString();
			writer.println(text);

        	//request.setAttribute("temp_xml", text);
        	//request.setAttribute("temp_dummy", "blah");
        	reader.close();

        }catch (Exception ex){
        	ex.printStackTrace();
        }
    }
}
