package tests;

import framework.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmokeTest extends BaseTest {

    @Test
    void shouldOpenPlaywrightSite() {
        page.navigate("https://playwright.dev");
        assertTrue(page.title().contains("Playwright"));
    }
}
