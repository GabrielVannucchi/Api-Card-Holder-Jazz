package tech.jazz.apicardholder.presentation.handler.exception;

public class BankAccountInvalidDataException extends RuntimeException {
    public BankAccountInvalidDataException(String message) {
        super(message);
    }
}
