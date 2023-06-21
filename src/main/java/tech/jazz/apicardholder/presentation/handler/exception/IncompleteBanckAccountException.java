package tech.jazz.apicardholder.presentation.handler.exception;

public class IncompleteBanckAccountException extends RuntimeException {
    public IncompleteBanckAccountException(String message) {
        super(message);
    }
}
