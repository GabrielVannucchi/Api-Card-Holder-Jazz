package tech.jazz.apicardholder.presentation.handler.exception;

public class CreditAnalysisNotFoundException extends RuntimeException {
    public CreditAnalysisNotFoundException(String message) {
        super(message);
    }
}
