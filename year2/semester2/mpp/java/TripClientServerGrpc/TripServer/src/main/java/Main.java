import net.GrpcServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        GrpcServer server = null;
        try {
            LOGGER.info("creating application context");
            ApplicationContext context = new ClassPathXmlApplicationContext("tripXML.xml");
            server = context.getBean(GrpcServer.class);
            server.start();
            server.blockUntilShutdown();
        } catch (Exception e) {
            LOGGER.warn("creating application context failed " + e);
            e.printStackTrace();
        }
    }
}
