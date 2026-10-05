package ru.netology;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;


public class AppCardDeliveryTest {

    @BeforeEach
    void setUp() {

        Selenide.open("http://localhost:9999");
    }


    private static final String CITY_SELECTOR = "[data-test-id='city'] input";
    private static final String DATE_SELECTOR = "[data-test-id='date'] input";
    private static final String NAME_SELECTOR = "[data-test-id='name'] input";
    private static final String PHONE_SELECTOR = "[data-test-id='phone'] input";
    private static final String AGREEMENT_SELECTOR = "[data-test-id='agreement']";
    private static final String BUTTON_SELECTOR = "button.button";
    private static final String NOTIFICATION_SELECTOR = "[data-test-id='notification']";

    private String setDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void shouldBeSuccessfulDelivery() {

        $(CITY_SELECTOR).setValue("Омск");
        String deliveryDate = setDate(3, "dd.MM.yyyy");
        $(DATE_SELECTOR).press(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $(DATE_SELECTOR).setValue(deliveryDate);
        $(NAME_SELECTOR).setValue("Орлов Сергей");
        $(PHONE_SELECTOR).setValue("+79131152552");
        $(AGREEMENT_SELECTOR).click();
        $(BUTTON_SELECTOR).click();
        $(NOTIFICATION_SELECTOR).should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text("Встреча успешно забронирована на " + deliveryDate));

    }

}
