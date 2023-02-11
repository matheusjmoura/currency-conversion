package com.matheusjmoura.currencyconversion.exception.common;

import lombok.Getter;

import java.util.ResourceBundle;

@Getter
public class BusinessException extends RuntimeException {

    private static final String DEFAULT_EXCEPTION_TITLE = "business.exception.title";

    private final String title;
    private final Object[] args;
    private final String messageKey;

    public BusinessException(String messageKey, Object[] args) {
        super(getMessage(messageKey, args));
        this.title = DEFAULT_EXCEPTION_TITLE;
        this.args = args;
        this.messageKey = messageKey;
    }

    public BusinessException(String title, String messageKey) {
        super(messageKey);
        this.title = title;
        this.args = new Object[]{};
        this.messageKey = messageKey;
    }

    private static String getMessage(String messageKey, Object[] args) {
        String message = ResourceBundle.getBundle("messages/messages").getString(messageKey);
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i].toString());
        }
        return message;
    }

}
