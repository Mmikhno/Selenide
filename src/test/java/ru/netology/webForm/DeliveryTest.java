package ru.netology.webForm;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    public String genDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void shouldRegistrationFormFill() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='name'] input").setValue(("Валерий Иванов"));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement notification = $("[data-test-id='notification']");
        notification.shouldBe(visible, Duration.ofMillis(15000));
        notification.$(".notification__title").shouldHave(exactText("Успешно!"));
        notification.$(".notification__content").shouldHave(exactText("Встреча успешно забронирована на " + date));
    }

    @Test
    public void shouldTestRegFormIfCityIsOutOfList() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Когалым");
        $("[data-test-id='name'] input").setValue(("Валерий Иванов"));
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldBe(visible, Duration.ofMillis(10000)).shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldTestRegFormIfCityIsEmpty() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='name'] input").setValue(("Валерий Иванов"));
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldBe(visible, Duration.ofMillis(10000)).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldTestRegFormIfDatePlusONEDay() {
        String date = genDate(1);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='name'] input").setValue(("Валерий Иванов"));
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldTestRegFormIfDateIsToday() {
        String date = genDate(0);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='name'] input").setValue(("Валерий Иванов"));
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldTestRegFormIfDateIsYesterday() {
        String date = genDate(-1);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='name'] input").setValue(("Валерий Иванов"));
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldTestRegFormIfDateIsEmpty() {
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='name'] input").setValue(("Валерий Иванов"));
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Неверно введена дата"));
    }

    @Test
    public void shouldTestRegFormIfNameContainsNums() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='name'] input").setValue(("Валерий Иванов1"));
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы"));
    }

    @Test
    public void shouldTestRegFormIfNameContainsSpecialChars() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='name'] input").setValue(("Елена*Петрова"));
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы"));
    }

    @Test
    public void shouldTestRegFormIfNameIsEmpty() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldTestRegFormIfPhoneExceed() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='name'] input").setValue(("Иван Иванович"));
        $("[data-test-id='phone'] input").setValue("+792112345678");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"));
    }

    @Test
    public void shouldTestRegFormIfPhoneIsLess() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='name'] input").setValue(("Иван Иванович"));
        $("[data-test-id='phone'] input").setValue("+7921123456");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"));
    }

    @Test
    public void shouldTestRegFormIfPhoneIsEmpty() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='name'] input").setValue(("Иван Иванович"));
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldTestIfNoCheckBox() {
        String date = genDate(3);
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input ").setValue(date);
        $("[data-test-id='name'] input").setValue(("Иван Иванович"));
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text").shouldBe(visible, Duration.ofMillis(5000)).shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
