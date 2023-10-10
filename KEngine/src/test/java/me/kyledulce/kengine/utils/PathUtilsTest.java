package me.kyledulce.kengine.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathUtilsTest {

    @Test
    public void testNormalizeResourcePath_removesLeadingSlash() {
        String initialPath = "/somePath/OtherPath/Somefile.txt";
        String expectedPath = "somePath/OtherPath/Somefile.txt";

        String actual = PathUtils.normalizeResourcePath(initialPath);

        assertEquals(expectedPath, actual);
    }

    @Test
    public void testNormalizeResourcePath_usesForwardSlashes() {
        String initialPath = "somePath\\OtherPath\\Somefile.txt";
        String expectedPath = "somePath/OtherPath/Somefile.txt";

        String actual = PathUtils.normalizeResourcePath(initialPath);

        assertEquals(expectedPath, actual);
    }

    @Test
    public void testNormalizeResourcePath_doesNotModifyNormalizedPath() {
        String expectedPath = "somePath/OtherPath/Somefile.txt";

        String actual = PathUtils.normalizeResourcePath(expectedPath);

        assertEquals(expectedPath, actual);
    }
}
