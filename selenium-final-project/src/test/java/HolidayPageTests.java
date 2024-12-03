
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static ge.swoop.data.Constants.*;
import static ge.swoop.utils.HelperFunctions.getAllPricesOnAllPages;

public class HolidayPageTests extends BaseTest {

    //1) descendingOrderTest:
    // - Go to 'დასვენება' section.
    // - Find the most expensive offer among ALL offers.
    // - Proceed to sort offers from most expensive to least expensive.
    // - Validate that the most expensive offer is displayed first in the list.

    @Test
    public void descendingOrderTests() {
        driver.get(BASE_URL);
        driver.findElement(By.linkText(SECTION_LINK_TEXT)).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));

        // Use the utility function to get all prices on all pages
        List<Double> allPrices = getAllPricesOnAllPages(driver, wait);

        // Find the most expensive price
        double maxPrice = allPrices.stream()
                .max(Double::compareTo)
                .orElse(0.0); // Default to 0 if no prices were found
        System.out.println("Max price: " + maxPrice);

        // Now, sort the offers by most expensive to least expensive
        WebElement sortMenuButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SORT_MENU_XPATH)));
        sortMenuButton.click();

        // Click on "ფასით კლებადი" (Descending by price)
        WebElement sortDescButton = driver.findElement(By.xpath(DESCENDING_SORT_XPATH));
        sortDescButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(PRICE_ELEMENTS_XPATH)
        ));

        // Validate that the first offer on the page matches the most expensive price
        WebElement firstOfferPriceAfterSorting = driver.findElement(By.xpath("(" + PRICE_ELEMENTS_XPATH + ")[1]"));
        double displayedPrice = Double.parseDouble(firstOfferPriceAfterSorting.getText().replaceAll("[^0-9.]", ""));
        System.out.println("First after sorting: " + displayedPrice);

        // Assert that the most expensive offer is now displayed first
        Assert.assertEquals(displayedPrice, maxPrice, "Most expensive offer is not displayed first!");
    }
    //2) ascendingOrderTest:
    //
    //     Go to 'დასვენება' section.
    //     Find the least expensive offer among ALL offers.
    //     Proceed to sort offers from least expensive to most expensive.
    //     Validate that the least expensive offer is displayed first in the list.

    @Test
    public void ascendingOrderTests() {
        driver.get(BASE_URL);
        driver.findElement(By.linkText(SECTION_LINK_TEXT)).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));

        // Get all prices
        List<Double> allPrices = getAllPricesOnAllPages(driver, wait);
        double minPrice = allPrices.stream().min(Double::compare).get();
        System.out.println("Current min price: " + minPrice);

        // Now, sort the offers by least expensive to most expensive
        WebElement sortMenuButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SORT_MENU_XPATH)));
        sortMenuButton.click();

        // Click on "ფასით ზრდადი" (Ascending by price)
        WebElement sortAscButton = driver.findElement(By.xpath(ASCENDING_SORT_XPATH));
        sortAscButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(PRICE_ELEMENTS_XPATH)
        ));

        // Validate that the first offer on the page matches the cheapest price
        WebElement firstOfferPriceAfterSorting = driver.findElement(By.xpath("(" + PRICE_ELEMENTS_XPATH + ")[1]"));
        double displayedPrice = Double.parseDouble(firstOfferPriceAfterSorting.getText().replaceAll("[^0-9.]", ""));
        System.out.println("First after sorting " + displayedPrice);

        // Assert that the least expensive offer is now displayed first
        Assert.assertEquals(displayedPrice, minPrice, "Least expensive offer is not displayed first!");
    }

    //3) filterTest:
    //
    //     Go to 'დასვენება' section.
    //     Choose 'მთის კურორტები' category on the left side.
    //     Choose 'სრული გადახდა' filter.
    //     Check that 'სრული გადახდა' tag appears above the list of offers.
    //     Proceed to sort offers from least expensive to most expensive.
    //     Validate that the least expensive offer is displayed first in the list.

    @Test
    public void filterTestss() {
        driver.get(BASE_URL);
        driver.findElement(By.linkText(SECTION_LINK_TEXT)).click();
        driver.findElement(By.linkText(MOUNTAIN_RESORTS_LINK_TEXT)).click();
        driver.findElement(By.xpath(FULL_PAYMENT_LABEL_XPATH)).click();

        String divText = driver.findElement(By.xpath(FILTER_TAG_XPATH)).getText();
        Assert.assertTrue(divText.contains(FULL_PAYMENT_FILTER_TEXT), "The text '" + FULL_PAYMENT_FILTER_TEXT + "' is not present in the element!");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
        // Get all prices
        List<Double> allPrices = getAllPricesOnAllPages(driver, wait);
        double minPrice = allPrices.stream().min(Double::compare).get();
        System.out.println("Current min price: " + minPrice);

        // Verify that the filtered results contain the lowest price
        List<WebElement> filteredPriceElements = driver.findElements(By.xpath(PRICE_ELEMENTS_XPATH));
        boolean isMinPriceFound = false;
        for (WebElement priceElement : filteredPriceElements) {
            double price = Double.parseDouble(priceElement.getText().replaceAll("[^0-9.]", ""));
            if (price == minPrice) {
                isMinPriceFound = true;
                break;
            }
        }

        Assert.assertTrue(isMinPriceFound, "The filtered results do not contain the lowest price!");
    }

    //4) priceRangeTest:
    //
    //     Go to 'დასვენება' section.
    //     Specify price range of your choice on the left side.
    //     Validate that ALL offers fall under the price range you specified.

    @Test
    public void priceRangeTests() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));

        // Navigate to "დასვენება" and open price range inputs
        driver.get(BASE_URL);
        driver.findElement(By.linkText(SECTION_LINK_TEXT)).click();

        // Specify price range
        int minPrice = 50, maxPrice = 100;
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MIN_PRICE_INPUT_XPATH))).sendKeys(String.valueOf(minPrice));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MAX_PRICE_INPUT_XPATH))).sendKeys(String.valueOf(maxPrice));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(PRICE_RANGE_APPLY_BUTTON_XPATH))).click();

        // Get all prices
        List<Double> allPrices = getAllPricesOnAllPages(driver, wait);

        // Validate that all prices are within the specified range
        for (double price : allPrices) {
            Assert.assertTrue(price >= minPrice && price <= maxPrice, "Price " + price + " is out of range! Must be between " + minPrice + " and " + maxPrice);
        }

        System.out.println("All prices are within the specified range of " + minPrice + " to " + maxPrice);
    }


}
