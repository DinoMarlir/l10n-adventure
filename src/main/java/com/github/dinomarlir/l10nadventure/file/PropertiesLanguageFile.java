package com.github.dinomarlir.l10nadventure.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Language file backed by a {@code .properties} file.
 *
 * <p>The contained values are copied into an immutable map so the instance can be safely shared.
 * This class can be created from readers or streams.</p>
 */
public final class PropertiesLanguageFile implements LanguageFile {

    private final String language;
    private final Map<String, String> values;

    /**
     * Creates a new properties-backed language file.
     *
     * @param language the language code
     * @param values the translation entries
     */
    public PropertiesLanguageFile(final String language, final Map<String, String> values) {
        this.language = Objects.requireNonNull(language, "language");
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(Objects.requireNonNull(values, "values")));
    }

    /**
     * Loads a language file from a character reader.
     *
     * @param language the language code
     * @param reader the reader containing {@code .properties} content
     * @return the loaded language file
     */
    public static PropertiesLanguageFile from(final String language, final Reader reader) {
        final Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(reader, "reader"));
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
        return new PropertiesLanguageFile(language, toMap(properties));
    }

    /**
     * Loads a language file from a byte stream using the {@code .properties} format.
     *
     * @param language the language code
     * @param inputStream the stream containing {@code .properties} content
     * @return the loaded language file
     */
    public static PropertiesLanguageFile from(final String language, final InputStream inputStream) {
        final Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(inputStream, "inputStream"));
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
        return new PropertiesLanguageFile(language, toMap(properties));
    }

    /**
     * Returns a factory that parses {@code .properties} files into {@link PropertiesLanguageFile}
     * instances.
     *
     * @return the factory for properties language files
     */
    public static LanguageFileFactory factory() {
        return PropertiesLanguageFile::from;
    }

    private static Map<String, String> toMap(final Properties properties) {
        final Map<String, String> values = new LinkedHashMap<>();
        for (final String name : properties.stringPropertyNames()) {
            values.put(name, properties.getProperty(name));
        }
        return values;
    }

    /** {@inheritDoc} */
    @Override
    public String language() {
        return this.language;
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> values() {
        return this.values;
    }
}



