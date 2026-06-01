package com.github.dinomarlir.l10nadventure.file;

import com.github.dinomarlir.l10nadventure.L10n;

import java.util.Map;
import java.util.Optional;

/**
 * Represents a loaded language file and its translation entries.
 *
 * <p>Implementations should expose the language code and all raw translation keys and values.
 * Higher-level lookup and fallback logic is handled by {@link L10n}.</p>
 */
public interface LanguageFile {

    /**
     * Returns the language code of this file, for example {@code en} or {@code de}.
     *
     * @return the language code
     */
    String language();

    /**
     * Returns all raw translation values contained in this file.
     *
     * @return an immutable map of translation keys to message templates
     */
    Map<String, String> values();

    /**
     * Looks up a translation key in this file.
     *
     * @param key the translation key
     * @return the raw template, if present
     */
    default Optional<String> get(final String key) {
        return Optional.ofNullable(this.values().get(key));
    }

    /**
     * Checks whether a translation key exists in this file.
     *
     * @param key the translation key
     * @return {@code true} if the key is present
     */
    default boolean containsKey(final String key) {
        return this.values().containsKey(key);
    }
}


