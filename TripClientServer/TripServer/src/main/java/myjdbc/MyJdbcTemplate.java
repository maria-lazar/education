package myjdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class MyJdbcTemplate {
    private static final Logger LOGGER = LogManager.getLogger(MyJdbcTemplate.class.getName());
    private Connection connection = null;

    private Properties prop;

    public MyJdbcTemplate(String properties) {
        try {
            this.prop = loadProperties(properties);
            try {
                LOGGER.info("loading jdbc Driver");
                Class.forName(prop.getProperty("jdbcDriver"));
            } catch (ClassNotFoundException e) {
                LOGGER.warn("loading jdbc driver failed " + e.getMessage());
                throw new MyJdbcException(e);
            }
            LOGGER.info("getting jdbc connection");
            connection = DriverManager.getConnection(prop.getProperty("dbUrl"), prop.getProperty("dbUser"), prop.getProperty("dbPassword"));
        } catch (Exception e) {
            LOGGER.warn("getting jdbc connection failed " + e.getMessage());
            throw new MyJdbcException(e);
        }
    }

    private Properties loadProperties(String properties) {
        Properties prop = new Properties();
        try (InputStream input = MyJdbcTemplate.class.getClassLoader().getResourceAsStream(properties)) {
            if (input == null) {
                throw new MyJdbcException("database config not found");
            }
            prop.load(input);
        } catch (IOException ex) {
            throw new MyJdbcException("database config file corrupted");
        }
        return prop;
    }


    public void closeConnection() {
        try {
            LOGGER.info("closing connection");
            connection.close();
        } catch (SQLException e) {
            LOGGER.warn("closing connection failed " + e.getMessage());
            throw new MyJdbcException(e);
        }
    }

    public <T> List<T> query(String sql, ResultSetExtractor<T> extractor) {
        List<T> list = new ArrayList<>();
        LOGGER.traceEntry("query {}", sql);
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    list.add(extractor.extractData(resultSet));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("query failed " + e.getMessage());
            throw new MyJdbcException(e);
        }
        LOGGER.traceExit();
        return list;
    }

    public int update(String sql) {
        LOGGER.traceEntry("update {}", sql);
        try (Statement statement = connection.createStatement()) {
            int lines = statement.executeUpdate(sql);
            LOGGER.traceExit(lines);
            return lines;
        } catch (SQLException e) {
            LOGGER.warn("update failed " + e.getMessage());
            throw new MyJdbcException(e);
        }
    }

    public int insert(String sql) {
        LOGGER.traceEntry("updateInsert {}", sql);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet != null && resultSet.next()) {
                    int id = resultSet.getInt(1);
                    LOGGER.traceExit(id);
                    return id;
                }
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.warn("updateInsert failed " + e.getMessage());
            throw new MyJdbcException(e);
        }
    }
}
