package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutInfoPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class NegativeCheckoutTests extends BaseTest {

    private CheckoutInfoPage info;

    @BeforeMethod(alwaysRun = true)
    public void reachCheckoutInfo() {
        InventoryPage inventory = new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
        inventory.addToCart("Sauce Labs Backpack");
        inventory.openCart();
        info = new CartPage(driver).clickCheckout();
    }

    @DataProvider(name = "invalidCheckout")
    public Object[][] invalidCheckout() {
        return new Object[][] {
            {"",     "Jahnavi", "500001", "First Name is required"},  // TC_008 blank first
            {"Siva", "",        "500001", "Last Name is required"},   // TC_009 blank last
            {"Siva", "Jahnavi", "",       "Postal Code is required"}, // TC_010 blank postal
            {"",     "",        "",       "First Name is required"}   // TC_011 all blank
        };
    }

    @Test(dataProvider = "invalidCheckout",
          description = "TC_008-011: Checkout blocked with validation error on missing fields")
    public void checkoutValidation(String fn, String ln, String zip, String expectedError) {
        info.fill(fn, ln, zip);
        info.clickContinue();
        Assert.assertTrue(info.isErrorDisplayed());
        Assert.assertTrue(info.getErrorMessage().contains(expectedError),
                "Expected '" + expectedError + "' but got: " + info.getErrorMessage());
        Assert.assertTrue(info.stillOnInfoPage());
    }
}
