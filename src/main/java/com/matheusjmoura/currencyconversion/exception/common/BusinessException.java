package com.matheusjmoura.currencyconversion.exception.common;

import lombok.Getter;

import java.util.ResourceBundle;

@Getter
public class BusinessException extends RuntimeException {

    private final Object[] args;
    private final String messageKey;

    public BusinessException(String messageKey) {
        super(getMessage(messageKey, new Object[]{}));
        this.messageKey = messageKey;
        this.args = new Object[]{};
    }

    public BusinessException(String messageKey, Object[] args) {
        super(getMessage(messageKey, args));
        this.messageKey = messageKey;
        this.args = args;
    }

    private static String getMessage(String messageKey, Object[] args) {
        String string = ResourceBundle.getBundle("messages/messages").getString(messageKey);
        for (int i = 0; i < args.length; i++) {
            string = string.replace("{" + i + "}", args[i].toString());
        }
        return string;
    }

}
