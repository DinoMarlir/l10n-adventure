package com.github.dinomarlir.l10nadventure;

import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * {@link TranslationStorage} implementation that reads translations from the classpath.
 *
 * <p>The storage points to a directory-like resource path and expects files named
 * {@code <prefix><language>.properties}, for example {@code l10n/messages_de_DE.properties}.</p>
 */
public final class ResourceBundleTranslationStorage implements TranslationStorage {

    private final String path;
    private final String prefix;
    private final ClassLoader classLoader;
    private final Set<String> availableLanguages;

    /**
     * Creates a storage backed by classpath resources using the current thread context class loader.
     *
     * @param path the resource path, for example {@code l10n}
     * @param prefix the file prefix, for example {@code messages_}
     */
    public ResourceBundleTranslationStorage(final String path, final String prefix) {
        this(path, prefix, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Creates a storage backed by classpath resources.
     *
     * @param path the resource path, for example {@code l10n}
     * @param prefix the file prefix, for example {@code messages_}
     * @param classLoader the class loader used to resolve bundles
     */
    public ResourceBundleTranslationStorage(final String path, final String prefix, final ClassLoader classLoader) {
        this.path = normalizePath(path);
        this.prefix = normalizePrefix(prefix);
        this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
        this.availableLanguages = Collections.unmodifiableSet(discoverLanguages());
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> availableLanguages() {
        return this.availableLanguages;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<LanguageFile> getLanguageFile(final String language) {
        if (!this.availableLanguages.isEmpty() && !this.availableLanguages.contains(language)) {
            return Optional.empty();
        }

        final String resourceName = this.path + "/" + this.prefix + language + ".properties";
        final InputStream inputStream = this.classLoader.getResourceAsStream(resourceName);
        if (inputStream == null) {
            return Optional.empty();
        }

        try (inputStream) {
            return Optional.of(PropertiesLanguageFile.from(language, inputStream));
        } catch (final Exception exception) {
            return Optional.empty();
        }
    }

    private static String normalizePath(final String path) {
        final String normalized = Objects.requireNonNull(path, "path").replaceAll("^/+|/+$", "");
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("path must not be blank");
        }
        return normalized;
    }

    private static String normalizePrefix(final String prefix) {
        return Objects.requireNonNull(prefix, "prefix");
    }

    private Set<String> discoverLanguages() {
        final Set<String> languages = new LinkedHashSet<>();

        try {
            scanResources(this.classLoader.getResources(""), languages);
            scanResources(this.classLoader.getResources(this.path), languages);
        } catch (final Exception exception) {
            return Set.of();
        }

        return languages;
    }

    private void scanResources(final Enumeration<URL> resources, final Set<String> languages) {
        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            if ("file".equals(url.getProtocol())) {
                discoverFromFileSystem(url, languages);
            } else if ("jar".equals(url.getProtocol())) {
                discoverFromJar(url, languages);
            }
        }
    }

    private void discoverFromFileSystem(final URL url, final Set<String> languages) {
        final Path directory;
        try {
            directory = Paths.get(url.toURI());
        } catch (final Exception exception) {
            return;
        }

        if (!Files.isDirectory(directory)) {
            return;
        }

        try (var stream = Files.walk(directory)) {
            stream.filter(Files::isRegularFile)
                .map(directory::relativize)
                .map(Path::toString)
                .map(this::normalizeResourceName)
                .forEach(resourceName -> this.considerResourceName(resourceName, languages));
        } catch (final Exception exception) {
            // ignore discovery problems and keep whatever was found so far
        }
    }

    private void discoverFromJar(final URL url, final Set<String> languages) {
        try {
            final JarURLConnection connection = (JarURLConnection) url.openConnection();
            try (JarFile jarFile = connection.getJarFile()) {
                final Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    final JarEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        continue;
                    }

                    this.considerResourceName(normalizeResourceName(entry.getName()), languages);
                }
            }
        } catch (final Exception exception) {
            // ignore discovery problems and keep whatever was found so far
        }
    }

    private void considerResourceName(final String resourceName, final Set<String> languages) {
        final String fileName = resourceName.startsWith(this.path + "/")
            ? resourceName.substring(this.path.length() + 1)
            : resourceName;

        if (this.isMatchingLanguageFile(fileName)) {
            languages.add(this.extractLanguage(fileName));
        }
    }

    private String normalizeResourceName(final String resourceName) {
        return resourceName.replace('\\', '/');
    }

    private boolean isMatchingLanguageFile(final String fileName) {
        if (!fileName.startsWith(this.prefix) || !fileName.endsWith(".properties")) {
            return false;
        }

        final String language = this.extractLanguage(fileName);
        return language.matches("^[a-z]{2,3}_[A-Z]{2}(?:_[A-Za-z0-9]+)*$");
    }

    private String extractLanguage(final String fileName) {
        return fileName.substring(this.prefix.length(), fileName.length() - ".properties".length());
    }
}











