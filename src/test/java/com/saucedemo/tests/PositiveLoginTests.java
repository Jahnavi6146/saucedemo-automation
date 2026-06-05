package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class PositiveLoginTests extends BaseTest {

    @DataProvider(name = "validUsers")
    public Object[][] validUsers() {
        return new Object[][] {
            {"standard_user"}, {"problem_user"}, {"performance_glitch_user"},
            {"error_user"}, {"visual_user"}
        };
    }

    @Test(dataProvider = "validUsers",
          description = "TC_001-005: Login succeeds for each valid SauceDemo user")
    public void loginWithValidUsers(String user) {
        InventoryPage inventory = new LoginPage(driver).login(user, ConfigReader.get("password"));
        Assert.assertTrue(inventory.isLoaded(), "Inventory should load for user: " + user);
    }

    @Test(description = "TC_006: Products page displayed after successful login")
    public void productsPageDisplayedAfterLogin() {
        InventoryPage inventory = new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
        Assert.assertEquals(inventory.getTitle(), "Products");
        Assert.assertTrue(inventory.getProductCount() > 0);
    }
}
