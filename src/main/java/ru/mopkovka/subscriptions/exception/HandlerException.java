package ru.mopkovka.subscriptions.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.mopkovka.subscriptions.util.DateMapper;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice("ru.mopkovka.subscriptions")
public class HandlerException {
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServiceException(ServiceException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .reason(e.getMessage())
                .message(e.getMessage())
                .timestamp(DateMapper.getExceptionTimestampString(LocalDateTime.now()))
                .build();

        log.debug("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .reason(e.getMessage())
                .message(e.getMessage())
                .timestamp(DateMapper.getExceptionTimestampString(LocalDateTime.now()))
                .build();

        log.debug("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errors = ex.getAllValidationResults().stream()
                .flatMap(validationResult -> validationResult.getResolvableErrors().stream())
                .map(error -> (DefaultMessageSourceResolvable) error)
                .collect(Collectors.toMap(
                        resolvableError -> extractFieldName(resolvableError),
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .reason("Произошла непредвиденная ошибка")
                .message(e.getMessage())
                .timestamp(DateMapper.getExceptionTimestampString(LocalDateTime.now()))
                .build();

        e.printStackTrace();

        log.debug("500 {}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }

    /*--------------------Вспомогательные методы--------------------*/
    private String extractFieldName(DefaultMessageSourceResolvable resolvableError) {
        String fullCode = resolvableError.getCodes()[0];
        return fullCode.substring(fullCode.lastIndexOf('.') + 1);
    }
}
