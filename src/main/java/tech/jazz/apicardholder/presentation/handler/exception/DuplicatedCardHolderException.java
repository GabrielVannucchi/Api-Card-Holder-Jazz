package tech.jazz.apicardholder.presentation.handler.exception;

public class DuplicatedCardHolderException extends RuntimeException {
    public DuplicatedCardHolderException(String message) {
        super(message);
    }
}
