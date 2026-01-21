package tests;

import framework.base.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SmokeTest extends BaseTest {

    @Test
    void shouldOpenPlaywrightSite() {
        page.navigate(baseUrl);
        assertTrue(page.title().contains("Playwright"));
    }

    @Test
    void shouldHaveCorrectUrlAfterNavigation() {
        page.navigate(baseUrl);
        String url = page.url();
        // allow for optional trailing slash or additional path fragments
        String expected = baseUrl.replaceAll("/+$", "");
        assertTrue(url.startsWith(expected), () -> "Expected URL to start with '" + expected + "', but was: " + url);
    }

    @Test
    void shouldBeFullyLoaded() {
        page.navigate(baseUrl);
        String ready = page.evaluate("() => document.readyState").toString();
        assertEquals("complete", ready, "Expected document.readyState to be 'complete'");
    }

    @Test
    void baseUrlMatchesConfig() {
        // BaseTest initializes baseUrl from TestConfig; ensure they match
        assertEquals(getConfigForEnv("base.url"), baseUrl);
    }
}
