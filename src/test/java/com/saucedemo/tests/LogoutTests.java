package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class LogoutTests extends BaseTest {

    @Test(description = "TC_020: User can logout from the application")
    public void logout() {
        InventoryPage inventory = new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
        Assert.assertTrue(inventory.isLoaded());
        inventory.logout();
        Assert.assertTrue(new LoginPage(driver).isLoginButtonDisplayed(),
                "Should be redirected back to login page");
    }
}
