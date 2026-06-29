package com.eagle_bank_api.exception;

import com.eagle_bank_api.model.dto.BadRequestErrorResponseDto;
import com.eagle_bank_api.model.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        var details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new BadRequestErrorResponseDto.Detail(
                        fe.getField(),
                        fe.getDefaultMessage(),
                        "validation"))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BadRequestErrorResponseDto("Invalid details supplied", details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BadRequestErrorResponseDto> handleConstraint(ConstraintViolationException ex) {
        var details = ex.getConstraintViolations().stream()
                .map(cv -> new BadRequestErrorResponseDto.Detail(
                        cv.getPropertyPath().toString(),
                        cv.getMessage(),
                        "validation"))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BadRequestErrorResponseDto("Invalid details supplied", details));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponseDto> handleInsufficient(InsufficientFundsException ex) {
        return ResponseEntity.status(422)   //HTTP 422 Unprocessable Content
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("An unexpected error occurred"));
    }
}
