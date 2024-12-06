package tech.jazz.apicardholder.presentation.handler.exception;

public class CardHolderNotFoundException extends RuntimeException {
    public CardHolderNotFoundException(String message) {
        super(message);
    }
}
