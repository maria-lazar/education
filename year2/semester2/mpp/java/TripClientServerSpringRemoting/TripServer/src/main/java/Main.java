import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            LOGGER.info("creating application context");
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:tripXML.xml");
        } catch (Exception e) {
            LOGGER.warn("creating application context failed");
        }
    }
}
