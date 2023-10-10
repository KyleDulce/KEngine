package me.kyledulce.kengine.utils;

public class PathUtils {

    /**
     * Normalizes file path.
     * Normalized path uses forward slashes and does not contain leading slash.
     * @param path path to normalize
     * @return normalized path
     */
    public static String normalizeResourcePath(String path) {
        String normalizedPath = path.replaceAll("\\\\", "/");

        if(normalizedPath.startsWith("/")) {
            normalizedPath = normalizedPath.replaceFirst("/", "");
        }

        return normalizedPath;
    }
}
