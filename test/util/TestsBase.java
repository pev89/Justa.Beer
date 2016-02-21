package util;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;


abstract public class TestsBase {
    
    protected WebDriver driver = null;
    protected Dimension viewportSize = null;
    protected JavascriptExecutor jse = null;
    
    
    protected void prepare() {
        jse = (JavascriptExecutor) driver;
    }
    
    
    protected void setBrowserSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }
}
