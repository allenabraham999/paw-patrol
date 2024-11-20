package com.simplogics.base.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class Translator {

    private static ResourceBundleMessageSource messageSource;

    @Autowired
    Translator(ResourceBundleMessageSource messageSource){
        Translator.messageSource = messageSource;
    }

    public static String translateToLocale(String messageCode){
        try {
            if (messageCode == null) {
                return "Message cannot be null! this should not happen!!!";
            }
            return messageSource.getMessage(messageCode, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return messageCode;
        }
    }
}
