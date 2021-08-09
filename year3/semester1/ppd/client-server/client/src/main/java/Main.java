import dto.VanzareDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.ShowServices;

import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            int nr_locuri = 500;
            Integer[] idSpectacole = {1, 2, 3};

            ApplicationContext context = new ClassPathXmlApplicationContext("showXMLClient.xml");
            ShowServices showServices = context.getBean(ShowServices.class);

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    int sId = idSpectacole[new Random().nextInt((idSpectacole.length - 1) + 1)];
                    int nr_bilete = new Random().nextInt((nr_locuri / 10 - 1) + 1) + 1;
                    List<Integer> locuri = new ArrayList<>();
                    for (int i = 0; i < nr_bilete; i++) {
                        int nr_loc = new Random().nextInt((nr_locuri - 1) + 1) + 1;
                        while (locuri.contains(nr_loc)) {
                            nr_loc = new Random().nextInt((nr_locuri - 1) + 1) + 1;
                        }
                        locuri.add(nr_loc);
                    }
                    VanzareDto vanzare = new VanzareDto(sId, LocalDate.now().toString(), nr_bilete, locuri);
                    System.out.println(showServices.buy(vanzare));
                }
            }, 0, 2 * 1000);

        } catch (Exception e) {
        }
    }
}


