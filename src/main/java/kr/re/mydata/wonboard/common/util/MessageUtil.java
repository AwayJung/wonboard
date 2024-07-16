package kr.re.mydata.wonboard.common.util;

import org.springframework.context.support.MessageSourceAccessor;

import java.util.Locale;

/**
 * Message 처리 유틸
 *
 * @author yrlee@mydata.re.kr
 */
public class MessageUtil {

    private static MessageSourceAccessor messageSourceAccessor = null;

    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        MessageUtil.messageSourceAccessor = messageSourceAccessor;
    }

    public static String getMessage(String key) {
        return messageSourceAccessor.getMessage(key);
    }

    public static String getMessage(String key, Object[] obj) {
        return messageSourceAccessor.getMessage(key, obj);
    }

    public static String getMessage(String key, Object[] obj, Locale locale) {
        return messageSourceAccessor.getMessage(key, obj, locale.getLanguage());
    }
    
    public static String getMessage(String key, Locale locale) {
        return messageSourceAccessor.getMessage(key, null, locale.getLanguage());
    }
}
