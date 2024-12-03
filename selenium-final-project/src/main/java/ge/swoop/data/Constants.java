package ge.swoop.data;

public class Constants {

    public static final String BASE_URL = "https://swoop.ge/";
    public static final String EXPECTED_CATEGORY_URL = BASE_URL + "category/2058/sporti/kartingi/";
    public static final int WAIT_TIMEOUT_SECONDS = 10;
    public static final String SECTION_LINK_TEXT = "დასვენება";
    public static final String MOUNTAIN_RESORTS_LINK_TEXT = "მთის კურორტები";
    public static final String FULL_PAYMENT_FILTER_TEXT = "სრული გადახდა";

    public static final String SPORT_LINK_TEXT = "სპორტი";

    // XPATH Constants
    public static final String SORT_MENU_XPATH = "//div[@class='relative z-40']";
    public static final String DESCENDING_SORT_XPATH = "//p[contains(text(), 'ფასით კლებადი')]";
    public static final String ASCENDING_SORT_XPATH = "//p[contains(text(), 'ფასით ზრდადი')]";
    public static final String PRICE_ELEMENTS_XPATH = "//div[contains(@class, 'flex flex-row items-center')]//h4[@type='h4' and not(contains(@class, 'line-through')) and contains(@class, 'text-primary_black')]";
    public static final String FILTER_TAG_XPATH = "//div[@class='items-center flex-wrap gap-3 hidden laptop:flex']";
    public static final String FULL_PAYMENT_LABEL_XPATH = "//label[.//span[text()='სრული გადახდა']]";

    // Price Range Input XPATHs
    public static final String MIN_PRICE_INPUT_XPATH = "//p[text()='დან']/following-sibling::input";
    public static final String MAX_PRICE_INPUT_XPATH = "//p[text()='მდე']/following-sibling::input";
    public static final String PRICE_RANGE_APPLY_BUTTON_XPATH = "//button[@data-testid='secondary-button']";


}
