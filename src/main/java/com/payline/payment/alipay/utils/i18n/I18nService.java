package com.payline.payment.alipay.utils.i18n;

import com.payline.payment.alipay.utils.properties.ConfigProperties;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * I18n (for Internationalization) service that provides messages following a given locale.
 */
public class I18nService {

    private static final Logger LOGGER = LogManager.getLogger(I18nService.class);
    private static final String DEFAULT_LOCALE = "en";

    I18nService() {
        String defaultLocale = ConfigProperties.getInstance().get("i18n.defaultLocale");
        Locale.setDefault( new Locale(defaultLocale != null ? defaultLocale : DEFAULT_LOCALE) );
    }

    private static class SingletonHolder {
        private static final I18nService instance = new I18nService();
    }

    public static I18nService getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Retrieve the message identified by the given key in the language of the given locale.
     *
     * @param key The identifying key of the message
     * @param locale The locale
     * @return The message in the right language
     */
    public String getMessage(final String key, final Locale locale) {
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        try {
            return messages.getString(key);
        }
        catch (MissingResourceException e) {
            LOGGER.error("Trying to get a message with a key that does not exist: {} (language: {})", key, locale.getLanguage());
            return "???" + locale + "." + key + "???";
        }
    }
}