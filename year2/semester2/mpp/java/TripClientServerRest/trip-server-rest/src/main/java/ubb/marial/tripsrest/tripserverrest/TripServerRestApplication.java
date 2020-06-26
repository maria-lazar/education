package ubb.marial.tripsrest.tripserverrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ubb.marial.tripsrest.tripserverrest.myjdbc.DatabaseProperties;

@SpringBootApplication
//@EnableConfigurationProperties(DatabaseProperties.class)
public class TripServerRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripServerRestApplication.class, args);
    }

}
