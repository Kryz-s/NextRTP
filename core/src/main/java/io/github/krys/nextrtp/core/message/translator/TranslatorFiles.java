package io.github.krys.nextrtp.core.message.translator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.krys.nextrtp.core.configuration.lang.LangConfiguration;

public final class TranslatorFiles {
  public static final TranslatorFiles INSTANCE = new TranslatorFiles();

  private final Map<Locale, LangConfiguration> langs = new HashMap<>();

  public LangConfiguration getLang(Locale locañe) {
    return this.langs.get(locañe);
  }

  public void add(Locale locale, LangConfiguration langConfiguration) {
    this.langs.put(locale, langConfiguration);
  }
}
