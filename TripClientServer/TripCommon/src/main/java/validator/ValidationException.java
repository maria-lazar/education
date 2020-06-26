package validator;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(RuntimeException ex) {
        super(ex.getMessage());
    }
}
