package com.saucedemo.tests;

import java.util.List;
import java.util.stream.Collectors;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class ProductTests extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod(alwaysRun = true)
    public void loginFirst() {
        inventory = new LoginPage(driver)
                .login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
    }

    @Test(description = "TC_007: All products displayed on Products page")
    public void allProductsDisplayed() {
        Assert.assertEquals(inventory.getProductCount(), 6, "SauceDemo lists 6 products");
    }

    @Test(description = "TC_008: Each product shows image, name, description and price")
    public void productDetailsDisplayed() {
        Assert.assertTrue(inventory.allProductsHaveDetails());
    }

    @Test(description = "TC_009: Products sortable by Name (A to Z)")
    public void sortByNameAtoZ() {
        inventory.sortBy("Name (A to Z)");
        List<String> actual = inventory.getProductNames();
        List<String> sorted = actual.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(actual, sorted, "Products should be in A-Z order");
    }

    @Test(description = "TC_010: Products sortable by Price (low to high)")
    public void sortByPriceLowToHigh() {
        inventory.sortBy("Price (low to high)");
        List<Double> actual = inventory.getProductPrices();
        List<Double> sorted = actual.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(actual, sorted, "Prices should ascend");
    }
}
