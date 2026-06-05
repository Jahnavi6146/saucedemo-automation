package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class EndToEndTests extends BaseTest {

    private InventoryPage login() {
        return new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
    }

    private CheckoutCompletePage checkout() {
        return new CartPage(driver).clickCheckout()
                .enterDetailsAndContinue(ConfigReader.get("firstName"),
                        ConfigReader.get("lastName"), ConfigReader.get("postalCode"))
                .clickFinish();
    }

    @Test(description = "TC_001 E2E: Purchase a single product end-to-end")
    public void singleProductPurchase() {
        InventoryPage inv = login();
        inv.addToCart("Sauce Labs Backpack");
        inv.openCart();
        Assert.assertTrue(new CartPage(driver).containsProduct("Sauce Labs Backpack"));
        Assert.assertEquals(checkout().getConfirmationMessage(), "Thank you for your order!");
    }

    @Test(description = "TC_002 E2E: Purchase multiple products end-to-end")
    public void multipleProductPurchase() {
        InventoryPage inv = login();
        inv.addToCart("Sauce Labs Backpack");
        inv.addToCart("Sauce Labs Bike Light");
        inv.addToCart("Sauce Labs Bolt T-Shirt");
        Assert.assertEquals(inv.getCartCount(), 3);
        inv.openCart();
        Assert.assertEquals(new CartPage(driver).getItemCount(), 3);
        Assert.assertEquals(checkout().getConfirmationMessage(), "Thank you for your order!");
    }

    @Test(description = "TC_003 E2E: Add three, remove one, purchase remaining")
    public void cartModificationFlow() {
        InventoryPage inv = login();
        inv.addToCart("Sauce Labs Backpack");
        inv.addToCart("Sauce Labs Bike Light");
        inv.addToCart("Sauce Labs Bolt T-Shirt");
        inv.openCart();
        CartPage cart = new CartPage(driver);
        cart.removeProduct("Sauce Labs Bike Light");
        Assert.assertEquals(cart.getItemCount(), 2);
        Assert.assertFalse(cart.containsProduct("Sauce Labs Bike Light"));
        Assert.assertEquals(checkout().getConfirmationMessage(), "Thank you for your order!");
    }

    @Test(description = "TC_004 E2E: Complete an order then logout")
    public void purchaseThenLogout() {
        InventoryPage inv = login();
        inv.addToCart("Sauce Labs Backpack");
        inv.openCart();
        CheckoutCompletePage complete = checkout();
        Assert.assertTrue(complete.isLoaded());
        complete.logout();
        Assert.assertTrue(new LoginPage(driver).isLoginButtonDisplayed());
    }

    @Test(description = "TC_005 E2E: Complete a purchase then continue shopping")
    public void purchaseThenContinueShopping() {
        InventoryPage inv = login();
        inv.addToCart("Sauce Labs Backpack");
        inv.openCart();
        InventoryPage back = checkout().backHome();
        Assert.assertTrue(back.isLoaded());
        back.addToCart("Sauce Labs Bike Light");
        Assert.assertEquals(back.getCartCount(), 1);
    }
}
