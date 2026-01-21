package framework.base;

import com.microsoft.playwright.*;
import framework.config.TestConfig;
import framework.factory.PlaywrightFactory;
import framework.extensions.ScreenshotOnFailureExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ScreenshotOnFailureExtension.class)
public class BaseTest {

    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    // Test configuration helper
    protected final TestConfig config = new TestConfig();

    // Base URL for tests. Resolved from TestConfig (env-aware) or system properties via TestConfig.get
    protected String baseUrl;

    @BeforeEach
    void setUp() {
        // resolve base URL from config (environment-specific first)
        String fromConfig = config.getForEnv("base.url");
        if (fromConfig != null && !fromConfig.trim().isEmpty()) {
            baseUrl = fromConfig.trim();
        } else {
            throw new IllegalStateException("No base URL configured. Set 'env' and '<env>.base.url' in test-config.properties, or pass -Dbase.url=<url> / -D<env>.base.url=<url>");
        }

        browser = PlaywrightFactory.initBrowser();
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            try { context.close(); } catch (Exception ignored) {}
        }
        PlaywrightFactory.tearDown();
    }

    /**
     * Returns a configuration value. Resolution order: system property -> env var -> test-config.properties
     */
    protected String getConfig(String key) {
        return config.get(key);
    }

    /**
     * Returns a configuration value that is resolved per active environment. For example, env=dev and key 'base.url' will return 'dev.base.url' if present.
     */
    protected String getConfigForEnv(String key) {
        return config.getForEnv(key);
    }

    // Public accessor used by extensions to capture screenshots
    public Page getPage() {
        return page;
    }
}
