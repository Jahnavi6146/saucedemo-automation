package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.saucedemo.base.BasePage;

public class CheckoutOverviewPage extends BasePage {

    private final By itemTotal    = By.className("summary_subtotal_label");
    private final By tax          = By.className("summary_tax_label");
    private final By total        = By.className("summary_total_label");
    private final By cartItems    = By.className("cart_item");
    private final By finishButton = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getItemCount()        { return count(cartItems); }
    public String getItemTotalText() { return text(itemTotal); }
    public String getTaxText()       { return text(tax); }
    public String getTotalText()     { return text(total); }

    public boolean summaryDisplayed() {
        return isDisplayed(itemTotal) && isDisplayed(tax) && isDisplayed(total);
    }

    public CheckoutCompletePage clickFinish() {
        clickAndWaitForUrl(finishButton, "checkout-complete.html");
        return new CheckoutCompletePage(driver);
    }
}
