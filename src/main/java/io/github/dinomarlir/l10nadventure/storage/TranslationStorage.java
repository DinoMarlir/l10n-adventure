package io.github.dinomarlir.l10nadventure.storage;

import io.github.dinomarlir.l10nadventure.file.LanguageFile;

import java.util.Optional;
import java.util.Set;

/**
 * Abstraction for where language files come from.
 *
 * <p>Implementations can load translations from resource bundles, files, databases or any other
 * backing store.</p>
 */
public interface TranslationStorage {

    /**
     * Returns the set of languages available from this storage.
     *
     * @return the available language codes
     */
    Set<String> availableLanguages();

    /**
     * Loads the language file for the given language code.
     *
     * @param language the language code to load
     * @return the loaded language file, if present
     */
    Optional<LanguageFile> getLanguageFile(String language);
}


