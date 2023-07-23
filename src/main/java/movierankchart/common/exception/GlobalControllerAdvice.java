package movierankchart.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest httpServletRequest) throws URISyntaxException {
        ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(new URI(errorCode.getType()), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), new URI(httpServletRequest.getRequestURI())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest httpServletRequest) throws URISyntaxException {
        ErrorCode errorCode = ErrorCode.MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(new URI(errorCode.getType()), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), new URI(httpServletRequest.getRequestURI())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e, HttpServletRequest httpServletRequest) throws URISyntaxException {
        ErrorCode errorCode = ErrorCode.NO_SUCH_ELEMENT_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(new URI(errorCode.getType()), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), new URI(httpServletRequest.getRequestURI())), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e, HttpServletRequest httpServletRequest) throws URISyntaxException {
        ErrorCode errorCode = ErrorCode.BIND_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(new URI(errorCode.getType()), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), new URI(httpServletRequest.getRequestURI())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBindException(IllegalArgumentException e, HttpServletRequest httpServletRequest) throws URISyntaxException {
        ErrorCode errorCode = ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(new URI(errorCode.getType()), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), new URI(httpServletRequest.getRequestURI())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, HttpServletRequest httpServletRequest) throws URISyntaxException {
        ErrorCode errorCode = ErrorCode.UNCAUGHT_RUNTIME_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(new URI(errorCode.getType()), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), new URI(httpServletRequest.getRequestURI())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest httpServletRequest) throws URISyntaxException {
        ErrorCode errorCode = ErrorCode.UNCAUGHT_EXCEPTION;
        return new ResponseEntity<>(new ErrorResponse(new URI(errorCode.getType()), errorCode.getTitle(), errorCode.getStatus(), e.getMessage(), new URI(httpServletRequest.getRequestURI())), HttpStatus.BAD_REQUEST);
    }
}
