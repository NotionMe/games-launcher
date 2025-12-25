package ua.notion.utils;

import io.github.cdimascio.dotenv.Dotenv;

public final class Config {

    private static final Dotenv dotenv = Dotenv.load();

    private Config() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String get(String key) {
        return dotenv.get(key);
    }

    public static String get(String key, String defaultValue) {
        return dotenv.get(key, defaultValue);
    }

    public static String getSteamGridDBApiKey() {
        return get("STEAMGRIDDB_API_KEY");
    }
}
