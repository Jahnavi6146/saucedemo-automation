package com.saucedemo.pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.saucedemo.base.BasePage;

public class InventoryPage extends BasePage {

    private final By pageTitle      = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemNames      = By.className("inventory_item_name");
    private final By itemPrices     = By.className("inventory_item_price");
    private final By sortDropdown   = By.className("product_sort_container");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            return text(pageTitle).equalsIgnoreCase("Products");
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle()       { return text(pageTitle); }
    public int getProductCount()   { return count(inventoryItems); }

    public List<String> getProductNames() {
        return driver.findElements(itemNames).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public List<Double> getProductPrices() {
        return driver.findElements(itemPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public void sortBy(String visibleText) {
        selectByVisibleText(sortDropdown, visibleText);
    }

    public boolean allProductsHaveDetails() {
        List<WebElement> items = driver.findElements(inventoryItems);
        for (WebElement item : items) {
            boolean img   = !item.findElements(By.tagName("img")).isEmpty();
            boolean name  = !item.findElements(By.className("inventory_item_name")).isEmpty();
            boolean desc  = !item.findElements(By.className("inventory_item_desc")).isEmpty();
            boolean price = !item.findElements(By.className("inventory_item_price")).isEmpty();
            if (!(img && name && desc && price)) return false;
        }
        return true;
    }

    public void addToCart(String productName) {
        int before = getCartCount();
        By btn = By.xpath("//div[text()='" + productName +
                "']/ancestor::div[contains(@class,'inventory_item')]//button");
        click(btn);
        // Confirm the click registered: the cart badge must increment.
        waitForCartCount(before + 1);
    }
}
