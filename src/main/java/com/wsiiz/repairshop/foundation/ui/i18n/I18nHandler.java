package com.wsiiz.repairshop.foundation.ui.i18n;

import jakarta.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.springframework.stereotype.Component;

@Component
public class I18nHandler {

  private static final Locale POLISH = Locale.forLanguageTag("pl");

  public static I18nHandler i18nHandler;

  public final List<Locale> supportedLocales = Collections
      .unmodifiableList(Arrays.asList(POLISH, Locale.ENGLISH));
  private final Locale defaultLocale = POLISH;
  private ResourceBundle resourceBundle;
  private Locale locale;

  @PostConstruct
  private void init() {
    init(defaultLocale);
    i18nHandler = this;
  }

  public void init(Locale locale) {
    Locale languageLocale = Locale.forLanguageTag(locale.getLanguage());
    if (this.supportedLocales.contains(languageLocale)) {
      this.locale = languageLocale;
    } else {
      this.locale = defaultLocale;
    }
    this.resourceBundle = ResourceBundle.getBundle("messages", this.locale, new UTF8Control());
  }

  public String getMessage(String key, Object...params) {
    String message = key;
    try {
      message = MessageFormat.format(this.resourceBundle.getString(key), params);
    } catch (MissingResourceException e) {
      message = String.format("** %s **", key);
    }
    return message;
  }

  public Locale getLocale() {
    return locale;
  }
}
