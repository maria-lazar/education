import net.ConcurrentServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ConcurrentServer server = null;
        try {
            LOGGER.info("creating application context");
            ApplicationContext context = new ClassPathXmlApplicationContext("showXML.xml");

            server = context.getBean(ConcurrentServer.class);
            server.start();
        } catch (Exception ignored) {
        }
    }
}
