package tech.jazz.apicardholder.presentation.handler.exception;

public class UnapprovedCreditAnalysisException extends RuntimeException {
    public UnapprovedCreditAnalysisException(String message) {
        super(message);
    }
}
