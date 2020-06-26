package myjdbc;

public class MyJdbcException extends RuntimeException {
    public MyJdbcException(Exception e) {
        super(e);
    }

    public MyJdbcException(String message) {
        super(message);
    }
}
