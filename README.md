# l10n-adventure

`l10n-adventure` is a small, platform-independent localization / i18n library for Minecraft-based applications.

## Features

- Classpath-based translation loading from `path/<prefix><lang>.properties`
- Minecraft-style locale names such as `de_DE` or `en_US`
- `.properties`-based language files
- Kyori Adventure / MiniMessage output as `Component`
- Named placeholders via `Argument.component("name", Component.text("Kezz"))`
- Runtime language switching

## Usage

```java
import com.github.dinomarlir.l10nadventure.Argument;
import com.github.dinomarlir.l10nadventure.L10n;
import com.github.dinomarlir.l10nadventure.storage.ResourceBundleTranslationStorage;
import com.github.dinomarlir.l10nadventure.storage.TranslationStorage;
import net.kyori.adventure.text.Component;

public final class Example {

  static void main(String[] args) {
    TranslationStorage storage = new ResourceBundleTranslationStorage("l10n", "messages_");

    L10n l10n = new L10n(storage, "en_US");

    // Uses the active language
    Component message = l10n.translate(
        "greeting",
        Argument.component("name", Component.text("Kezz"))
    );

    // Switch language at runtime
    l10n.setLanguage("de_DE");
    Component germanMessage = l10n.translate(
        "greeting",
        Argument.component("name", Component.text("Kezz"))
    );
  }
}
```

<details>
<summary>Alternative: custom <code>LanguageFile</code> implementation</summary>

```java
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import com.github.dinomarlir.l10nadventure.file.LanguageFile;
import com.github.dinomarlir.l10nadventure.file.LanguageFileFactories;
import com.github.dinomarlir.l10nadventure.storage.ResourceBundleTranslationStorage;
import com.github.dinomarlir.l10nadventure.storage.TranslationStorage;

final class MyCustomLanguageFile implements LanguageFile {
  private final String language;
  private final Map<String, String> values;

  MyCustomLanguageFile(String language, InputStream inputStream) {
    this.language = language;
    Properties properties = new Properties();
    try {
      properties.load(inputStream);
    } catch (java.io.IOException e) {
      throw new java.io.UncheckedIOException(e);
    }
    this.values = (Map) properties;
  }

  @Override
  public String language() {
    return language;
  }

  @Override
  public Map<String, String> values() {
    return values;
  }
}

TranslationStorage storage = new ResourceBundleTranslationStorage(
    "l10n",
    "messages_",
    LanguageFileFactories.of(MyCustomLanguageFile::new)
);
```

</details>

## Example resource files

Create language files like these:

- `src/main/resources/l10n/messages_en_US.properties`
- `src/main/resources/l10n/messages_de_DE.properties`

The storage discovers available languages automatically by scanning the configured path for files
matching the prefix and a Minecraft-style locale name such as `de_DE`.

Example content:

```properties
# messages_en_US.properties
greeting=Hello, <name>!
```

```properties
# messages_de_DE.properties
greeting=Hallo, <name>!
```

## Choosing a `LanguageFile` implementation

By default, `ResourceBundleTranslationStorage` uses `PropertiesLanguageFile`.

If you want to plug in your own implementation, pass a `LanguageFileFactory`:

```java
TranslationStorage storage = new ResourceBundleTranslationStorage(
    "l10n",
    "messages_",
    LanguageFileFactories.of((language, inputStream) -> new MyCustomLanguageFile(language, inputStream))
);
```

This lets you parse the same `path/<prefix><lang>.properties` files into any `LanguageFile`
implementation you want.

## Publishing

The project is configured with Gradle's `maven-publish` plugin and exposes a `mavenJava` publication.

To publish the library to your local Maven repository:

```bash
./gradlew publishToMavenLocal
```

The generated publication includes the main JAR, sources JAR, and Javadoc JAR.










