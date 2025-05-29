package com.zetta.forex.config;

import com.zetta.forex.model.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LocalDateTimeProvider localDateTimeProvider;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(ConstraintViolationException ex,
                                                                       WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        List<String> errors = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());

        ErrorResponseDto errorResponseDto = new ErrorResponseDto()
                .setTimestamp(localDateTimeProvider.getTime())
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setError("Validation failed")
                .setDetails(errors)
                .setPath(path);

        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingParams(MissingServletRequestParameterException ex,
                                                                WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto()
                .setTimestamp(localDateTimeProvider.getTime())
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setError("Missing required parameter")
                .setDetails(List.of(String.format("Parameter '%s' of type %s is required",
                        ex.getParameterName(), ex.getParameterType())))
                .setPath(path);

        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoResourceFound(NoResourceFoundException ex,
                                                                  WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto()
                .setTimestamp(localDateTimeProvider.getTime())
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setError("Resource not found")
                .setDetails(List.of(String.format("Path '%s' was not found", ex.getResourcePath())))
                .setPath(path);

        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentTypeMismatchException ex,
                                                                       WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        String errorDetails = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(),
                ex.getName(),
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

        ErrorResponseDto errorResponseDto = new ErrorResponseDto()
                .setTimestamp(localDateTimeProvider.getTime())
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setError("Illegal Argument")
                .setDetails(List.of(errorDetails))
                .setPath(path);

        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}
