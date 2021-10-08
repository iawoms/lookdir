package look.web;

import look.web.ws.Ws_Sv;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * main
 */
public class WebServer {


    public static String indexPage = "/index.html";
    public static int port = 809;

    public static void startServ() throws Exception {
        Server server = new Server(port);
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(false);
        resource_handler.setWelcomeFiles(new String[]{indexPage});
        resource_handler.setResourceBase("./");

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        SessionHandler sesHdr = new SessionHandler();
        sesHdr.setMaxInactiveInterval(60 * 20);
        handler.setSessionHandler(sesHdr);

//        handler.addServlet(Login_Sv.class, "/login");
        handler.addServlet(Ws_Sv.class, "/ws");
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, handler, new DefaultHandler()});
        server.setHandler(handlers);

        server.start();
        System.out.println("http://127.0.0.1:" + port);
        server.join();
    }
    public static void main(String[] args) throws Exception {
       startServ();
    }
}
