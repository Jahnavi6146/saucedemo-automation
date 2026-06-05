package com.saucedemo.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent-report.html");
            spark.config().setReportName("SauceDemo Automation Report");
            spark.config().setDocumentTitle("SauceDemo Test Results");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "https://www.saucedemo.com/");
            extent.setSystemInfo("Framework", "Selenium + TestNG + POM");
        }
        return extent;
    }
}
