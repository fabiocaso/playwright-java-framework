package framework;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeEach
    void setUp() {
        browser = PlaywrightFactory.initBrowser();
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void tearDown() {
        context.close();
        PlaywrightFactory.tearDown();
    }
}
