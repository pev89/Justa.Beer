package util;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;


public class TestsConfig {
    public static final String CHROME_DRIVER_PATH = "lib/chromedriver.exe";
    public static final String OPERA_DRIVER_PATH = "lib/operadriver.exe";
    
    public static final String OPERA_LAUNCHER_PATH = "C:/Program Files (x86)/Opera/launcher.exe";
    
    public static final String WIDGET_TEST_PAGE_URL = "file:///Z:/Projects/justa.beer/Justa.Beer/widget/widget.html";
    public static final String TEST_PAGE_URL = "file:///Z:/Projects/justa.beer/Justa.Beer/www/form-demo.html";
    public static final String WIDGET_CSS_CLASS_NAME = "jsb-widget";
    public static final int POSITION_ERROR = 1;
    
    
    
    
    public static final void waitForElement(WebDriver driver, By element, int totalTime, int checkTimeDelta) {
        Wait<WebDriver> wait =
            new FluentWait<>(driver)
                .withTimeout(totalTime, TimeUnit.MILLISECONDS)
                .pollingEvery(checkTimeDelta, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(element));
    }
}
