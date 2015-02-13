package com.arsenyko.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author Arseny Kovalchuk
 *
 */
public class StatsServlet extends HttpServlet {
    
    private static Logger log = Logger.getLogger(StatsServlet.class.getName());

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("Header - %s", req.getHeader("X-AppEngine-Country")));
        }
        res.setContentType("text/plain");
        res.getWriter().println("Hi, It's me!");
    }
    
}
