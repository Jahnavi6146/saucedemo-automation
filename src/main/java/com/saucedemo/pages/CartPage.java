package com.saucedemo.pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.saucedemo.base.BasePage;

public class CartPage extends BasePage {

    private final By cartItems      = By.className("cart_item");
    private final By itemNames      = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShop   = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("cart.html"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getItemCount() { return count(cartItems); }

    public List<String> getItemNames() {
        return driver.findElements(itemNames).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public boolean containsProduct(String name) {
        return getItemNames().contains(name);
    }

    public void removeProduct(String productName) {
        int before = getItemCount();
        By btn = By.xpath("//div[text()='" + productName +
                "']/ancestor::div[contains(@class,'cart_item')]//button");
        click(btn);
        // Confirm the row was actually removed before the test asserts.
        wait.until(d -> getItemCount() == before - 1);
    }

    public CheckoutInfoPage clickCheckout() {
        clickAndWaitForUrl(checkoutButton, "checkout-step-one.html");
        return new CheckoutInfoPage(driver);
    }

    public InventoryPage continueShopping() {
        clickAndWaitForUrl(continueShop, "inventory.html");
        return new InventoryPage(driver);
    }
}
