package ubb.marial.tripsrest.tripserverrest.myjdbc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConfigurationProperties("db")
@Component
public class DatabaseProperties {
    static String url = "jdbc:mysql://localhost:3306/mpp_trips";
    static String user = "root";
    static String password = "mariaDaria1999";
//    private String url = "jdbc:mysql://localhost:3306/mpp_trips";
//    private String user = "root";
//    private String password = "mariaDaria1999";

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getUser() {
//        return user;
//    }
//
//    public void setUser(String user) {
//        this.user = user;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
