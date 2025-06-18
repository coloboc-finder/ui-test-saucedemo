package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class CartTest {


    private static final String BASE_URL = "https://www.saucedemo.com";
    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";

    @BeforeEach
    public void setUp() {
        open(BASE_URL);
        $(By.id("user-name"))
                .setValue(VALID_USERNAME);
        $(By.id("password"))
                .setValue(VALID_PASSWORD);
        $(By.id("login-button"))
                .click();
        $(By.className("app_logo"))
                .shouldBe(visible);
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Корзина пуста при первом входе")
    public void testEmptyCartOnLogin() {
        $(".shopping_cart_badge")
                .shouldNot(exist);
    }

    @Test
    @DisplayName("Добавить товар в корзину и проверить наличие товара")
    public void testAddProductToCart() {
        $(".inventory_item:nth-child(1) .btn_inventory")
                .click();
        $("#shopping_cart_container .shopping_cart_badge")
                .shouldHave(text("1"));
    }

    @Test
    @DisplayName("Перейти в корзину и проверить наличие товара")
    public void testGoToCartAndCheckProduct() {
        $(".inventory_item:nth-child(1) .btn_inventory")
                .click();
        $("#shopping_cart_container")
                .click();
        $$(".cart_item")
                .shouldHave(size(1));
        $(".cart_item .inventory_item_name")
                .shouldHave(text("Sauce Labs Backpack"));
        $(".cart_item .btn_secondary")
                .click();
        $(".shopping_cart_badge")
                .shouldNot(exist);
    }
}
