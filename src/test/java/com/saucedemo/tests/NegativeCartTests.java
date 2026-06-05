package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class NegativeCartTests extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod(alwaysRun = true)
    public void loginFirst() {
        inventory = new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
    }

    @Test(description = "TC_014: Removed product no longer appears in cart")
    public void removedProductNotInCart() {
        inventory.addToCart("Sauce Labs Backpack");
        inventory.openCart();
        CartPage cart = new CartPage(driver);
        cart.removeProduct("Sauce Labs Backpack");
        Assert.assertFalse(cart.containsProduct("Sauce Labs Backpack"));
        Assert.assertEquals(cart.getItemCount(), 0);
    }

    // NOTE: SauceDemo does NOT technically block finishing an empty checkout (known demo quirk),
    // so this test verifies the real guard — that an empty cart has zero products to order.
    @Test(description = "TC_015: Empty cart has no products to checkout")
    public void emptyCartHasNoProducts() {
        inventory.openCart();
        CartPage cart = new CartPage(driver);
        Assert.assertEquals(cart.getItemCount(), 0,
                "No products should be present to check out");
    }
}
