package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.saucedemo.base.BasePage;

public class LoginPage extends BasePage {

    private final By username     = By.id("user-name");
    private final By password     = By.id("password");
    private final By loginButton  = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public InventoryPage login(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
        return new InventoryPage(driver);
    }

    public void enterUsername(String user) { type(username, user); }
    public void enterPassword(String pass) { type(password, pass); }
    public void clickLogin()               { click(loginButton); }

    public String getErrorMessage()        { return text(errorMessage); }
    public boolean isErrorDisplayed()       { return isDisplayed(errorMessage); }
    public boolean isLoginButtonDisplayed() { return isDisplayed(loginButton); }
}
