package com.reba.personcrudapi.model.validator.util;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(\\S+)$");
    private static final Pattern WEB_SITE_PATTERN = Pattern.compile("^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_+.~#?&/=]*)$");
    private static final Pattern PIGEON_MESSENGER_PATTERN = Pattern.compile("^[0-9]+(-[0-9]+)+$");

    private ValidationUtils(){}

    public static boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidWebSite(String webSite) {
        return WEB_SITE_PATTERN.matcher(webSite).matches();
    }

    public static boolean isValidPigeonMessenger(String pigeonMessenger) {
        return PIGEON_MESSENGER_PATTERN.matcher(pigeonMessenger).matches();
    }
}
