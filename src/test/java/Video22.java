import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Video22 {
    private WebDriver driver;

    @Before
    public void init() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void testKeyEventsWithShiftKey() {
        driver.get("https://demoqa.com/auto-complete/");
        driver.manage().window().maximize();

        WebElement element = driver.findElement(By.xpath("//div[@class='auto-complete__value-container auto-complete__value-container--is-multi css-hlgwow']//div[@class='auto-complete__input-container css-19bb58m']"));
        Actions actions = new Actions(driver);
        actions.keyDown(element, Keys.SHIFT);
        actions.sendKeys("We succeed when our efforts are more than our excuses");
        actions.keyUp(Keys.SHIFT);
        actions.build().perform();
        driver.quit();
    }

    @Test
    public void testKeyEventsForCtrlCCtrlV() {
        driver.get("https://demoqa.com/text-box");
        driver.manage().window().maximize();

        Actions actions = new Actions(driver);

        // Enter Full Name
        WebElement fullName = driver.findElement(By.id("userName"));
        fullName.sendKeys("Mr.Peter Haynes");

        // Enter Email
        WebElement email = driver.findElement(By.id("userEmail"));
        email.sendKeys("PeterHaynes@toolsqa.com");

        // Enter Current Address
        WebElement currentAddress = driver.findElement(By.id("currentAddress"));
        currentAddress.sendKeys("43 School Lane London EC71 9G0");

        // Select Current Address using CTRL + A
        actions.keyDown(currentAddress, Keys.CONTROL);
        actions.sendKeys("a");
        actions.keyUp(currentAddress, Keys.CONTROL);
        actions.build().perform();

        // Copy Current Address using CTRL + C
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("c");
        actions.keyUp(Keys.CONTROL);
        actions.build().perform();

        // Press TAB key to switch focus to Permanent Address
        actions.sendKeys(Keys.TAB);
        actions.build().perform();

        // Paste Address in Permanent Address field using CTRL + V
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("v");
        actions.keyUp(Keys.CONTROL);
        actions.build().perform();

        // Compare Text of Current Address and Permanent Address
        WebElement permanentAddress = driver.findElement(By.id("permanentAddress"));
        assertEquals(currentAddress.getAttribute("value"), permanentAddress.getAttribute("value"));
        driver.quit();
    }

    @Test
    public void testMouseRightClick() {
        driver.get("https://swisnl.github.io/jQuery-contextMenu/demo.html");
        driver.manage().window().maximize();

        Actions actions = new Actions(driver);

        // Retrieve WebElement to perform right click
        WebElement btnElement = driver.findElement(By.xpath("//span[@class='context-menu-one btn btn-neutral']"));

        // Right click the button to display Context Menu
        actions.contextClick(btnElement).perform();
        System.out.println("Right click Context Menu displayed");

        // Select and click 'Copy' from context menu
        WebElement elementOpen = driver.findElement(By.xpath("//span[text()='Copy']"));
        elementOpen.click();

        // Accept the Alert
        driver.switchTo().alert().accept();
        System.out.println("Right click Alert Accepted");
        driver.quit();
    }

    @Test
    public void testDragAndDrop() {
        String URL = "https://demoqa.com/droppable/";
        driver.get(URL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));

        Actions action = new Actions(driver);
        WebElement from = driver.findElement(By.id("draggable"));
        WebElement to = driver.findElement(By.id("droppable"));

        // Perform drag and drop
        action.dragAndDrop(from, to).perform();
        // Alternative method: action.clickAndHold(from).moveToElement(to).release().build().perform();

        // Verify text changed into 'Drop here' box
        String textTo = to.getText();
        assertEquals("Dropped!", textTo);
        driver.quit();
    }

    @Test
    public void testDragAndDropBy() {
        String URL = "https://demoqa.com/slider";
        driver.get(URL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));

        Actions action = new Actions(driver);
        WebElement from = driver.findElement(By.xpath("//input[@id='slider']"));
        WebElement movebyvalue = driver.findElement(By.xpath("//input[@id='sliderValue']"));

        int before = Integer.parseInt(movebyvalue.getAttribute("value"));
        int xOffset = 50;

        // Perform drag and drop By
        action.dragAndDropBy(from, xOffset, 0).perform();

        int after = Integer.parseInt(movebyvalue.getAttribute("value"));
        System.out.println("Before: " + before + " | After: " + after);

        // Assert ki value change hui hai (badh gayi hai)
        assertNotEquals(before, after);

        driver.quit();
    }
}