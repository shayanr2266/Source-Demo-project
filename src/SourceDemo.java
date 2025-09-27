
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class SourceDemo {

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

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        // POM usage
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");
        inventoryPage.addFirstProductToCart();
        inventoryPage.goToCart();
        cartPage.clickCheckout();
        checkoutPage.fillInfoAndContinue("TEST FNAME", "TEST LNAME", "12345");
        checkoutPage.finishOrder();
        driver.quit();
    }
}