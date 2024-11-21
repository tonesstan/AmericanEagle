package AmericanEagleTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.logging.Level;

import static AmericanEagleTest.Utils.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.util.logging.Logger.getLogger;

public class PurchaseTest {

    private static Level previousLevel;

    @BeforeAll
    public static void setUp() {
        previousLevel = getLogger("").getLevel();
        getLogger("").setLevel(Level.WARNING);
        Configuration.baseUrl = "https://www.ae.com";
        Configuration.browser = "firefox";
        Configuration.browserCapabilities = new FirefoxOptions().setPageLoadStrategy(PageLoadStrategy.EAGER)
                .addArguments("--headless", "--window-size=1920,1080", "--disable-notifications", "--disable-gpu", "--disable-dev-tools", "--fastSetValue")
                .addPreference("permissions.default.geo", 2);
        Configuration.timeout = 10000;
    }

    @AfterAll
    public static void tearDown() {
        closeWebDriver();
        getLogger("").setLevel(previousLevel);
    }

    @Test
    @DisplayName("Покупка случайного товара на American Eagle")
    public void purchase() {
        System.out.println("Тестируем покупку случайного товара на American Eagle.");
        open("");

        System.out.println("Принимаем куки.");
        $("span[data-test-logo='active-logo-aeo-holiday']").shouldBe(visible);
        $("div#onetrust-banner-sdk").shouldBe(visible);
        $("button#onetrust-accept-btn-handler").shouldBe(visible).click();
        $("div#onetrust-banner-sdk").shouldNotBe(visible);

        System.out.println("\nВыбираем случайную категорию товаров.");
        SelenideElement element = hoverRandomElement($("nav#top-navigation").shouldBe(visible), $("div._mm-top-level_ali1iz"));
        assert element != null;
        clickRandomElement(element, $("a[class*='_column-link_1mj2se']"));
        $("div[data-test-active-logo='aeo']").hover();

        scrollToElement($("h1.qa-non-link-label").shouldBe(visible));
        System.out.println("\nЗашли на страницу выбранной категории товаров.\nВыбираем случайный товар.");
        element = hoverRandomElement($("div.results-list, div.product-list"), $("div.tile-media"));
        assert element != null;
        element.click();
        System.out.println("Товар выбран.");

        $("h1._product-name_kfr7e8").shouldBe(visible);
        System.out.println("\nОткрыли карточку товара.\nВыбираем доступный размер.");
        scrollToElement($("span.dropdown-text").shouldBe(visible));
        $("span.dropdown-text").click();
        clickRandomElement($("ul.dropdown-menu").shouldBe(visible), $("a:not(:has(small))"));
        $("button.qa-btn-add-to-bag").click();

        $x("//h2[text() = 'Added to bag!']").shouldBe(visible);
        System.out.println("\nВыбранный товар добавлен в корзину.");
        $("button.qa-btn-view-bag").click();

        $x("//h1[text() = 'Shopping Bag']").shouldBe(visible);
        System.out.println("Зашли на страницу корзины.");
        switchTo().frame($("iframe.component-frame").should(exist).shouldBe(visible));
        scrollToElement($("img.paypal-logo").shouldBe(visible));
        $("img.paypal-logo").click();
        System.out.println("Выбрали PayPal для оплаты товара.");

        switchTo().window(1);
        $("h1#headerText").shouldBe(visible).shouldHave(text("Pay with PayPal"));
        System.out.println("Перешли на страницу оплаты PayPal.\nТестируем компоненты страницы.");
        $("#emailSubTagLine").shouldBe(visible).shouldHave(text("Enter your email address to get started."));
        $("#forgotEmail").shouldBe(visible).shouldHave(text("Forgot email?"));
        $("#btnNext").shouldBe(visible).shouldHave(text("Next"));
        $("#startOnboardingFlow").shouldBe(visible).shouldHave(text("Create an Account"));
        $("#cancelLink").shouldBe(visible).scrollTo().shouldHave(text("Cancel and return to American Eagle Outfitters, Inc.")).click();
        System.out.println("Тест компонентов страницы оплаты PayPal прошёл успешно.\nВозвращаемся на страницу корзины.");

        switchTo().window(0);
        switchTo().defaultContent();
        scrollToElement($x("//h1[text() = 'Shopping Bag']").shouldBe(visible));
        System.out.println("Успешно вернулись на страницу корзины.");

        System.out.println("Тест прошёл успешно!");
    }

}