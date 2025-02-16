package global;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

import static global.Constants.*;

public class sauceDemo {
    WebDriver driver;
    WebDriverWait wait;
    WebElement usernameField;
    WebElement passwordField;

    @BeforeTest
    public void launchPage() {

        // Create an instance of ChromeOptions
        ChromeOptions options = new ChromeOptions();

        //to disable any notifications
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito");

        // Create an instance of ChromeDriver with the options
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.manage().window().maximize();

        //step 1: Open a website
        driver.get(siteUrl);

        //initialize password and username field
        usernameField = driver.findElement(By.id("user-name"));
        passwordField = driver.findElement(By.id("password"));

    }

    @Test(priority = 0)

    public void InvalidLogin() throws InterruptedException {

        //enter invalid credentials
        usernameField.sendKeys(invalidUserName);

        //enter valid password
        passwordField.sendKeys(invalidpassword);

        //click on login button
        driver.findElement(By.id("login-button")).click();

        //to identify the expected response
        String exp = "Epic sadface: Username and password do not match any user in this service";
        WebElement invalidCred = driver.findElement(By.tagName("h3"));
        String out = invalidCred.getText();
        System.out.println("Error Message: " + out);

        //verify error message with assertion
        Assert.assertEquals(exp, out);

        //clear fields
        driver.findElement(By.tagName("button")).click();
        usernameField.clear();
        passwordField.clear();
        passwordField.sendKeys(Keys.BACK_SPACE);
        Thread.sleep(5000);

    }

    @Test(priority = 1)

    public void validLogin() throws InterruptedException {

        //Enter valid credentials to login
        //enter user name
        usernameField.sendKeys(userName);
        //enter valid password
        passwordField.sendKeys(passWord);
        //click on login button
        driver.findElement(By.id("login-button")).click();

        //Verify that the login is successful and the user is redirected to the products page.
        String expectedTitle = "Products";
        String actualTitle = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(actualTitle, expectedTitle);


        // Locate the filter dropdown
        WebElement filterDropdown = driver.findElement(By.className(filterButton));
        Select select = new Select(filterDropdown);

        // Apply all sorting options by visible text
        String[] filterOptions = {"Name (A to Z)", "Name (Z to A)"};

        for (String option : filterOptions) {
            select.selectByVisibleText(option); // Select filter
            System.out.println("Applied filter: " + option);

            // Optional: Add a small wait to observe the change
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        //filter by from a Z to A

        //Select a T-shirt by clicking on its image or name.
        String shirtName = driver.findElement(By.xpath(teeShirtName)).getText();
        String shirtPrice = driver.findElement(By.xpath(teeShirtPrice)).getText();
        driver.findElement(By.xpath(teeShirtName)).click();
        System.out.println(shirtName);
        System.out.println(shirtPrice);

        //Verify that the T-shirt details page is displayed.
        String actualText = driver.findElement(By.xpath(selectShirt)).getText();
        if (actualText.equals(shirtName)) {
            System.out.println(shirtName);
        } else
            System.out.println("Error");

        //Click the "Add to Cart" button.
        driver.findElement(By.id(addCartBtn)).click();

        //Verify that the T-shirt is added to the cart successfully.
        driver.findElement(By.id("remove")).getText();

        //Navigate to the cart by clicking the cart icon or accessing the cart page directly.
        driver.findElement(By.className(cart)).click();

        //Verify that the cart page is displayed.
        String cartContent = driver.findElement(By.xpath(cartPage)).getText();
        if (cartContent.contains("Continue Shopping") && cartContent.contains("Checkout")) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }

//Review the items in the cart and ensure that the T-shirt is listed with the correct details (name, price,quantity, etc.).
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

        //Click the checkout button
        driver.findElement(By.id("checkout")).click();

        //Verify the checkout information page is displayed
        String checkOutURL = driver.getCurrentUrl();
        if (checkOutURL.endsWith("/checkout-step-one.html")) {
            Assert.assertTrue(true);
            System.out.println(checkOutURL);
        } else {
            Assert.assertTrue(false);
        }

        //Enter the required checkout information (e.g., name, shipping address, payment details).
        driver.findElement(By.id(fName)).sendKeys(firstName);
        driver.findElement(By.id(lName)).sendKeys(lastName);
        driver.findElement(By.id(pCode)).sendKeys(postalCode);

        //Click on Continue button
        driver.findElement(By.id("continue")).click();

        //Verify that the order summary page is displayed, showing the T-shirt details and the total amount.
        String checkOutUrl = driver.getCurrentUrl();
        System.out.println(checkOutUrl);
        String totalPrice = driver.findElement(By.className("summary_total_label")).getText();
        System.out.println(totalPrice);

        //Click the finish button
        driver.findElement(By.id("finish")).click();

        //Verify that the order confirmation page is displayed, indicating a successful purchase.
        String expectedMessage = successOrder;
        String actualMessage = driver.findElement(By.cssSelector(".complete-header")).getText();

        Assert.assertEquals(actualMessage, expectedMessage);

        //Go back to home
        driver.findElement(By.id("back-to-products")).click();

        //Log out of the app
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(3000);

        //click on logout
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
        Thread.sleep(2000);

        //Close the browser
        driver.quit();
    }
}
