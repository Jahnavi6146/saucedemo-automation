package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.saucedemo.base.BasePage;

public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");
    private final By backHome       = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfirmationMessage() {
        return text(completeHeader);
    }

    public InventoryPage backHome() {
        click(backHome);
        return new InventoryPage(driver);
    }
}
