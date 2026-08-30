import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class DatePicker {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testDynamicDatePicker() {
        driver.get("https://demoqa.com/date-picker");

        // 1. Target date string ko split() karke Day, Month, aur Year alag alag store karna
        String targetDateInput = "6-August-2027";
        String[] dateParts = targetDateInput.split("-");

        String targetDay = dateParts[0];     // "6"
        String targetMonth = dateParts[1];   // "August"
        String targetYear = dateParts[2];    // "2026"

        // 2. Date input box par click karke calendar widget open karna
        WebElement dateInputBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("datePickerMonthYearInput"))
        );
        dateInputBox.click();

        // 3. Dropdowns se Month aur Year select karna (DemoQA specific elements)
        WebElement monthDropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker__month-select"))
        );
        Select selectMonth = new Select(monthDropdown);
        selectMonth.selectByVisibleText(targetMonth);

        WebElement yearDropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker__year-select"))
        );
        Select selectYear = new Select(yearDropdown);
        selectYear.selectByVisibleText(targetYear);

           while(true) {
               String currentMonthYear = driver.findElement(By.className("react-datepicker__current-month")).getText();
               if(currentMonthYear.contains(targetMonth) && currentMonthYear.contains(targetYear)) {
                   break;
               }
               driver.findElement(By.xpath("//button[text()='Next Month']")).click();
           }

        // 4. Calendar ki saari dates ko findElements se list me store karna
        // (Yeh wo dates hain jo active month mein dikh rahi hain)
        List<WebElement> allDates = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'react-datepicker__day--outside-month'))]")
                )
        );

        // 5. Enhanced for-loop ka use karke specific date par click karna
        for (WebElement element : allDates) {
            if (element.getText().equals(targetDay)) {
                element.click();
                break;
            }
        }

        System.out.println("Target Date " + targetDateInput + " successfully select ho gayi hai!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}