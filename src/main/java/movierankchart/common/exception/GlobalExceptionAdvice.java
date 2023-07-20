package movierankchart.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest httpServletRequest) {
        ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(errorCode.getType(), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), httpServletRequest.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest httpServletRequest) {
        ErrorCode errorCode = ErrorCode.MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(errorCode.getType(), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), httpServletRequest.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e, HttpServletRequest httpServletRequest) {
        ErrorCode errorCode = ErrorCode.NO_SUCH_ELEMENT_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(errorCode.getType(), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), httpServletRequest.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e, HttpServletRequest httpServletRequest) {
        ErrorCode errorCode = ErrorCode.BIND_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(errorCode.getType(), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), httpServletRequest.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, HttpServletRequest httpServletRequest) {
        ErrorCode errorCode = ErrorCode.UNCAUGHT_RUNTIME_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(errorCode.getType(), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), httpServletRequest.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest httpServletRequest) {
        ErrorCode errorCode = ErrorCode.UNCAUGHT_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(errorCode.getType(), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), httpServletRequest.getRequestURI()), HttpStatus.BAD_REQUEST);
    }
}
