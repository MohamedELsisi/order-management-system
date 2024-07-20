package com.qeema.engineering.exception;



import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<Object> handleResourceException(ResourceException ex, WebRequest request) {
        ErrorDetails errorDetails = getErrorDetails(ex , HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorDetails, HttpStatus.INSUFFICIENT_STORAGE);
    }

    private ErrorDetails getErrorDetails(Exception ex, HttpStatus status) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setCode(status.toString());
        errorDetails.setReason(ex.getMessage());
        errorDetails.setStatus(String.valueOf(status.value()));

        return errorDetails;
    }
}
