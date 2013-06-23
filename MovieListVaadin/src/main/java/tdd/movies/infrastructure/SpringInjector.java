package tdd.movies.infrastructure;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.VaadinServlet;

public class SpringInjector {

    private WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext());

    public <T> T getBean(Class<T> beanClass) {
        
        return context.getBean(beanClass);
    }

    private ServletContext servletContext() {
        return VaadinServlet.getCurrent().getServletContext();
    }

}
