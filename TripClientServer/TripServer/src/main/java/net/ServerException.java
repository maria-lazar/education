package net;

import java.io.IOException;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }

    public ServerException(Exception e) {
        super(e);
    }
}
