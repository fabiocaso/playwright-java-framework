package framework.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Simple test configuration loader.
 * Loads properties from classpath resource `test-config.properties` by default,
 * or from a resource specified by system property `test.config`.
 */
public class TestConfig {

    private final Properties props = new Properties();

    public TestConfig() {
        String override = System.getProperty("test.config");
        if (override != null && !override.trim().isEmpty()) {
            // try to load as classpath resource first
            InputStream is = getClass().getResourceAsStream(override.startsWith("/") ? override : "/" + override);
            if (is == null) {
                throw new IllegalStateException("Test config resource '" + override + "' not found on classpath");
            }
            loadFromStream(is);
        } else {
            InputStream is = getClass().getResourceAsStream("/test-config.properties");
            if (is == null) {
                throw new IllegalStateException("Resource '/test-config.properties' not found on the classpath. Create src/test/resources/test-config.properties or set -Dtest.config=<resource>");
            }
            loadFromStream(is);
        }
    }

    private void loadFromStream(InputStream is) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            props.load(r);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test configuration", e);
        }
    }

    public String get(String key) {
        // system property override
        String p = System.getProperty(key);
        if (p != null) return p;
        // env var override (convert dot to underscore and upper-case)
        String envKey = key.toUpperCase().replace('.', '_');
        String envVal = System.getenv(envKey);
        if (envVal != null) return envVal;
        return props.getProperty(key);
    }

    /**
     * Returns a key resolved for the active environment (env.<key>) if present, otherwise falls back to key.
     */
    public String getForEnv(String key) {
        String env = get("env");
        if (env != null && !env.trim().isEmpty()) {
            String envKey = env.trim() + "." + key;
            String v = get(envKey);
            if (v != null) return v;
        }
        return get(key);
    }

    // --- Typed helpers ---

    /**
     * Return an int config value or defaultValue when not present or unparseable.
     */
    public int getInt(String key, int defaultValue) {
        String v = get(key);
        if (v == null) return defaultValue;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Return a boolean config value or defaultValue when not present. Accepts true/false (case-insensitive).
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String v = get(key);
        if (v == null) return defaultValue;
        return Boolean.parseBoolean(v.trim());
    }
}
