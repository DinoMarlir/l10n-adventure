package com.github.dinomarlir.l10nadventure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

import com.github.dinomarlir.l10nadventure.file.PropertiesLanguageFile;
import org.junit.jupiter.api.Test;

class PropertiesLanguageFileTest {

    @Test
    void loadsPropertiesContent() {
        final PropertiesLanguageFile file = PropertiesLanguageFile.from("en", new StringReader("greeting=Hello\nname=World\n"));

        assertEquals("en", file.language());
        assertTrue(file.containsKey("greeting"));
        assertEquals("Hello", file.get("greeting").orElseThrow());
        assertEquals("World", file.values().get("name"));
    }
}

