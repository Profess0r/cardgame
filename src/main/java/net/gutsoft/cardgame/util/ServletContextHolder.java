package net.gutsoft.cardgame.util;

import javax.servlet.ServletContext;

public class ServletContextHolder {
    private static ServletContext servletContext;

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static void setServletContext(ServletContext servletCont) {
        servletContext = servletCont;
    }
}
