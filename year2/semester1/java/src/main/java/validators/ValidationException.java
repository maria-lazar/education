package validators;

public class ValidationException extends RuntimeException {
    private final String message;

    public ValidationException(String message) {
        this.message = message;
    }

    /**
     * Returns the ValidationException's message
     * @return the value of message
     */
    @Override
    public String getMessage() {
        return message;
    }
}
