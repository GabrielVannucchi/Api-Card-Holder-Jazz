package tech.jazz.apicardholder.presentation.handler.exception;

public class InactiveCardHolderException extends RuntimeException {
    public InactiveCardHolderException(String message) {
        super(message);
    }
}
