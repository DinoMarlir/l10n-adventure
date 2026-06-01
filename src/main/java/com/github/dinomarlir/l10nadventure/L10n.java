package com.github.dinomarlir.l10nadventure;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.dinomarlir.l10nadventure.file.LanguageFile;
import com.github.dinomarlir.l10nadventure.storage.TranslationStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Central translation service for loading and rendering localized MiniMessage strings.
 *
 * <p>Instances cache loaded language files and provide fallback handling to a configured default
 * language.</p>
 */
public final class L10n {

    private final TranslationStorage storage;
    private final MiniMessage miniMessage;
    private final String defaultLanguage;
    private final Map<String, Optional<LanguageFile>> cache = new ConcurrentHashMap<>();
    private volatile String language;

    /**
     * Creates a new translation service using the default MiniMessage instance.
     *
     * @param storage the translation storage
     * @param defaultLanguage the fallback language
     */
    public L10n(final TranslationStorage storage, final String defaultLanguage) {
        this(storage, defaultLanguage, MiniMessage.miniMessage());
    }

    /**
     * Creates a new translation service.
     *
     * @param storage the translation storage
     * @param defaultLanguage the fallback language
     * @param miniMessage the MiniMessage instance used for deserialization
     */
    public L10n(final TranslationStorage storage, final String defaultLanguage, final MiniMessage miniMessage) {
        this.storage = Objects.requireNonNull(storage, "storage");
        this.defaultLanguage = Objects.requireNonNull(defaultLanguage, "defaultLanguage");
        this.miniMessage = Objects.requireNonNull(miniMessage, "miniMessage");
        this.language = defaultLanguage;
    }

    /**
     * Returns the currently active language.
     *
     * @return the active language code
     */
    public String language() {
        return this.language;
    }

    /**
     * Returns the languages exposed by the configured storage.
     *
     * @return the available languages
     */
    public Set<String> availableLanguages() {
        return this.storage.availableLanguages();
    }

    /**
     * Changes the active language used by {@link #translate(String, Argument...)} and
     * {@link #raw(String)}.
     *
     * @param language the new active language code
     */
    public void setLanguage(final String language) {
        this.language = Objects.requireNonNull(language, "language");
    }

    /**
     * Clears the internal language-file cache so the next lookup reloads from storage.
     */
    public void reload() {
        this.cache.clear();
    }

    /**
     * Translates a key using the currently active language.
     *
     * @param key the translation key
     * @param arguments named MiniMessage arguments
     * @return the translated Adventure component
     */
    public Component translate(final String key, final Argument... arguments) {
        return this.translate(this.language, key, arguments);
    }

    /**
     * Translates a key using the given language.
     *
     * @param language the language code to use
     * @param key the translation key
     * @param arguments named MiniMessage arguments
     * @return the translated Adventure component
     */
    public Component translate(final String language, final String key, final Argument... arguments) {
        final String template = this.resolveTemplate(language, key);
        final TagResolver resolver = TagResolver.resolver(Arrays.stream(arguments)
            .map(Argument::asTagResolver)
            .toArray(TagResolver[]::new));
        return this.miniMessage.deserialize(template, resolver);
    }

    /**
     * Returns the raw template for a key using the currently active language.
     *
     * @param key the translation key
     * @return the raw template or the key itself if no translation is available
     */
    public String raw(final String key) {
        return this.resolveTemplate(this.language, key);
    }

    /**
     * Returns the raw template for a key using the given language.
     *
     * @param language the language code to use
     * @param key the translation key
     * @return the raw template or the key itself if no translation is available
     */
    public String raw(final String language, final String key) {
        return this.resolveTemplate(language, key);
    }

    private String resolveTemplate(final String language, final String key) {
        final Optional<String> current = this.languageFile(language).flatMap(file -> file.get(key));
        if (current.isPresent()) {
            return current.get();
        }

        if (!this.defaultLanguage.equals(language)) {
            final Optional<String> fallback = this.languageFile(this.defaultLanguage).flatMap(file -> file.get(key));
            if (fallback.isPresent()) {
                return fallback.get();
            }
        }

        return key;
    }

    private Optional<LanguageFile> languageFile(final String language) {
        return this.cache.computeIfAbsent(language, this.storage::getLanguageFile);
    }
}


