package com.wsiiz.repairshop.foundation.ui.i18n;

import static com.wsiiz.repairshop.foundation.ui.i18n.I18nHandler.i18nHandler;

public interface I18nAware {

  default String i18ng(String key, Object... params) {
    return i18nHandler.getMessage(key, params);
  }

  default String i18n(Class callerClass, String key, Object... params) {
    return i18ng(callerClass.getName() + "." + key, params);
  }

  default String i18n(String key, Object... params) {
    return i18n(getClass(), key, params);
  }

}
