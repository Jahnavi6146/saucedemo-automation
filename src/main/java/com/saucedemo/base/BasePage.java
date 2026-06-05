package com.saucedemo.base;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.saucedemo.config.ConfigReader;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private final By cartBadge  = By.className("shopping_cart_badge");
    private final By cartLink   = By.className("shopping_cart_link");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By menuCross  = By.id("react-burger-cross-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By loginButton = By.id("login-button");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getInt("explicitWaitSeconds")));
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        clickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement e = visible(locator);
        e.clear();
        if (text != null && !text.isEmpty()) e.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected int count(By locator) {
        return driver.findElements(locator).size();
    }

    protected void selectByVisibleText(By locator, String value) {
        new Select(visible(locator)).selectByVisibleText(value);
    }

    /**
     * Clicks an element and waits for the URL to advance. SauceDemo is a React SPA: a
     * button can be "clickable" before its handler is attached, so the first click is
     * occasionally a no-op. If the URL does not change and we are still on the same page,
     * we click once more before failing.
     */
    protected void clickAndWaitForUrl(By locator, String urlFragment) {
        click(locator);
        try {
            wait.until(ExpectedConditions.urlContains(urlFragment));
        } catch (TimeoutException e) {
            if (isPresent(locator)) {          // still on the same page -> click was lost
                click(locator);
                wait.until(ExpectedConditions.urlContains(urlFragment));
            } else {
                throw e;
            }
        }
    }

    /** Quick presence check (no long wait) — used to detect a lost click. */
    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    public int getCartCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    /** Waits until the cart badge reflects the expected count (handles React lag). */
    public void waitForCartCount(int expected) {
        wait.until(d -> getCartCount() == expected);
    }

    public void openCart() {
        clickAndWaitForUrl(cartLink, "cart.html");
    }

    public void logout() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
    }

    /** Opens the burger menu, retrying the toggle if the first click is lost. */
    private void openMenu() {
        click(menuButton);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(menuCross));
        } catch (TimeoutException e) {
            click(menuButton);
            wait.until(ExpectedConditions.visibilityOfElementLocated(menuCross));
        }
    }
}
