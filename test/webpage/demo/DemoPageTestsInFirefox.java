package webpage.demo;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;


public class DemoPageTestsInFirefox extends DemoPageTests {
    
    @BeforeTest
    public void open_browser() {
        driver = new FirefoxDriver();
        prepare();
    }
}
