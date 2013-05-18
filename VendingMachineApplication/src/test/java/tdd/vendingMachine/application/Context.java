package tdd.vendingMachine.application;

import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;

public class Context {

    public static ApplicationContext get(HandlerWrapper server){
        
        WebAppContext handler = (WebAppContext) server.getHandler();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(handler.getServletContext(), FrameworkServlet.SERVLET_CONTEXT_PREFIX + "SpringDispatcher");
        
        return context;
    }
    
}