package global;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import static global.Constants.*;

public class OpenIncognito {
    WebDriverWait wait;

    public static void main(String[] args) throws InterruptedException {

        // Create an instance of ChromeOptions
        ChromeOptions options = new ChromeOptions();

        //Open in incognito mode
        options.addArguments("--incognito");

        // Create an instance of ChromeDriver with the options
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);

        //step 1: Open a website
        driver.get(siteUrl);

        driver.manage().window().maximize();

        //step 2: Enter valid credentials to login
        //enter user name
        driver.findElement(By.id("user-name")).sendKeys(userName);
        //enter valid password
        driver.findElement(By.id("password")).sendKeys(passWord);
        //click on login button
        driver.findElement(By.id("login-button")).click();

        //3. Verify that the login is successful and the user is redirected to the products page.
        String expectedTitle = "Products";
        String actualTitle = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(actualTitle, expectedTitle);

        //Step 4: Select a T-shirt by clicking on its image or name.
        String shirtName = driver.findElement(By.xpath(teeShirtName)).getText();
        String shirtPrice = driver.findElement(By.xpath(teeShirtPrice)).getText();
        driver.findElement(By.xpath(teeShirtName)).click();
        System.out.println(shirtName);
        System.out.println(shirtPrice);

        //Step 5: Verify that the T-shirt details page is displayed.
        String actualText = driver.findElement(By.xpath(selectShirt)).getText();
        if (actualText.equals(shirtName)) {
            System.out.println(shirtName);
        } else
            System.out.println("Error");

        //Step 6: Click the "Add to Cart" button.
        driver.findElement(By.id(addCartBtn)).click();

        //Step 7: Verify that the T-shirt is added to the cart successfully.
        driver.findElement(By.id("remove")).getText();

        //Step 8: Navigate to the cart by clicking the cart icon or accessing the cart page directly.
        driver.findElement(By.className(cart)).click();

        //Step 9: Verify that the cart page is displayed.
        String cartContent = driver.findElement(By.xpath(cartPage)).getText();
        if (cartContent.contains("Continue Shopping") && cartContent.contains("Checkout")) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }

//        Step 10: Review the items in the cart and ensure that the T-shirt is listed with the correct details (name, price,
//        quantity, etc.).
        String currentURL = driver.getCurrentUrl();
        System.out.println(currentURL);

        String inventoryItemName = driver.findElement(By.className("inventory_item_name")).getText();
        String inventoryItemPrice = driver.findElement(By.className("inventory_item_price")).getText();
        String inventoryShirtQtty = driver.findElement(By.className("cart_quantity")).getText();

        if (inventoryItemName.contains(shirtName) && inventoryItemPrice.contains(shirtPrice)
                && inventoryShirtQtty.equals("1")) {
            System.out.println("Correct Details");
            ;
        } else {
            System.out.println("Incorrect Details");
            ;
        }

        //11. Click the checkout button
        driver.findElement(By.id("checkout")).click();

        //12. Verify the checkout information page is displayed
        String checkOutURL = driver.getCurrentUrl();
        if (checkOutURL.endsWith("/checkout-step-one.html")) {
            Assert.assertTrue(true);
            System.out.println(checkOutURL);
        } else {
            Assert.assertTrue(false);
        }

        //13. Enter the required checkout information (e.g., name, shipping address, payment details).
        driver.findElement(By.id(fName)).sendKeys(firstName);
        driver.findElement(By.id(lName)).sendKeys(lastName);
        driver.findElement(By.id(pCode)).sendKeys(postalCode);

        //14. Click on Continue button
        driver.findElement(By.id("continue")).click();

        //15. Verify that the order summary page is displayed, showing the T-shirt details and the total amount.
        String checkOutUrl = driver.getCurrentUrl();
        System.out.println(checkOutUrl);
        String totalPrice = driver.findElement(By.className("summary_total_label")).getText();
        System.out.println(totalPrice);

        //16. Click the finish button
        driver.findElement(By.id("finish")).click();

        //17. Verify that the order confirmation page is displayed, indicating a successful purchase.
        String expectedMessage = successOrder;
        String actualMessage = driver.findElement(By.cssSelector(".complete-header")).getText();

        Assert.assertEquals(actualMessage, expectedMessage);

        //Go back to home
        driver.findElement(By.id("back-to-products")).click();

        //18. Log out of the app
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(3000);

        //click on logout
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
        Thread.sleep(2000);

        //Close the browser
        driver.quit();
    }
}
