package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.LoginPage;

public class NegativeLoginTests extends BaseTest {

    @Test(description = "TC_001: Login fails for locked_out_user")
    public void lockedOutUser() {
        LoginPage login = new LoginPage(driver);
        login.login(ConfigReader.get("lockedOutUser"), ConfigReader.get("password"));
        Assert.assertTrue(login.isErrorDisplayed());
        Assert.assertTrue(login.getErrorMessage().contains("locked out"),
                "Actual: " + login.getErrorMessage());
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
            {"",              "secret_sauce", "Username is required"}, // TC_002 blank username
            {"standard_user", "",             "Password is required"}, // TC_003 blank password
            {"",              "",             "Username is required"}, // TC_004 both blank
            {"invalid_user",  "secret_sauce", "do not match"},         // TC_005 invalid username
            {"standard_user", "wrong_pass",   "do not match"},         // TC_006 invalid password
            {"invalid_user",  "wrong_pass",   "do not match"}          // TC_007 both invalid
        };
    }

    @Test(dataProvider = "invalidCredentials",
          description = "TC_002-007: Login fails with proper validation error")
    public void loginFailsWithError(String user, String pass, String expectedError) {
        LoginPage login = new LoginPage(driver);
        login.enterUsername(user);
        login.enterPassword(pass);
        login.clickLogin();
        Assert.assertTrue(login.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(login.getErrorMessage().contains(expectedError),
                "Expected '" + expectedError + "' but got: " + login.getErrorMessage());
    }
}
