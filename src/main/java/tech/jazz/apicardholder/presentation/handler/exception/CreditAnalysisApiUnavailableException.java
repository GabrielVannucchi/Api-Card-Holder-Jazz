package tech.jazz.apicardholder.presentation.handler.exception;

public class CreditAnalysisApiUnavailableException extends RuntimeException {
    public CreditAnalysisApiUnavailableException(String message) {
        super(message);
    }
}
