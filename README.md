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
import com.github.dinomarlir.l10nadventure.ResourceBundleTranslationStorage;
import com.github.dinomarlir.l10nadventure.TranslationStorage;
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










