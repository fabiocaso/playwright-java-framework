package framework;

import com.microsoft.playwright.*;

public class PlaywrightFactory {

    private static Playwright playwright;
    private static Browser browser;

    public static Browser initBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        return browser;
    }

    public static void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
