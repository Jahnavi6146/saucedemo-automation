package com.saucedemo.driver;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import com.saucedemo.config.ConfigReader;

public final class DriverFactory {

    // ThreadLocal => each parallel TestNG thread gets its own browser.
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {}

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void initDriver(String browser, boolean headless) {
        boolean isFirefox = "firefox".equalsIgnoreCase(browser);
        WebDriver driver = null;

        // Chrome can transiently fail to launch ("Chrome instance exited") under load,
        // so creation is retried a couple of times before giving up.
        RuntimeException lastError = null;
        for (int attempt = 1; attempt <= 3 && driver == null; attempt++) {
            try {
                driver = isFirefox ? newFirefox(headless) : newChrome(headless);
            } catch (RuntimeException e) {
                lastError = e;
            }
        }
        if (driver == null) {
            throw (lastError != null ? lastError
                    : new IllegalStateException("Could not start the browser"));
        }

        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getInt("pageLoadTimeoutSeconds")));
        if (!headless) {
            driver.manage().window().maximize();
        }
        DRIVER.set(driver);
    }

    private static WebDriver newChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-first-run");
        return new ChromeDriver(options);
    }

    private static WebDriver newFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("-headless");
        return new FirefoxDriver(options);
    }

    public static void quitDriver() {
        if (DRIVER.get() != null) {
            DRIVER.get().quit();
            DRIVER.remove();
        }
    }
}
