package tools;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitTools {

    private WebDriver driver;

    public WaitTools(WebDriver driver) {
        this.driver = driver;

    }

    public boolean waitForCondition(ExpectedCondition condition) {
        try {
            new WebDriverWait(driver, 30);
            return true;
        } catch(TimeoutException ignore) {
            return false;
        }
    }

}