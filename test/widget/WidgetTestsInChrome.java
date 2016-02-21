package widget;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import util.TestsConfig;


public class WidgetTestsInChrome extends WidgetTests {
    
    @BeforeTest
    public void open_browser() {
        System.setProperty("webdriver.chrome.driver", TestsConfig.CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        prepare();
    }
}
