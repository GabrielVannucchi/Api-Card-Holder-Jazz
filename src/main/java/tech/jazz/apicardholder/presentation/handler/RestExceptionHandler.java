package tech.jazz.apicardholder.presentation.handler;

import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.jazz.apicardholder.presentation.handler.exception.BankAccountInvalidDataException;
import tech.jazz.apicardholder.presentation.handler.exception.CreditAnalysisApiUnavailableException;
import tech.jazz.apicardholder.presentation.handler.exception.CreditAnalysisNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCreditAnalysisAndClientException;
import tech.jazz.apicardholder.presentation.handler.exception.DuplicatedCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.IncompleteBanckAccountException;
import tech.jazz.apicardholder.presentation.handler.exception.InvalidCardHolderRequestException;
import tech.jazz.apicardholder.presentation.handler.exception.StatusOutOfFormatException;
import tech.jazz.apicardholder.presentation.handler.exception.UnapprovedCreditAnalysisException;

@RestControllerAdvice
public class RestExceptionHandler {
    private ProblemDetail problemDetailBuilder(HttpStatus status, String title, String message, Exception e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/" + status.value()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setTitle(title);
        problemDetail.setDetail(message);
        return problemDetail;
    }

    @ExceptionHandler(CreditAnalysisApiUnavailableException.class)
    public ResponseEntity<ProblemDetail> handlerCreditAnalysisApiUnavailableException(CreditAnalysisApiUnavailableException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.SERVICE_UNAVAILABLE, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CreditAnalysisNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerCreditAnalysisNotFoundException(CreditAnalysisNotFoundException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DivergentCreditAnalysisAndClientException.class)
    public ResponseEntity<ProblemDetail> handlerDivergentCreditAnalysisAndClientException(DivergentCreditAnalysisAndClientException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(IncompleteBanckAccountException.class)
    public ResponseEntity<ProblemDetail> handlerIncompleteBanckAccountException(IncompleteBanckAccountException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(BankAccountInvalidDataException.class)
    public ResponseEntity<ProblemDetail> handlerBankAccountInvalidDataException(BankAccountInvalidDataException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DuplicatedCardHolderException.class)
    public ResponseEntity<ProblemDetail> handlerDuplicatedCardHolderException(DuplicatedCardHolderException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.CONFLICT, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InvalidCardHolderRequestException.class)
    public ResponseEntity<ProblemDetail> handlerInvalidCardHolderRequestException(InvalidCardHolderRequestException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(UnapprovedCreditAnalysisException.class)
    public ResponseEntity<ProblemDetail> handlerUnapprovedCreditAnalysisException(UnapprovedCreditAnalysisException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handlerConstraintViolationException(ConstraintViolationException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(StatusOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> handlerStatusOutOfFormatException(StatusOutOfFormatException e) {

        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

}
