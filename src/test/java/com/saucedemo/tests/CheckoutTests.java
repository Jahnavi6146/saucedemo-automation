package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutInfoPage;
import com.saucedemo.pages.CheckoutOverviewPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class CheckoutTests extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod(alwaysRun = true)
    public void loginAndAddProduct() {
        inventory = new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
        inventory.addToCart("Sauce Labs Backpack");
    }

    @Test(description = "TC_016: Navigate to Checkout Information page")
    public void navigateToCheckoutInfo() {
        inventory.openCart();
        CheckoutInfoPage info = new CartPage(driver).clickCheckout();
        Assert.assertTrue(info.isLoaded());
    }

    @Test(description = "TC_017: Provide valid checkout information")
    public void provideValidCheckoutInfo() {
        inventory.openCart();
        CheckoutOverviewPage overview = new CartPage(driver).clickCheckout()
                .enterDetailsAndContinue(ConfigReader.get("firstName"),
                        ConfigReader.get("lastName"), ConfigReader.get("postalCode"));
        Assert.assertTrue(overview.isLoaded());
    }

    @Test(description = "TC_018: Order summary displayed before purchase")
    public void orderSummaryDisplayed() {
        inventory.openCart();
        CheckoutOverviewPage overview = new CartPage(driver).clickCheckout()
                .enterDetailsAndContinue(ConfigReader.get("firstName"),
                        ConfigReader.get("lastName"), ConfigReader.get("postalCode"));
        Assert.assertTrue(overview.summaryDisplayed());
        Assert.assertEquals(overview.getItemCount(), 1);
    }

    @Test(description = "TC_019: Complete purchase successfully")
    public void completePurchase() {
        inventory.openCart();
        CheckoutCompletePage complete = new CartPage(driver).clickCheckout()
                .enterDetailsAndContinue(ConfigReader.get("firstName"),
                        ConfigReader.get("lastName"), ConfigReader.get("postalCode"))
                .clickFinish();
        Assert.assertTrue(complete.isLoaded());
        Assert.assertEquals(complete.getConfirmationMessage(), "Thank you for your order!");
    }
}
