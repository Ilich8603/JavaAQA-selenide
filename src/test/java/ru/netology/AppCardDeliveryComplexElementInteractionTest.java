package ru.netology;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;


public class AppCardDeliveryComplexElementInteractionTest {

    @BeforeEach
    void setUp() {

        Selenide.open("http://localhost:9999");
    }

    // константы для селекторов
    private static final String CITY_SELECTOR = "[data-test-id='city'] input";
    private static final String DATE_SELECTOR = "[data-test-id='date'] input";
    private static final String NAME_SELECTOR = "[data-test-id='name'] input";
    private static final String PHONE_SELECTOR = "[data-test-id='phone'] input";
    private static final String AGREEMENT_SELECTOR = "[data-test-id='agreement']";
    private static final String BUTTON_SELECTOR = "button.button";
    private static final String NOTIFICATION_SELECTOR = "[data-test-id='notification']";
    private static final String MENU_ITEM_SELECTOR = ".menu-item";
    private static final String CALENDAR_ARROW_RIGHT_SELECTOR = ".calendar__arrow_direction_right:not(.calendar__arrow_double)";
    private static final String CALENDAR_LAYOUT_SELECTOR = "//table[@class='calendar__layout']//td[text()='%d']";

    //вычисление даты
    private LocalDate setTargetDate(long addDays) {
        return LocalDate.now().plusDays(addDays);
    }

    //преобразование даты в строку по шаблону
    private String formatDeliveryDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    //проверка перехода на следующий месяц
    private boolean isNextMonth(LocalDate targetDate) {
        return targetDate.getMonthValue() != LocalDate.now().getMonthValue();
    }

    //выбор даты в календаре с навигацией
    private void selectDateInCalendar(LocalDate targetDate) {
        $(DATE_SELECTOR).click();

        // Если целевая дата в следующем месяце, нажимаем стрелку "вперед"
        if (isNextMonth(targetDate)) {
            $(CALENDAR_ARROW_RIGHT_SELECTOR).click();
        }

        int dayOfMonth = targetDate.getDayOfMonth();
        $x(String.format(CALENDAR_LAYOUT_SELECTOR, dayOfMonth)).click();
    }

    @Test
    public void shouldBeSuccessfulDelivery() {

        $(CITY_SELECTOR).setValue("Но");
        //выбор из списка городов
        $$(MENU_ITEM_SELECTOR).findBy(Condition.text("Новосибирск")).click();
        LocalDate targetDate = setTargetDate(7);
        selectDateInCalendar(targetDate);
        $(NAME_SELECTOR).setValue("Орлов Сергей");
        $(PHONE_SELECTOR).setValue("+79131152552");
        $(AGREEMENT_SELECTOR).click();
        $(BUTTON_SELECTOR).click();
        String expectedDate = formatDeliveryDate(targetDate, "dd.MM.yyyy");
        $(NOTIFICATION_SELECTOR).should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text("Встреча успешно забронирована на " + expectedDate));

    }

    @Test
    public void shouldBeSuccessfulDeliveryNextMonth() {

        $(CITY_SELECTOR).setValue("Но");
        $$(MENU_ITEM_SELECTOR).findBy(Condition.text("Новосибирск")).click();
        LocalDate targetDate = setTargetDate(30);
        selectDateInCalendar(targetDate);
        $(NAME_SELECTOR).setValue("Орлов Сергей");
        $(PHONE_SELECTOR).setValue("+79131152552");
        $(AGREEMENT_SELECTOR).click();
        $(BUTTON_SELECTOR).click();
        String expectedDate = formatDeliveryDate(targetDate, "dd.MM.yyyy");
        $(NOTIFICATION_SELECTOR).should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text("Встреча успешно забронирована на " + expectedDate));

    }

}