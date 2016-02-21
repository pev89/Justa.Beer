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
    public static final int POSITION_ERROR = 1;
    public static final String WIDGET_CSS_CLASS_NAME = "jsb-widget";
    public static final String CHROME_DRIVER_PATH = "lib/chromedriver.exe";
    public static final String OPERA_DRIVER_PATH = "lib/operadriver.exe";
    
    // system specific config
    public static final String OPERA_LAUNCHER_PATH = "<<< path to 'Opera/launcher.exe' file >>>";
    public static final String WIDGET_TEST_PAGE_URL = "<<< widget/widget.html >>>";
    public static final String TEST_PAGE_URL = "<<< www/form-demo.html >>>";
    public static final String LINK_GENERATION_PAGE_URL = "<<< path to 'www/link-generation.html' page >>>";
    
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
