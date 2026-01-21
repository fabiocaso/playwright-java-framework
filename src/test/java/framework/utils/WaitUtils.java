package framework.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.function.BooleanSupplier;

/**
 * Small helper with common explicit wait utilities for Playwright tests.
 * Usage: WaitUtils.waitForVisible(page, "#myId", 5000);
 */
public final class WaitUtils {

    private WaitUtils() {}

    public static final int DEFAULT_TIMEOUT_MS = 5000;
    public static final int DEFAULT_POLL_INTERVAL_MS = 200;

    // -- Page/selector based waits --

    public static void waitForVisible(Page page, String selector) {
        waitForVisible(page, selector, DEFAULT_TIMEOUT_MS);
    }

    public static void waitForVisible(Page page, String selector, int timeoutMs) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeoutMs));
    }

    public static void waitForHidden(Page page, String selector) {
        waitForHidden(page, selector, DEFAULT_TIMEOUT_MS);
    }

    public static void waitForHidden(Page page, String selector, int timeoutMs) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeoutMs));
    }

    // -- Locator based waits --

    public static void waitForEnabled(Locator locator) {
        waitForEnabled(locator, DEFAULT_TIMEOUT_MS);
    }

    public static void waitForEnabled(Locator locator, int timeoutMs) {
        waitUntil(() -> {
            try {
                return locator.isEnabled();
            } catch (Exception e) {
                // If locator state can't be queried yet, treat as not enabled and continue polling
                return false;
            }
        }, timeoutMs, DEFAULT_POLL_INTERVAL_MS);
    }

    public static void waitForTextContains(Locator locator, String text) {
        waitForTextContains(locator, text, DEFAULT_TIMEOUT_MS);
    }

    public static void waitForTextContains(Locator locator, String text, int timeoutMs) {
        waitUntil(() -> {
            try {
                String t = locator.textContent();
                return t != null && t.contains(text);
            } catch (Exception e) {
                return false;
            }
        }, timeoutMs, DEFAULT_POLL_INTERVAL_MS);
    }

    // -- Page-level helpers --

    public static void waitForTitleContains(Page page, String fragment) {
        waitForTitleContains(page, fragment, DEFAULT_TIMEOUT_MS);
    }

    public static void waitForTitleContains(Page page, String fragment, int timeoutMs) {
        waitUntil(() -> {
            try {
                String title = page.title();
                return title != null && title.contains(fragment);
            } catch (Exception e) {
                return false;
            }
        }, timeoutMs, DEFAULT_POLL_INTERVAL_MS);
    }

    /**
     * Waits until the page URL contains the given fragment or the timeout is reached.
     * Implementation uses Playwright's page.waitForURL with a glob pattern for reliability.
     */
    public static void waitForUrlContains(Page page, String fragment) {
        waitForUrlContains(page, fragment, DEFAULT_TIMEOUT_MS);
    }

    public static void waitForUrlContains(Page page, String fragment, int timeoutMs) {
        try {
            // use a glob pattern so we match anywhere in the URL
            String pattern = "**" + fragment + "**";
            page.waitForURL(pattern, new Page.WaitForURLOptions().setTimeout((double) timeoutMs));
        } catch (PlaywrightException e) {
            throw new RuntimeException("Timeout waiting for URL containing '" + fragment + "'", e);
        }
    }

    /**
     * Non-throwing variant: returns true if the URL matched within the timeout, false otherwise.
     */
    public static boolean tryWaitForUrlContains(Page page, String fragment, int timeoutMs) {
        try {
            String pattern = "**" + fragment + "**";
            page.waitForURL(pattern, new Page.WaitForURLOptions().setTimeout((double) timeoutMs));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // -- Generic polling helper --

    public static void waitUntil(BooleanSupplier condition, int timeoutMs, int pollIntervalMs) {
        long deadline = System.currentTimeMillis() + Math.max(0, timeoutMs);
        while (System.currentTimeMillis() <= deadline) {
            if (condition.getAsBoolean()) return;
            try {
                Thread.sleep(Math.max(1, pollIntervalMs));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }
        throw new RuntimeException("Timeout after " + timeoutMs + " ms waiting for condition");
    }
}
