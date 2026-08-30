import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Video23 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void init() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void selectDropdownOption(String dropdownXpath, String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownXpath)));
        dropdown.click();

        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@role='listbox']//div[@role='option']")
        ));

        for (WebElement option : options) {
            if (option.getText().trim().equals(visibleText)) {
                option.click();
                break;
            }
        }
    }

    private void selectAutoSuggestOption(String inputXpath, String typeText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(inputXpath)));
        inputField.clear();
        inputField.sendKeys(typeText);

        try {
            Thread.sleep(1500); // 1.5 second ka pause taaki list fully load ho jaye
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Actions actions = new Actions(driver);
        actions.moveToElement(inputField)
                .sendKeys(Keys.ARROW_DOWN)
                .pause(Duration.ofMillis(1000)) // Thoda sa natural delay
                .sendKeys(Keys.ENTER)
                .perform();

        // 4. Value check karein
        String selectedItem = inputField.getAttribute("value");
        System.out.println("Selected Value after auto-suggest: " + selectedItem);
    }

    @Test
    public void toolTipTest() {
        driver.get("https://demoqa.com/tool-tips");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement targetElement = driver.findElement(By.id("toolTipButton"));


        Actions actions = new Actions(driver);
        actions.moveToElement(targetElement).perform();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tooltip = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("tooltip-inner"))
        );


        String tooltipText = tooltip.getText();

        String expectedText = "You hovered over the Button";
        assertEquals(tooltipText, expectedText);

        driver.quit();
    }


    @Test
    public void AutomateNewUserAdd() {
        // Launch the URL & Maximize
        driver.get("https://opensource-demo.orangehrmlive.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Login steps
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // Navigation to User Management
        WebElement adminElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Admin']")));
        adminElement.click();

        WebElement userManagement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='User Management']")));
        userManagement.click();

        WebElement usersElement = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Users")));
        usersElement.click();

        WebElement add = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Add']")));
        add.click();

        selectDropdownOption("(//div[contains(text(),'-- Select --')])[1]", "Admin");

        // Dynamic Auto-Suggest Input Box (Jaise Employee Name type karke select karna ho)
        selectAutoSuggestOption("//input[@placeholder='Type for hints...']", "ra");


        // Second Dropdown selection using DRY method
        selectDropdownOption("(//div[contains(text(),'-- Select --')])[1]", "Enabled");

        // Fill Username
        WebElement users2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='oxd-form-row']//div[@class='oxd-grid-2 orangehrm-full-width-grid']//div[@class='oxd-grid-item oxd-grid-item--gutters']//div[@class='oxd-input-group oxd-input-field-bottom-space']//div//input[@class='oxd-input oxd-input--active']")));
        users2.sendKeys("I don't Know!");

        // Fill Password & Confirm Password
        WebElement password = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[2]/div/div[1]/div/div[2]/input"));
        password.sendKeys("Nitin@123");

        WebElement confirmPassword = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[2]/div/div[2]/div/div[2]/input"));
        confirmPassword.sendKeys("Nitin@123");

        String currentURL = driver.getCurrentUrl();

        // Click Save
        WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Save']")));
        save.click();

        boolean savedSuccesfully = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"oxd-toaster_1\"]/div/div[1]/div[2]/p[2]"))).isDisplayed();
        System.out.println(savedSuccesfully);
        driver.quit();


    }
}