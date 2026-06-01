package com.github.dinomarlir.l10nadventure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.Test;

class L10nTest {

    @Test
    void translatesNamedMiniMessageArguments() {
        final ResourceBundleTranslationStorage storage = new ResourceBundleTranslationStorage("l10n", "messages_");

        final L10n l10n = new L10n(storage, "en_US");
        assertEquals(Set.of("en_US", "de_DE"), storage.availableLanguages());
        final Component translated = l10n.translate("greeting", Argument.component("name", Component.text("Kezz")));

        assertEquals("Hello, Kezz!", MiniMessage.miniMessage().serialize(translated));
    }

    @Test
    void canSwitchLanguageAtRuntime() {
        final TranslationStorage storage = new ResourceBundleTranslationStorage("l10n", "messages_");

        final L10n l10n = new L10n(storage, "en_US");
        l10n.setLanguage("de_DE");

        final Component translated = l10n.translate("greeting", Argument.component("name", Component.text("Kezz")));
        assertEquals("Hallo, Kezz!", MiniMessage.miniMessage().serialize(translated));
    }

    @Test
    void fallsBackToDefaultLanguageWhenTranslationIsMissing() {
        final TranslationStorage storage = new TranslationStorage() {
            @Override
            public Set<String> availableLanguages() {
                return Set.of("en_US", "de_DE");
            }

            @Override
            public java.util.Optional<LanguageFile> getLanguageFile(final String language) {
                return switch (language) {
                    case "en_US" -> java.util.Optional.of(new PropertiesLanguageFile("en_US", java.util.Map.of("greeting", "Hello")));
                    case "de_DE" -> java.util.Optional.of(new PropertiesLanguageFile("de_DE", java.util.Map.of()));
                    default -> java.util.Optional.empty();
                };
            }
        };

        final L10n l10n = new L10n(storage, "en_US");
        assertEquals("Hello", l10n.raw("de_DE", "greeting"));
    }
}






