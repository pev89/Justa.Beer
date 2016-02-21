package widget;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.TestsBase;
import util.TestsConfig;


abstract public class WidgetTests extends TestsBase {
    
    WebElement widget = null;
    
    @AfterTest
    public void close_browser() {
        driver.quit();
    }
    
    
    @BeforeMethod
    public void DemoPageTests() {
        driver.get(TestsConfig.WIDGET_TEST_PAGE_URL);
        setBrowserSize(1000, 600);
        viewportSize = TestsConfig.getViewportSize(jse);
        
        widget = driver.findElement(By.className(TestsConfig.WIDGET_CSS_CLASS_NAME));
    }
    
    
    @Test
    public void widget_should_be_visible_and_enabled() {
        assertEquals(widget.isDisplayed(), true);
        assertEquals(widget.isEnabled(), true);
    }
    
    
    @Test
    public void widget_should_be_placed_in_correct_position() {
        int yScroll = 0;
        check_widget_position(yScroll);
    }
    
    
    @Test
    public void widget_should_keep_its_place_after_scrolling_down() {
        int yScroll = 200;
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("scroll(0, " + yScroll + ");");
        check_widget_position(yScroll);
    }
    
    
    private void check_widget_position(int yScroll) {
        int rightDistanceFromWindowBorder = 10;
        int bottomDistanceFromWindowBorder = 10;
        Dimension widgetSize = widget.getSize();
        int expectedX = viewportSize.width - widgetSize.width - rightDistanceFromWindowBorder;
        int expectedY = viewportSize.height - widgetSize.height - bottomDistanceFromWindowBorder + yScroll;
        
        Point widgetLocation = widget.getLocation();
        
        assertEquals(widgetLocation.y, expectedY);
        assertEquals(widgetLocation.x, expectedX);
    }
}
