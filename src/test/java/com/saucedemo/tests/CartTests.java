package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class CartTests extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod(alwaysRun = true)
    public void loginFirst() {
        inventory = new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
    }

    @Test(description = "TC_011: Add a single product to cart")
    public void addSingleProduct() {
        inventory.addToCart("Sauce Labs Backpack");
        Assert.assertEquals(inventory.getCartCount(), 1);
    }

    @Test(description = "TC_012: Add multiple products to cart")
    public void addMultipleProducts() {
        inventory.addToCart("Sauce Labs Backpack");
        inventory.addToCart("Sauce Labs Bike Light");
        inventory.addToCart("Sauce Labs Bolt T-Shirt");
        Assert.assertEquals(inventory.getCartCount(), 3);
    }

    @Test(description = "TC_013: Cart badge count updates correctly")
    public void cartBadgeUpdates() {
        inventory.addToCart("Sauce Labs Backpack");
        Assert.assertEquals(inventory.getCartCount(), 1);
        inventory.addToCart("Sauce Labs Bike Light");
        Assert.assertEquals(inventory.getCartCount(), 2);
    }

    @Test(description = "TC_014: View products added to cart")
    public void viewCartProducts() {
        inventory.addToCart("Sauce Labs Backpack");
        inventory.openCart();
        CartPage cart = new CartPage(driver);
        Assert.assertTrue(cart.isLoaded());
        Assert.assertTrue(cart.containsProduct("Sauce Labs Backpack"));
    }

    @Test(description = "TC_015: Remove a product from the cart")
    public void removeProductFromCart() {
        inventory.addToCart("Sauce Labs Backpack");
        inventory.openCart();
        CartPage cart = new CartPage(driver);
        cart.removeProduct("Sauce Labs Backpack");
        Assert.assertEquals(cart.getItemCount(), 0);
    }
}
