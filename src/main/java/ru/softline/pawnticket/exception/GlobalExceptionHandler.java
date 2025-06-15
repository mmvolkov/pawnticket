package ru.softline.pawnticket.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex,
                                                              HttpServletRequest req) {
        HttpStatusCode code = ex.getStatusCode();
        ErrorResponse body = ErrorResponse.of(code, ex.getReason(), req.getRequestURI());
        return ResponseEntity.status(code.value()).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrity(DataIntegrityViolationException ex,
                                                         HttpServletRequest req) {
        String msg = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        ErrorResponse body = ErrorResponse.of(CONFLICT, msg, req.getRequestURI());
        return ResponseEntity.status(CONFLICT).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        ErrorResponse body = ErrorResponse.of(BAD_REQUEST, msg, req.getRequestURI());
        return ResponseEntity.status(BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleAuth(BadCredentialsException ex,
                                                    HttpServletRequest req) {
        ErrorResponse body = ErrorResponse.of(UNAUTHORIZED, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccess(AccessDeniedException ex,
                                                      HttpServletRequest req) {
        ErrorResponse body = ErrorResponse.of(FORBIDDEN, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(FORBIDDEN).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception", ex);
        ErrorResponse body = ErrorResponse.of(
                INTERNAL_SERVER_ERROR,
                "Internal error: " + ex.getClass().getSimpleName(),
                req.getRequestURI());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(body);
    }
}
