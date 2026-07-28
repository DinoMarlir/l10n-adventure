package io.github.dinomarlir.l10nadventure.file;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Common {@link LanguageFileFactory} helpers.
 */
public final class LanguageFileFactories {

    private LanguageFileFactories() {
    }

    /**
     * Wraps an arbitrary parser into a {@link LanguageFileFactory}.
     *
     * @param parser parser that creates the language file from a language code and input stream
     * @return a {@link LanguageFileFactory}
     */
    public static LanguageFileFactory of(final BiFunction<String, InputStream, ? extends LanguageFile> parser) {
        Objects.requireNonNull(parser, "parser");
        return parser::apply;
    }
}

