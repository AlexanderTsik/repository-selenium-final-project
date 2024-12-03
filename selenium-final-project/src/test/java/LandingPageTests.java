import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static ge.swoop.data.Constants.*;

public class LandingPageTests extends BaseTest {

    //1) activeCategoryTest:
    //
    //     Click on  'კატეგორიები', choose 'სპორტი'->'კარტინგი' from the dropdown.
    //     Validate that the URL is https://www.swoop.ge/category/2058/sporti/kartingi/
    //     Validate that category chain (separated by > symbols) contains 'კარტინგი'
    @Test
    public void activeCategoryTest() {
        // Navigate to the base URL
        driver.get(BASE_URL);

        // Expected URL
        String expectedUrl = EXPECTED_CATEGORY_URL;

        // Wait until the 'სპორტი' link is clickable and click it
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
        WebElement sportLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(SPORT_LINK_TEXT)));
        sportLink.click();

        // Wait and ensure the page is loaded
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[h5[text()='კარტინგი']]")));

        // Find the kartingi link using multiple strategies
        WebElement kartingiLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[h5[text()='კარტინგი']]")
        ));

        // Scroll the element into view using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", kartingiLink);

        // Wait a moment after scrolling
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Try clicking with JavaScript if regular click fails
        try {
            kartingiLink.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", kartingiLink);
        }

        // Wait for URL to change
        wait.until(ExpectedConditions.urlContains("/category/2058/sporti/kartingi/"));

        // Get the actual URL
        String actualUrl = driver.getCurrentUrl();

        // Print expected and actual URLs
        System.out.println("Expected URL: " + expectedUrl);
        System.out.println("Actual URL: " + actualUrl);

        // Assert that the URLs match
        Assert.assertEquals( actualUrl, expectedUrl, "URLs do not match");

        WebElement breadcrumbNav = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//nav[@class='flex items-center flex-nowrap whitespace-nowrap py-2']")
        ));

        // Get the text of all breadcrumb links
        List<WebElement> breadcrumbLinks = breadcrumbNav.findElements(By.tagName("p"));

        // Convert to stream and map to text for easier validation
        String breadcrumbText = breadcrumbLinks.stream()
                .map(WebElement::getText)
                .collect(Collectors.joining(" > "));

        // Print the full breadcrumb text for debugging
        System.out.println("Breadcrumb Text: " + breadcrumbText);

        // Assert that the breadcrumb contains 'კარტინგი'
        Assert.assertTrue(breadcrumbText.contains("კარტინგი"),"Breadcrumb does not contain 'კარტინგი'");
    }

    //2) logoTest:
    //
    //     Go to 'დასვენება' section.
    //     Click on Swoop logo
    //     Validate that the logo takes the user back to landingPage


    @Test
    public void logoTest() {
        // Navigate to the base URL
        driver.get(BASE_URL);

        // Navigate to დასვენება section
        driver.findElement(By.linkText("დასვენება")).click();

        // Wait until the URL becomes the specific დასვენება category URL
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
        wait.until(ExpectedConditions.urlToBe("https://swoop.ge/category/24/dasveneba/"));

        // Find the Swoop logo
        WebElement logo = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/']/img[@alt='swoop']")
        ));

        // Click on the logo
        logo.click();

        // Wait for the page to load
        wait.until(ExpectedConditions.urlToBe("https://swoop.ge/"));

        // Validate that we're back to the landing page
        String landingPageUrl = "https://swoop.ge/";
        String actualUrl = driver.getCurrentUrl();

        // Print URLs for debugging
        System.out.println("Landing Page URL: " + landingPageUrl);
        System.out.println("Actual URL after logo click: " + actualUrl);

        // Assert that we're back to the landing page
        Assert.assertEquals( landingPageUrl, actualUrl, "Logo did not navigate to landing page");
    }
}
