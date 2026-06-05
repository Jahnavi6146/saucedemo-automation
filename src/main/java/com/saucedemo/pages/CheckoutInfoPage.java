package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.saucedemo.base.BasePage;

public class CheckoutInfoPage extends BasePage {

    private final By firstName      = By.id("first-name");
    private final By lastName       = By.id("last-name");
    private final By postalCode     = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage   = By.cssSelector("h3[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(continueButton);
    }

    public CheckoutOverviewPage enterDetailsAndContinue(String fn, String ln, String zip) {
        fill(fn, ln, zip);
        clickAndWaitForUrl(continueButton, "checkout-step-two.html");
        return new CheckoutOverviewPage(driver);
    }

    public void fill(String fn, String ln, String zip) {
        type(firstName, fn);
        type(lastName, ln);
        type(postalCode, zip);
    }

    public void clickContinue()         { click(continueButton); }
    public String getErrorMessage()     { return text(errorMessage); }
    public boolean isErrorDisplayed()   { return isDisplayed(errorMessage); }
    public boolean stillOnInfoPage()    { return isDisplayed(continueButton); }
}
