package ui;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class LoginTest {

    private static final String BASE_URL = "https://www.saucedemo.com";
    private static final String VALID_USERNAME = "standard_user";
    private static final String LOCED_USERNAME = "locked_out_user";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String INVALID_USERNAME = "standard_user000";
    private static final String INVALID_PASSWORD = "secret_sauce000";

    @BeforeAll
    static void configure() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");

        Configuration.browser = "chrome";
        Configuration.browserCapabilities = options;
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    public void setUp() {
        open(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Успешный вход с правильными данными")
    public void testSuccessfulLogin() {
        $(By.id("user-name"))
                .setValue(VALID_USERNAME);
        $(By.id("password"))
                .setValue(VALID_PASSWORD);
        $(By.id("login-button"))
                .click();
        $(By.className("app_logo"))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Неправильный логин или пароль")
    public void testLoginWithInvalidCredentials() {
        $(By.id("user-name"))
                .setValue(INVALID_USERNAME);
        $(By.id("password"))
                .setValue(INVALID_PASSWORD);
        $(By.id("login-button"))
                .click();
        $(By.cssSelector(".error-message-container"))
                .shouldHave(text("Epic sadface: " +
                        "Username and password do not match any user in this service"));
    }

    @Test
    @DisplayName("Вход с пустыми полями")
    public void testLoginWithEmptyFields() {
        $(By.id("login-button"))
                .click();
        $(By.cssSelector(".error-message-container"))
                .shouldHave(text("Epic sadface: Username is required"));
    }

    @Test
    @DisplayName("Вход с заблокированным пользователем")
    public void testLoginWithLockedOutUser() {
        $(By.id("user-name"))
                .setValue(LOCED_USERNAME);
        $(By.id("password"))
                .setValue(VALID_PASSWORD);
        $(By.id("login-button"))
                .click();
        $(By.cssSelector(".error-message-container"))
                .shouldHave(text("Epic sadface: Sorry, this user has been locked out."));
    }
}
