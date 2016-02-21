package webpage.linkgeneration;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;


public class LinkGenPageTestsInFirefox extends LinkGenPageTests {
    
    @BeforeTest
    public void open_browser() {
        driver = new FirefoxDriver();
    }
}
