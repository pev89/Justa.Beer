package widget;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;


public class WidgetTestsInFirefox extends WidgetTests {
    
    @BeforeTest
    public void open_browser() {
        driver = new FirefoxDriver();
        prepare();
    }
}
