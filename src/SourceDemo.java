import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class SourceDemo {

    // Utility method to take screenshot and return file path
    public static String takeScreenshot(WebDriver driver, String name) {
        String dest = "screenshot-" + name + ".png";
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(dest);
        try {
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
        return destFile.getAbsolutePath();
    }

    public static void main(String[] args) {

        // Set path to Firefox browser binary if not in default location
        String[] possiblePaths = {
            "C:\\Program Files\\Mozilla Firefox\\firefox.exe",
            "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"
        };
        boolean found = false;
        for (String path : possiblePaths) {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                System.setProperty("webdriver.firefox.bin", path);
                found = true;
                break;
            }
        }
        if (!found) {
            System.err.println("Firefox browser not found. Please install Firefox or set the correct path in SourceDemo.java");
            System.exit(1);
        }

    // ExtentReports setup for v5.x
    ExtentSparkReporter spark = new ExtentSparkReporter("ExtentReport.html");
    ExtentReports extent = new ExtentReports();
    extent.attachReporter(spark);
    ExtentTest test = extent.createTest("SauceDemo End-to-End Test");

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    try {
            // POM usage
            LoginPage loginPage = new LoginPage(driver);
            InventoryPage inventoryPage = new InventoryPage(driver);
            CartPage cartPage = new CartPage(driver);
            CheckoutPage checkoutPage = new CheckoutPage(driver);

            loginPage.open();
            test.info("Opened SauceDemo login page");
            loginPage.login("standard_user", "secrt_sauce");
            test.info("Logged in as standard_user");
            inventoryPage.addFirstProductToCart();
            test.info("Added first product to cart");
            inventoryPage.goToCart();
            test.info("Navigated to cart");
            cartPage.clickCheckout();
            test.info("Clicked checkout");
            checkoutPage.fillInfoAndContinue("TEST FNAME", "TEST LNAME", "12345");
            test.info("Filled checkout info");
            checkoutPage.finishOrder();
            test.info("Finished order");
            test.pass("Test completed successfully");
        } catch (Exception e) {
            String screenshotPath = takeScreenshot(driver, "failure");
            test.fail("Test failed: " + e.getMessage()).addScreenCaptureFromPath(screenshotPath);
            throw e;
        } finally {
            driver.quit();
            extent.flush();
        }
    }
}