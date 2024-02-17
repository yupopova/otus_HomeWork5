import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tools.WaitTools;

public class OtusPersonalDataTest {

    private Logger logger ;
    private WebDriver driver;
    private WaitTools waitTools;

    private final String baseUrl = System.getProperty("base.url");
    private final String login = System.getProperty("login");
    private final String password = System.getProperty("password");

    @BeforeEach
    public void initDriver() {
        driver = new DriverFactory("--start-fullscrean").create();
        logger = LogManager.getLogger(OtusPersonalDataTest.class);
        logger.info("Start driver and open browser");
        waitTools = new WaitTools(driver);
        driver.get(baseUrl);
        logger.info("Open url");
    }

    @AfterEach
    public void driverQuit() {
        if (driver != null) {
            logger.info("Close browser");
            driver.close();
            driver.quit();
        }
    }

    private void auth() {
        String signInButtonLocator = "//button[text()='Войти']";

        waitTools.waitForCondition(ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated((By.xpath(signInButtonLocator))),
                ExpectedConditions.elementToBeClickable(By.xpath(signInButtonLocator))
        ));


        WebElement signInButton = driver.findElement(By.xpath(signInButtonLocator));

        String signInPopupSelector = "#__PORTAL__ > div";
        Assertions.assertTrue(waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(signInPopupSelector)))),
                "Error! SignInPopup is opened");

        signInButton.click();

        WebElement authPopupElement = driver.findElement(By.cssSelector(signInPopupSelector));

        Assertions.assertTrue(waitTools.waitForCondition(ExpectedConditions.visibilityOf(authPopupElement)),
                "Error! SignInPopup is not visible");

        driver.findElement(By.xpath("//div[./input[@name='email']]")).click();

        WebElement emailInputField = driver.findElement(By.cssSelector("input[name='email']"));
        waitTools.waitForCondition(ExpectedConditions.visibilityOf(emailInputField));

        emailInputField.sendKeys(login);
        logger.info("Login entered");

        WebElement passwordInputField = driver.findElement(By.cssSelector("input[type='password']"));

        driver.findElement(By.xpath("//div[./input[@type='password']]")).click();
        waitTools.waitForCondition(ExpectedConditions.visibilityOf(passwordInputField));
        passwordInputField.sendKeys(password);
        logger.info("Password entered");

        driver.findElement(By.cssSelector("#__PORTAL__ button")).click();

        Assertions.assertTrue(waitTools
                .waitForCondition(ExpectedConditions
                        .presenceOfElementLocated(By.cssSelector("img[src*='blue-owl']"))));
        logger.info("Successful authorization");
    }

    private void openMenuAboutMe() {
        String openUserMenuLocator = "//div[@class='sc-i28ik1-0 bmVffP sc-1youhxc-0 dwrtLP']";

        waitTools.waitForCondition(ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(By.xpath(openUserMenuLocator)),
                ExpectedConditions.elementToBeClickable(By.xpath(openUserMenuLocator))
        ));

        WebElement openUserMenu = driver.findElement(By.xpath(openUserMenuLocator));
        openUserMenu.click();
        logger.info("User menu is open");

        driver.findElement(By.xpath("//*[contains(@href, 'learning')][.='Личный кабинет']")).click();
        logger.info("Personal cabinet is open");

        String MenuAboutMe = "//*[contains(@href, 'personal')][contains(@title, 'О себе')]";

        waitTools.waitForCondition(ExpectedConditions.elementToBeClickable(By.xpath(MenuAboutMe)));
        driver.findElement(By.xpath(MenuAboutMe)).click();
        logger.info("Menu About Me is open");

        if (!driver.findElements(By.xpath("//div[@class='modal modal_slim modal_no-full lk-invites-modal-agreement']")).isEmpty()) {
            logger.info("Modal window about publication resume is open");
            driver.findElement(By.name("agreement")).click();
            logger.info("Agreement about publication resume is done");
        } else {
            logger.info("Modal window about about publication resume is not display"); }
    }

    @Test
    public void inputTest() {
        auth();
        openMenuAboutMe();
        WebElement name = driver.findElement(By.id("id_fname"));
        name.clear();
        name.sendKeys("Тест");
        logger.info("Name entered");

        WebElement nameLatin = driver.findElement(By.id("id_fname_latin"));
        nameLatin.clear();
        nameLatin.sendKeys("Test");
        logger.info("Latin name entered");

        WebElement surname = driver.findElement(By.id("id_lname"));
        surname.clear();
        surname.sendKeys("Тестовин");
        logger.info("Surname entered");

        WebElement surnameLatin = driver.findElement(By.id("id_lname_latin"));
        surnameLatin.clear();
        surnameLatin.sendKeys("Testovin");
        logger.info("Latin surname entered");

        WebElement blogname = driver.findElement(By.id("id_blog_name"));
        blogname.clear();
        blogname.sendKeys("Blog_name");
        logger.info("Blog name entered");

        WebElement birth = driver.findElement(By.name("date_of_birth"));
        birth.clear();
        birth.sendKeys("01.01.2000");
        logger.info("Date of birth entered");

        WebElement countrySelectElement = driver.findElement(By.cssSelector("[data-slave-selector='.js-lk-cv-dependent-slave-city']"));
        countrySelectElement.click();
        WebElement countryListContainer = countrySelectElement
                .findElement(By.xpath(".//*[contains(@class, 'js-custom-select-options-container')]"));
        waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions
                .attributeContains(countryListContainer, "class", "hide")));
        driver.findElement(By.cssSelector("[title='Россия']")).click();
        waitTools.waitForCondition(ExpectedConditions
                .attributeContains(countryListContainer, "class", "hide"));
        logger.info("Country entered");

        WebElement citySelectElement = driver.findElement(By.xpath("//*[contains(@class, 'js-lk-cv-dependent-slave-city')]"));
        citySelectElement.click();

        WebElement cityListContainer = citySelectElement
                .findElement(By.xpath(".//*[contains(@class, 'js-custom-select-options-container')]"));
        waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions
                .attributeContains(cityListContainer, "class", "hide")));
        driver.findElement(By.cssSelector("[title='Москва']")).click();
        waitTools.waitForCondition(ExpectedConditions.attributeContains(cityListContainer, "class", "hide"));
        logger.info("City entered");

        driver.findElement(By.xpath("//*[contains(@class, 'select lk-cv-block__input lk-cv-block__input_full js-lk-cv-custom-select')]")).click();
        driver.findElement(By.cssSelector("[title='Продвинутый (Advanced)']")).click();
        logger.info("Level of English entered");

        if (!driver.findElements(By.xpath("//*[contains(@class, 'placeholder')][.='Способ связи']")).isEmpty()) {
            driver.findElement(By.xpath("//*[contains(@class, 'placeholder')][.='Способ связи']")).click();
            driver.findElement(By.xpath("//button[@title='Тelegram']")).click();
            driver.findElement(By.id("id_contact-0-value")).sendKeys("@Test_telegram");
            logger.info("Telegram entered");
            driver.findElement(By.xpath("//button[.='Добавить']")).click();
            driver.findElement(By.xpath("//*[contains(@class, 'placeholder')][.='Способ связи']")).click();
            driver.findElement(By.xpath("(//*[contains(@class, 'lk-cv-block__select-option_selected')][@title='WhatsApp']")).click();
            driver.findElement(By.id("id_contact-1-value")).sendKeys("+79998887766");
            logger.info("WhatsApp entered");
        } else {
            logger.info("Contacts already exists"); }

        driver.findElement(By.name("continue")).click();

        Assertions.assertTrue(waitTools
                .waitForCondition(ExpectedConditions
                        .presenceOfElementLocated(By.xpath("//*[contains(@class, 'success')]"))));
        logger.info("Successful input");

    }

    @Test
    public void verifyTest() {
        auth();
        openMenuAboutMe();
        Assertions.assertEquals("Тест", driver.findElement(By.id("id_fname")).getAttribute("value"));
        logger.info("Name is check");
        Assertions.assertEquals("Test", driver.findElement(By.id("id_fname_latin")).getAttribute("value"));
        logger.info("Latin name is check");
        Assertions.assertEquals("Тестовин", driver.findElement(By.id("id_lname")).getAttribute("value"));
        logger.info("Surname is check");
        Assertions.assertEquals("Testovin", driver.findElement(By.id("id_lname_latin")).getAttribute("value"));
        logger.info("Latin surname is check");
        Assertions.assertEquals("Blog_name", driver.findElement(By.id("id_blog_name")).getAttribute("value"));
        logger.info("Blog_name is check");
        Assertions.assertEquals("01.01.2000", driver.findElement(By.name("date_of_birth")).getAttribute("value"));
        logger.info("Date of birth is check");
        Assertions.assertEquals("@Test_telegram", driver.findElement(By.id("id_contact-0-value")).getAttribute("value"));
        logger.info("Telegram is check");
        Assertions.assertEquals("+79998887766", driver.findElement(By.id("id_contact-1-value")).getAttribute("value"));
        logger.info("WhatsApp is check");
    }
}