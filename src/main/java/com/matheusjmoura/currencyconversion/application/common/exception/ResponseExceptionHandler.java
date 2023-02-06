package com.matheusjmoura.currencyconversion.application.common.exception;

import com.matheusjmoura.currencyconversion.exception.common.BusinessException;
import com.matheusjmoura.currencyconversion.exception.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BodyErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception, Locale locale) {
        BodyErrorResponse bodyErrorResponse = new BodyErrorResponse(messageSource.getMessage("validation.exception.title", null, locale));
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        for (ObjectError error : errors) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                String message = messageSource.getMessage(fieldError.getCode(), fieldError.getArguments(), fieldError.getDefaultMessage(), locale);
                bodyErrorResponse.addDetail(String.format("%s: %s", fieldError.getField(), message));
            } else {
                bodyErrorResponse.addDetail(messageSource.getMessage(error.getCode(), error.getArguments(), locale));
            }
        }
        return new ResponseEntity<>(bodyErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BodyErrorResponse> handleBusinessExceptions(BusinessException exception, Locale locale) {
        return new ResponseEntity<>(handleExceptionMessage("business.exception.title", exception, locale), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BodyErrorResponse> handleNotFoundExceptions(NotFoundException exception, Locale locale) {
        return new ResponseEntity<>(handleExceptionMessage("notFound.exception.title", exception, locale), HttpStatus.NOT_FOUND);
    }

    private BodyErrorResponse handleExceptionMessage(String messageKey, BusinessException exception, Locale locale) {
        BodyErrorResponse bodyErrorResponse = new BodyErrorResponse(messageSource.getMessage(messageKey, null, locale));
        bodyErrorResponse.addDetail(messageSource.getMessage(exception.getMessageKey(), exception.getArgs(), locale));
        return bodyErrorResponse;
    }

}
