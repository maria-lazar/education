package service;

import java.io.IOException;

public class ServiceException extends RuntimeException {
    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(Exception e) {
        super(e);
    }
}
