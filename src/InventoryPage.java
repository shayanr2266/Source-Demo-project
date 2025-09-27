import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InventoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void addFirstProductToCart() {
        WebElement addToCartBtn = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn_inventory"))
        );
        addToCartBtn.click();
    }

    public void goToCart() {
        WebElement cartIcon = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("a.shopping_cart_link"))
        );
        cartIcon.click();
    }
}
