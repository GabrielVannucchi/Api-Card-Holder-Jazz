package tech.jazz.apicardholder.presentation.handler.exception;

public class InsufficientLimitException extends RuntimeException {
    public InsufficientLimitException(String message) {
        super(message);
    }
}
