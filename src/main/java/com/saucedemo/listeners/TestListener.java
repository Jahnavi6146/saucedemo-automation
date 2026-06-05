package com.saucedemo.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.saucedemo.driver.DriverFactory;
import com.saucedemo.reports.ExtentManager;
import com.saucedemo.utils.ScreenshotUtil;

public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        test.set(extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, result.getThrowable());
        String path = ScreenshotUtil.capture(DriverFactory.getDriver(),
                result.getMethod().getMethodName());
        if (path != null) {
            try {
                test.get().addScreenCaptureFromPath(path);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extent.createTest(result.getMethod().getMethodName()).log(Status.SKIP, "Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
