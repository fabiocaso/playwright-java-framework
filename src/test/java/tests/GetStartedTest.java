package tests;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import framework.base.BaseTest;
import framework.utils.WaitUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetStartedTest extends BaseTest {

    @Test
    void clickGetStartedFromHome() {
        // Navigate to the configured base URL and click the visible "Get started" control
        page.navigate(baseUrl);
        page.getByText("Get started").first().click();

        // Wait for navigation to docs intro and assert the page loaded correctly using WaitUtils
        WaitUtils.waitForUrlContains(page, "/docs/intro", 10_000);

        String current = page.url();
        assertTrue(current.contains("/docs/intro"), () -> "Expected to navigate to '/docs/intro' but was: " + current);

        // Wait for the page title to contain 'Installation' and then assert the exact title
        WaitUtils.waitForTitleContains(page, "Installation", 10_000);
        String title = page.title();
        assertEquals("Installation | Playwright", title, () -> "Expected title to be 'Installation | Playwright' but was: '" + title + "'");
    }
}
