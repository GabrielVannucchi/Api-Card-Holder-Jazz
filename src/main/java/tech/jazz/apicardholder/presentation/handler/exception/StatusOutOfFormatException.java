package tech.jazz.apicardholder.presentation.handler.exception;

public class StatusOutOfFormatException extends RuntimeException {
    public StatusOutOfFormatException(String message) {
        super(message);
    }
}
