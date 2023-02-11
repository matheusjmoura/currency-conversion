package com.matheusjmoura.currencyconversion.configuration;

import com.matheusjmoura.currencyconversion.exception.common.BusinessException;
import com.matheusjmoura.currencyconversion.exception.common.ErrorAttributes;
import com.matheusjmoura.currencyconversion.exception.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorAttributes> handleValidationErrors(MethodArgumentNotValidException exception, Locale locale) {
        ErrorAttributes errorAttributes = new ErrorAttributes(messageSource.getMessage("validation.exception.title", null, locale));
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        for (ObjectError error : errors) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                String message = messageSource.getMessage(Objects.requireNonNull(fieldError.getCode()), fieldError.getArguments(),
                    fieldError.getDefaultMessage(), locale);
                errorAttributes.addDetail(String.format("%s: %s", fieldError.getField(), message));
            } else {
                errorAttributes.addDetail(messageSource.getMessage(Objects.requireNonNull(error.getCode()), error.getArguments(), locale));
            }
        }
        return new ResponseEntity<>(errorAttributes, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorAttributes> handleBusinessExceptions(BusinessException exception, Locale locale) {
        return new ResponseEntity<>(handleExceptionMessage(exception.getTitle(), exception, locale), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorAttributes> handleNotFoundExceptions(NotFoundException exception, Locale locale) {
        return new ResponseEntity<>(handleExceptionMessage("notFound.exception.title", exception, locale), HttpStatus.NOT_FOUND);
    }

    private ErrorAttributes handleExceptionMessage(String errorTitle, BusinessException exception, Locale locale) {
        ErrorAttributes errorAttributes = new ErrorAttributes(messageSource.getMessage(errorTitle, null, locale));
        try {
            errorAttributes.addDetail(messageSource.getMessage(exception.getMessageKey(), exception.getArgs(), locale));
        } catch (NoSuchMessageException ex) {
            errorAttributes.addDetail(exception.getMessageKey());
        }
        return errorAttributes;
    }

}
