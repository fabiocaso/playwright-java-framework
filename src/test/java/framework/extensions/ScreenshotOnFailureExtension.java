package framework.extensions;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import framework.base.BaseTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * JUnit 5 extension that captures a screenshot and page HTML when a test fails.
 * It expects tests to extend framework.base.BaseTest which exposes getPage().
 */
public class ScreenshotOnFailureExtension implements TestWatcher {

    private static final Path SCREENSHOT_DIR = Paths.get("target", "reports", "screenshots");

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Object testInstance = null;
        try {
            testInstance = context.getRequiredTestInstance();
        } catch (Exception e) {
            // can't get test instance
        }

        if (testInstance instanceof BaseTest) {
            BaseTest base = (BaseTest) testInstance;
            Page page = base.getPage();
            String displayName = context.getDisplayName();
            String className = context.getRequiredTestClass().getSimpleName();
            String methodName = context.getRequiredTestMethod().getName();
            try {
                Files.createDirectories(SCREENSHOT_DIR);
                if (page != null) {
                    Path screenshotPath = SCREENSHOT_DIR.resolve(className + "-" + methodName + ".png");
                    try {
                        page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
                    } catch (Exception e) {
                        // ignore screenshot failures
                    }

                    // save page HTML
                    try {
                        String html = page.content();
                        Path htmlPath = SCREENSHOT_DIR.resolve(className + "-" + methodName + ".html");
                        Files.writeString(htmlPath, html);
                    } catch (Exception e) {
                        // ignore
                    }
                } else {
                    // no page available
                    Path infoPath = SCREENSHOT_DIR.resolve(className + "-" + methodName + "-no-page.txt");
                    Files.writeString(infoPath, "Page instance was null; no screenshot captured.");
                }
            } catch (IOException e) {
                // ignore IO errors
            }
        }
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) { }

    @Override
    public void testSuccessful(ExtensionContext context) { }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) { }
}
