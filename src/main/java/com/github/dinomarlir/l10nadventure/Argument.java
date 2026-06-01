package com.github.dinomarlir.l10nadventure;

import java.util.Objects;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Convenience wrapper for named MiniMessage placeholders.
 *
 * <p>Use this type together with {@link L10n#translate(String, Argument...)} to pass named
 * MiniMessage arguments in a readable way.</p>
 */
public final class Argument {

    private final TagResolver resolver;

    private Argument(final TagResolver resolver) {
        this.resolver = Objects.requireNonNull(resolver, "resolver");
    }

    /**
     * Creates a named placeholder whose value is rendered as an Adventure component.
     *
     * @param name the placeholder name
     * @param value the component value
     * @return a reusable argument wrapper
     */
    public static Argument component(final String name, final ComponentLike value) {
        return new Argument(Placeholder.component(name, value));
    }

    /**
     * Creates a named placeholder whose value is inserted as plain text.
     *
     * @param name the placeholder name
     * @param value the placeholder value
     * @return a reusable argument wrapper
     */
    public static Argument text(final String name, final String value) {
        return new Argument(Placeholder.unparsed(name, value));
    }

    /**
     * Returns the underlying MiniMessage resolver.
     *
     * @return the resolver for this argument
     */
    TagResolver asTagResolver() {
        return this.resolver;
    }
}


