package io.github.dinomarlir.l10nadventure.file;

import java.io.InputStream;

/**
 * Creates {@link LanguageFile} instances from raw language resources.
 */
@FunctionalInterface
public interface LanguageFileFactory {

    /**
     * Creates a language file for the given language code.
     *
     * @param language the language code, for example {@code en_US}
     * @param inputStream the raw language content
     * @return a parsed language file implementation
     */
    LanguageFile from(String language, InputStream inputStream);
}

