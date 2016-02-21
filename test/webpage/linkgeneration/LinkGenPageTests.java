package webpage.linkgeneration;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.TestsConfig;


abstract public class LinkGenPageTests {
    
    WebDriver driver = null;
    Dimension viewportSize = null;
    WebElement containerEl = null;
    WebElement urlInput = null;
    WebElement codeField = null;
    private final String DEMO_PAGE_URL = "file:///Z:/Projects/justa.beer/Justa.Beer/www/link-generation.html";
    private JavascriptExecutor jse = null;
    
    
    @AfterTest
    public void close_browser() {
        driver.quit();
    }
    
    
    @BeforeMethod
    public void DemoPageTests() {
        driver.get(DEMO_PAGE_URL);
        driver.manage().window().setSize(new Dimension(1000, 600));
        
        containerEl = driver.findElement(By.className("formBox"));
        urlInput = driver.findElement(By.id("userUrl"));
        codeField = driver.findElement(By.id("generatedCode"));
        
        jse = (JavascriptExecutor) driver;
        updateViewportSize();
    }
    
    
    private void updateViewportSize() {
        viewportSize = new Dimension(
            Integer.parseInt(jse.executeScript("return document.documentElement.clientWidth").toString()),
            Integer.parseInt(jse.executeScript("return window.innerHeight").toString())
        );
    }
    
    
    @Test
    public void fileds_are_empty_after_page_load() {
        assertEquals(urlInput.getText(), "");
        assertEquals(codeField.getText(), "");
    }
    
    
    @Test
    public void main_container_has_proper_size() {
        int WIDTH_PERCENTAGE = 80;
        int expectedContainerWidth = (int) viewportSize.width * WIDTH_PERCENTAGE / 100;
        
        assertEquals(containerEl.getSize().width, expectedContainerWidth, TestsConfig.POSITION_ERROR);
    }
    
    
    @Test
    public void main_container_has_proper_size_on_small_screens() {
        driver.manage().window().setSize(new Dimension(200, 600));
        updateViewportSize();
        assertEquals(containerEl.getSize().width, viewportSize.width);
    }
    
    
    @Test
    public void inserting_white_characters_like_space_at_the_beginning_or_end_will_trim_the_url() {
        String url = "example.com";
        
        urlInput.clear();
        urlInput.sendKeys(" ");
        assertEquals(urlInput.getAttribute("value"), "");
        urlInput.clear();
        urlInput.sendKeys("\t");
        assertEquals(urlInput.getAttribute("value"), "");
        urlInput.clear();
        urlInput.sendKeys("\r");
        assertEquals(urlInput.getAttribute("value"), "");
        urlInput.clear();
        urlInput.sendKeys(" " + url + " ");
        assertEquals(urlInput.getAttribute("value"), url);
    }
    
    
    /* This test needs to be enhanced since disabling the caret check in "link-generation.js" will pass this test
    @Test
    public void curser_pointer_does_not_change_place_after_user_url_trimming() {
        urlInput.clear();
        urlInput.sendKeys("ab");
        urlInput.sendKeys(Keys.LEFT);
        urlInput.sendKeys(Keys.LEFT);
        urlInput.sendKeys("cd");
        System.out.println(urlInput.getAttribute("value"));
        assertEquals(urlInput.getAttribute("value"), "cdab");
    }*/
    
    
    @Test
    public void typing_text_into_user_url_input_will_correctly_generates_code() {
        String url = "http://example.com";
        
        urlInput.clear();
        urlInput.sendKeys(url);
        
        assertEquals(codeField.getAttribute("value"), getExpectedCode(url));
    }
    
    
    @Test
    public void clearing_url_field_will_clear_code_field() {
        String url = "a";
        
        urlInput.clear();
        urlInput.sendKeys(url);
        urlInput.sendKeys(Keys.BACK_SPACE);
        assertEquals(codeField.getAttribute("value"), "");
    }
    
    
    private String getExpectedCode(String url) {
        return "<script>document.write(\"<script type='text/javascript' src='js/widget.js?z=\" + Date.now() + \"&url=" + url + "'><\\/script>\");</script>";
    }
}
