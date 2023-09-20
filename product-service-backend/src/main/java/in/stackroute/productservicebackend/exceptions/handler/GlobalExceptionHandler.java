package in.stackroute.productservicebackend.exceptions.handler;

import in.stackroute.productservicebackend.exceptions.ProductNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     *
     * @param ex the exception to handle
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance with the response body as {@link ApiError}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles ProductNotFoundException. Created to encapsulate errors with more detail than
     * @param ex the ProductNotFoundException
     * @return a {@code ResponseEntity} instance with the response body as {@link ApiError}
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles ProductAlreadyExistsException. Created to encapsulate errors with more detail.
     * @param ex the exception to handle
     * @param headers the headers to use for the response
     * @param status the status code to use for the response
     * @param request the current request
     * @return a {@code ResponseEntity} instance with the response body as {@link ApiError}
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    /**
     * @param ex the exception to handle
     * @return a {@code ResponseEntity} instance with the response body as {@link ApiError}
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

}
