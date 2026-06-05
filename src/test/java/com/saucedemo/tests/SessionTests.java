package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class SessionTests extends BaseTest {

    @Test(description = "TC_012: Inventory not accessible via Back button after logout")
    public void noAccessAfterLogoutBack() {
        InventoryPage inventory = new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
        inventory.logout();
        driver.navigate().back();
        LoginPage login = new LoginPage(driver);
        Assert.assertTrue(login.isErrorDisplayed() || login.isLoginButtonDisplayed(),
                "User should not regain access to inventory after logout");
    }

    @Test(description = "TC_013: Direct inventory URL access without login is blocked")
    public void directUrlBlocked() {
        driver.get(ConfigReader.get("baseUrl") + "inventory.html");
        LoginPage login = new LoginPage(driver);
        Assert.assertTrue(login.isErrorDisplayed() || login.isLoginButtonDisplayed(),
                "Direct access should redirect to login page");
    }
}
