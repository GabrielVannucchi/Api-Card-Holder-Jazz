package tech.jazz.apicardholder.presentation.handler;

import feign.FeignException;
import feign.RetryableException;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.LocalDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.jazz.apicardholder.presentation.handler.exception.CardHolderNotFoundException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.DivergentCreditAnalysisAndClientException;
import tech.jazz.apicardholder.presentation.handler.exception.InactiveCardHolderException;
import tech.jazz.apicardholder.presentation.handler.exception.InsufficientLimitException;
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

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ProblemDetail> handlerFeignException(FeignException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.valueOf(e.status()), e.getClass().getSimpleName(),
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<ProblemDetail> handlerRetryableException(RetryableException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(StatusOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> handlerStatusOutOfFormatException(StatusOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CardHolderNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerCardHolderNotFoundException(CardHolderNotFoundException e) {

        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DivergentCardHolderException.class)
    public ResponseEntity<ProblemDetail> handlerDivergentCardHolderException(DivergentCardHolderException e) {

        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InsufficientLimitException.class)
    public ResponseEntity<ProblemDetail> handlerInsufficientLimitException(InsufficientLimitException e) {

        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InactiveCardHolderException.class)
    public ResponseEntity<ProblemDetail> handlerInactiveCardHolderException(InactiveCardHolderException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }



}
