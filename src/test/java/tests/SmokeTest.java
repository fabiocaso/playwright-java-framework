package tests;

import framework.base.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmokeTest extends BaseTest {

    @Test
    void shouldOpenPlaywrightSite() {
        page.navigate(baseUrl);
        assertTrue(page.title().contains("Playwright"));
    }
}
