package ru.netology.webForm;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.LocalDate.now;

public class DeliveryTestTask2 {
    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    public LocalDate genDate(int days) {
        return LocalDate.now().plusDays(days);
    }

    public int clickNum(LocalDate date) {
        //возвращает кол-во нажатий по кнопке переключения месяца.
        Period period = Period.between(now(), date);
        int diffMonths = period.getMonths();
        int remainDays = period.getDays();
        int diffDays = date.withDayOfMonth(date.lengthOfMonth()).getDayOfMonth() - now().plusMonths(diffMonths).getDayOfMonth();
        int numOfClick;
        if (diffDays <= remainDays) {
            numOfClick = diffMonths + 1;
        } else {
            numOfClick = diffMonths;
        }
        return numOfClick;
    }

    @Test
    public void shouldTestRegFormWithCalendar() {
        $("[data-test-id='city'] input").setValue("Ка");
        $$(".popup__container .menu-item__control").find(text("Калининград")).click();
        LocalDate date = genDate(7);
        int click = clickNum(date);
        $("[data-test-id='date'] input ").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] button[type='button").click();
        SelenideElement next = $(".popup .calendar__arrow_direction_right[role='button'][data-step='1']");
        if (click > 0) {
            for (int i = 1; i <= click; i++) {
                next.click();
            }
        }
        ElementsCollection calendarDays = $$("tr .calendar__day");
        calendarDays.find(text(date.format(DateTimeFormatter.ofPattern("d")))).click();
        $("[data-test-id='name'] input").setValue(("Петров Петр"));
        $("[data-test-id='phone'] input").setValue("+79211234567");
        $$("[data-test-id='agreement']").first().click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement notification = $("[data-test-id='notification']");
        notification.shouldBe(visible, Duration.ofMillis(15000));
        notification.$(".notification__title").shouldHave(exactText("Успешно!"));
        notification.$(".notification__content").shouldHave(exactText("Встреча успешно забронирована на " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }

}

