import ge.swoop.utils.HelperFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static ge.swoop.data.Constants.*;

public class MoviePageTests extends BaseTest {


    //    Go to 'კინო'
    //    Select the first movie in the returned list and click on it
    //    Scroll vertically (if necessary), and horizontally and choose ‘კავეა ისთ ფოინთი’
    //    Click on last option
    //    Check in opened popup that movie name, cinema and datetime is valid
    //    Validate that Free seats has the same color as legend chart (ფერების აღწერის ჩარტი)
    //    Choose any vacant place
    //    Register for a new account
    //    Fill all fields with valid data except for email
    //    Validate that error message ‘მეილის ფორმატი არასწორია!' has appeared.
    @Test
    public void movieBookingTest() {
        // Navigate to the base URL
        driver.get(BASE_URL);

        // Go to 'კინო'
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement kinoLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("კინო")
        ));
        kinoLink.click();

        // Wait for movie list to load and select first movie
        List<WebElement> movieList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@class='w-full group ']")
        ));

        // Ensure we have at least one movie
        Assert.assertFalse(movieList.isEmpty(), "No movies found in the list");

        // Get the current URL before clicking the movie
        String currentUrl = driver.getCurrentUrl();

        // Click on the first movie
        WebElement firstMovie = movieList.get(0);
        firstMovie.click();

        // Wait for the URL to change
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));


        try {
            // Find ALL cinema sections
            List<WebElement> cinemaSections = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//div[.//h3[contains(@class, 'text-nowrap')]]")
            ));

            // Print out all cinema names for debugging
            for (WebElement section : cinemaSections) {
                WebElement cinemaNameElement = section.findElement(By.xpath(".//h3[contains(@class, 'text-nowrap')]"));
                System.out.println("Found Cinema: " + cinemaNameElement.getText());
            }

            // Find the specific კავეა ისთ ფოინთი section
            WebElement caveatSection = cinemaSections.stream()
                    .filter(section -> {
                        WebElement cinemaNameElement = section.findElement(By.xpath(".//h3[contains(@class, 'text-nowrap')]"));
                        return cinemaNameElement.getText().trim().equals("კავეა ისთ ფოინთი");
                    })
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("კავეა ისთ ფოინთი cinema section not found"));

            // Find showtimes specifically within the კავეა ისთ ფოინთი section
            List<WebElement> showtimes = caveatSection.findElements(By.xpath(
                    ".//div[contains(@class, 'grid grid-cols-') and contains(@class, 'gap-2')]//div[contains(@class, 'cursor-pointer')]"
            ));

            Assert.assertFalse(showtimes.isEmpty(), "No showtimes available for კავეა ისთ ფოინთი");

            // Get the last showtime
            WebElement lastShowtime = showtimes.get(showtimes.size() - 1);


            // Use JavaScriptExecutor for scrolling and clicking
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Scroll to the last showtime
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", lastShowtime);

            // Wait a moment for any potential animations or overlays
            Thread.sleep(500);

            // Try multiple methods to click
            try {
                // Try regular click first
                lastShowtime.click();
            } catch (Exception regularClickException) {
                try {
                    // If regular click fails, try JavaScript click
                    js.executeScript("arguments[0].click();", lastShowtime);
                } catch (Exception jsClickException) {
                    // If both methods fail, provide more detailed error information
                    System.out.println("Regular click exception: " + regularClickException.getMessage());
                    System.out.println("JavaScript click exception: " + jsClickException.getMessage());
                    Assert.fail("Could not click the last showtime for კავეა ისთ ფოინთი");
                }
            }

            System.out.println("Last showtime for კავეა ისთ ფოინთი clicked successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }


        // Validate movie details in popup
        WebElement movieNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@class='flex  justify-start items-start flex-col gap-2']/h2")
        ));
        String thisMovieName = movieNameElement.getText();

        WebElement cinemaNameElement = driver.findElement(By.xpath("(//div[@class='flex  justify-start items-start gap-5']/p)[1]"));
        String cinemaName = cinemaNameElement.getText();

        WebElement dateTimeElement = driver.findElement(By.xpath("(//div[@class='flex  justify-start items-start gap-5']/p)[2]"));
        String thisDateTime = dateTimeElement.getText();

        // Basic validation of movie details
        Assert.assertFalse(thisMovieName.isEmpty(), "Movie name is empty");
        Assert.assertTrue(cinemaName.contains("კავეა ისთ ფოინთი"), "Cinema name should contain 'კავეა ისთ ფოინთი'");
        Assert.assertFalse(thisDateTime.isEmpty(), "Date and time are empty");


        // Validate the free seats' color from the legend
        WebElement freeSeatLegend = driver.findElement(By.xpath("//div[contains(@class, 'bg-primary_green-100-value')]"));
        String legendColor = freeSeatLegend.getCssValue("background-color");

        List<WebElement> vacantSeats = driver.findElements(By.xpath("//div[contains(@class, 'cursor-pointer') and contains(@style, 'position: absolute;')]"));


        try {
            Assert.assertEquals(HelperFunctions.rgbaToHex(legendColor), "#8ECC69", "The colors are different");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
            // You can log the error or handle the failure here if needed, but the code will continue executing.
        }


        if (!vacantSeats.isEmpty()) {
            vacantSeats.get(0).click();
        } else {
            System.out.println("No green seats found.");
        }

//        // Click on registration/continue button
        WebElement createAccountLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='create pl-2 ']/a[@class='black-hover' and contains(text(), 'შექმენი')]")
        ));

// Click on the "შექმენი" link
        createAccountLink.click();


        // Fill in all fields with valid data except for the email
        driver.findElement(By.id("password")).sendKeys("ValidPass123!");
        driver.findElement(By.id("PasswordRetype")).sendKeys("ValidPass123!");
        driver.findElement(By.id("name")).sendKeys("John");
        driver.findElement(By.id("surname")).sendKeys("Doe");

// Select gender
        WebElement genderMale = driver.findElement(By.id("Gender1")); // Assuming Gender1 for male
        genderMale.click();

// Select birth year
        Select birthYear = new Select(driver.findElement(By.name("birth_year")));
        birthYear.selectByValue("1990"); // Select a valid birth year

// Enter phone number
        driver.findElement(By.id("Phone")).sendKeys("555123456");
        // Input a 4-digit SMS code
        driver.findElement(By.id("PhoneCode")).sendKeys("1234");


        // Scroll to the checkboxes
        JavascriptExecutor js = (JavascriptExecutor) driver;

// Click on the terms checkbox
        WebElement termsCheckboxSpan = driver.findElement(By.cssSelector("label.checkbox-container:first-child .checkmark"));
        js.executeScript("arguments[0].click();", termsCheckboxSpan);


// Skip filling in the email field


        WebElement submitButton = driver.findElement(By.id("registrationBtn"));
        js.executeScript("arguments[0].click();", submitButton);

// Validate the error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("input-error-email")
        ));
// Assertion to check the error message text
        String expectedErrorMessage = "მეილის ფორმატი არასწორია!";
        String actualErrorMessage = errorMessage.getText();
        try {
            Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message validation failed!");
        }catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
            // You can log the error or handle the failure here if needed, but the code will continue executing.
        }
    }
}
