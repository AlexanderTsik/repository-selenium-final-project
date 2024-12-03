package ge.swoop.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class HelperFunctions {

    // Utility method to retrieve all the prices from all pages
    public static List<Double> getAllPricesOnAllPages(WebDriver driver, WebDriverWait wait) {
        List<Double> prices = new ArrayList<>();
        boolean nextPage = true;

        while (nextPage) {
            // Extract prices from the current page
            List<WebElement> priceElements = driver.findElements(By.xpath("//div[contains(@class, 'flex flex-row items-center')]//h4[@type='h4' and not(contains(@class, 'line-through')) and contains(@class, 'text-primary_black')]"));

            for (WebElement priceElement : priceElements) {
                double price = Double.parseDouble(priceElement.getText().replaceAll("[^0-9.]", ""));
                if(price!=15 && price !=25) {
                    prices.add(price); // Add each price to the list
                }
            }

            // Check if the "Next Page" button is disabled (if we've reached the last page)
            try {
                WebElement nextPageDiv = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[contains(@class, 'h-9 w-9')]//img[@alt='right arrow']/parent::div")
                ));

                // If the button is disabled (contains opacity class), we've reached the last page
                if (nextPageDiv.getAttribute("class").contains("opacity-50")) {
                    nextPage = false; // Exit the loop
                } else {
                    // Wait for the "Next Page" button to be clickable and then click
                    WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@class, 'h-9 w-9')]//img[@alt='right arrow']")
                    ));
                    nextPageButton.click();
                }

                // Wait for the new page to load before continuing
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'flex flex-row items-center')]")));

            } catch (NoSuchElementException e) {
                System.out.println("Next page button not found. Exiting loop.");
                nextPage = false; // Exit the loop if the button is missing
            }
        }

        return prices; // Return the list of all collected prices
    }

    public static String rgbaToHex(String rgba) {
        // Remove 'rgba(' and ')' and split the values
        String[] rgbaValues = rgba.replace("rgba(", "").replace(")", "").split(",");

        // Parse the RGB values
        int r = Integer.parseInt(rgbaValues[0].trim());
        int g = Integer.parseInt(rgbaValues[1].trim());
        int b = Integer.parseInt(rgbaValues[2].trim());

        // Convert to hex
        return String.format("#%02X%02X%02X", r, g, b);
    }
}
