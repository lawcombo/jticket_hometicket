package com.bluecom.common.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class ScriptUtils {

	public static void init(HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    }
 
    public static void alert(HttpServletResponse response, String alertText) throws Exception {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertText + "');</script> ");
        out.flush();
    }
 
    public static void alertAndMovePage(HttpServletResponse response, String alertText, String nextPage) throws Exception {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertText + "'); location.href='" + nextPage + "';</script> ");
        out.flush();
    }
 
    public static void alertAndBackPage(HttpServletResponse response, String alertText) throws Exception {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertText + "'); history.go(-1);</script>");
        out.flush();
    }


    public static void alertAndClose(HttpServletResponse response, String alertText) throws Exception {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertText + "');window.close();</script> ");
        out.flush();
    }	
}
