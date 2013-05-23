package tdd.junit;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class EmbeddedServer {
    
    private Server server;

    public EmbeddedServer(Integer port) {
        server = new Server(port);
        WebAppContext context = new WebAppContext();
        context.setResourceBase("./src/main/webapp");
        context.setDescriptor("./WEB-INF/web.xml");
        server.setHandler(context);
    }

    public void start() {
        
        System.err.println("-------------- Starting Embedded Server for Integration Tests...");
        
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void awaitShutdown() {
        try {
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {

        System.err.println("-------------- Stopping Embedded Server for Integration Tests...");
        
        try {
            server.stop();
            awaitShutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
