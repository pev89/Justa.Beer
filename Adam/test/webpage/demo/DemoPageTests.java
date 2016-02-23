package webpage.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.TestsBase;
import util.TestsConfig;


abstract public class DemoPageTests extends TestsBase {
    
    WebElement formContainer = null;
    WebElement titleElem = null;
    WebElement inputElem = null;
    WebElement submitElem = null;
    
    
    @AfterTest
    public void close_browser() {
        driver.quit();
    }
    
    
    @BeforeMethod
    public void DemoPageTests() {
        driver.get(TestsConfig.WIDGET_DEMO_PAGE_URL);
        setBrowserSize(1000, 600);
        viewportSize = TestsConfig.getViewportSize(jse);
        
        formContainer = driver.findElement(By.id("formContainer"));
        titleElem = formContainer.findElement(By.tagName("div"));
        inputElem = formContainer.findElement(By.tagName("input"));
        submitElem = formContainer.findElement(By.tagName("button"));
    }
    
    
    @Test
    public void form_and_its_elements_are_centered() {
        int expectedFormLeft = (viewportSize.width - formContainer.getSize().width) / 2;
        int expectedTitleLeft = (viewportSize.width - titleElem.getSize().width) / 2;
        int expectedInputLeft = (viewportSize.width - inputElem.getSize().width) / 2;
        int expectedSubmitLeft = (viewportSize.width - submitElem.getSize().width) / 2;
        
        assertEquals(formContainer.getLocation().x, expectedFormLeft, TestsConfig.POSITION_ERROR);
        assertEquals(titleElem.getLocation().x, expectedTitleLeft, TestsConfig.POSITION_ERROR);
        assertEquals(inputElem.getLocation().x, expectedInputLeft, TestsConfig.POSITION_ERROR);
        assertEquals(submitElem.getLocation().x, expectedSubmitLeft, TestsConfig.POSITION_ERROR);
    }
    
    
    @Test
    public void submitting_empty_url_will_show_error_message_with_correct_elements_and_message_text() {
        inputElem.clear();
        submitElem.click();
        try {
            WebElement msgTextElem = driver.findElement(By.className("msg"));
            driver.findElement(By.className("bg"));
            driver.findElement(By.className("box")).findElement(By.tagName("button"));
            assertEquals(msgTextElem.getText(), "Please provide a URL!");
        } catch (Exception ex) {
            assertEquals(ex.getMessage(), "");
        }
    }
    
    
    @Test
    public void error_message_and_its_elements_will_be_in_a_right_position() {
        inputElem.clear();
        submitElem.click();
        
        WebElement msgTextElem = driver.findElement(By.className("msg"));
        WebElement msgBgElem = driver.findElement(By.className("bg"));
        WebElement msgCloseButton = driver.findElement(By.className("box")).findElement(By.tagName("button"));
        int expectedTextLeft = (viewportSize.width - msgTextElem.getSize().width) / 2;
        int expectedCloseButtonLeft = (viewportSize.width - msgCloseButton.getSize().width) / 2;
        Dimension expectedBgElelentSize = new Dimension(viewportSize.width, viewportSize.height);

        assertEquals(msgTextElem.getLocation().x, expectedTextLeft, TestsConfig.POSITION_ERROR);
        assertEquals(msgCloseButton.getLocation().x, expectedCloseButtonLeft, TestsConfig.POSITION_ERROR);
        assertEquals(msgBgElem.getLocation(), new Point(0, 0));
        assertEquals(msgBgElem.getSize(), expectedBgElelentSize);
    }
    
    
    @Test
    public void closes_error_message_on_close_button_click() {
        inputElem.clear();
        submitElem.click();
        WebElement msgCloseButton = driver.findElement(By.className("box")).findElement(By.tagName("button"));
        msgCloseButton.click();
        try {
            driver.findElement(By.id("msgContainer"));
            assertEquals("Message box should be closed!!!", "");
        } catch (Exception ex) {
            assertTrue(true);
        }
    }
    
    
    @Test
    public void after_submitting_url_hides_form_container_and_shows_iframe_with_back_button_and_widget() {
        submitExamplePage();
        assertThatIFrameIsOpenedAndWidgetIsVisible();
    }
    
    
    @Test
    public void iframe_size_and_back_button_position_are_correct() {
        submitExamplePage();
        WebElement iframe = driver.findElement(By.tagName("iframe"));
        WebElement iframeCloseButton = driver.findElement(By.id("iframeCloseButton"));
        
        assertEquals(iframe.getSize().width, viewportSize.width);
        assertEquals(iframeCloseButton.getLocation(), new Point(0, 0));
    }
    
    
    @Test
    public void clicking_page_back_button_will_hide_iframe_with_widget_and_show_form_again() {
        submitExamplePage();
        WebElement iframeCloseButton = driver.findElement(By.id("iframeCloseButton"));
        iframeCloseButton.click();
        assertThatIFrameIsClosedAndWidgetIsRemoved();
    }
    
    
    @Test
    public void pressing_browsers_back_button_will_hide_iframe_with_widget_and_show_form_again() {
        submitExamplePage();
        driver.navigate().back();
        assertThatIFrameIsClosedAndWidgetIsRemoved();
    }
    
    
    @Test
    public void pressing_enter_after_inserting_url_will_act_as_clicking_submit_button() {
        inputElem.clear();
        inputElem.sendKeys(TestsConfig.LINK_GENERATION_PAGE_URL);
        inputElem.sendKeys(Keys.RETURN);
        assertThatIFrameIsOpenedAndWidgetIsVisible();
    }
    
    
    private void assertThatIFrameIsOpenedAndWidgetIsVisible() {
        try {
            WebElement iframe = driver.findElement(By.tagName("iframe"));
            WebElement iframeCloseButton = driver.findElement(By.id("iframeCloseButton"));
            assertFalse(formContainer.isDisplayed());
            assertTrue(iframe.isDisplayed());
            assertTrue(iframeCloseButton.isDisplayed());
            TestsConfig.waitForElement(
                driver,
                By.className(TestsConfig.WIDGET_CSS_CLASS_NAME),
                10000, // total time [in milliseconds]
                1000 // check time delta [in milliseconds]
            );
        } catch (Exception ex) {
            assertEquals(ex, "");
        }
    }
    
    
    private void assertThatIFrameIsClosedAndWidgetIsRemoved() {
        assertTrue(formContainer.isDisplayed());
        try {
            driver.findElement(By.tagName("iframe"));
            assertEquals("Iframe is still visible!!!", "");
        } catch (Exception ex) {
            assertTrue(true);
        }
        try {
            driver.findElement(By.id("iframeCloseButton"));
            assertEquals("Back button is still visible!!!", "");
        } catch (Exception ex) {
            assertTrue(true);
        }
        try {
            driver.findElement(By.className(TestsConfig.WIDGET_CSS_CLASS_NAME));
            assertEquals("Widget is still visible!!!", "");
        } catch (Exception ex) {
            assertTrue(true);
        }
    }
    
    
    private void submitExamplePage() {
        inputElem.clear();
        inputElem.sendKeys(TestsConfig.LINK_GENERATION_PAGE_URL);
        submitElem.click();
    }
}
