package tech.jazz.apicardholder.presentation.handler.exception;

public class InvalidCardHolderRequestException extends RuntimeException {
    public InvalidCardHolderRequestException(String message) {
        super(message);
    }
}
