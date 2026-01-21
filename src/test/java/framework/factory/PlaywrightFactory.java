package framework.factory;

import com.microsoft.playwright.*;
import framework.config.TestConfig;

public class PlaywrightFactory {

    private static Playwright playwright;
    private static Browser browser;

    public static Browser initBrowser() {
        TestConfig config = new TestConfig();
        boolean headless = config.getBoolean("headless", true);

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(headless)
        );
        return browser;
    }

    public static void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
