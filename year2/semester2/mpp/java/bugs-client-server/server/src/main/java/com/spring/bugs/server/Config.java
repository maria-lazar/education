package com.spring.bugs.server;

import com.spring.bugs.server.service.BugsServices;
import com.spring.bugs.server.service.BugsServicesImpl;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.remoting.support.RemoteExporter;

import javax.sql.DataSource;

@Configuration
public class Config {
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/iss_bugs");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("mariaDaria1999");
        return dataSourceBuilder.build();
    }
    @Bean
    RemoteExporter registerRMIExporter(BugsServicesImpl bugsServices) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("BugsServices");
        exporter.setServiceInterface(BugsServices.class);
        exporter.setService(bugsServices);
        return exporter;
    }
}

