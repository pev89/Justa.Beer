package widget;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;
import util.TestsConfig;


public class WidgetTestsInOpera extends WidgetTests {
    
    @BeforeTest
    public void open_browser() {
        System.setProperty("webdriver.opera.driver", TestsConfig.OPERA_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(TestsConfig.OPERA_LAUNCHER_PATH);  
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new OperaDriver();
        prepare();
    }
}
