package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardPage {
    private SelenideElement heading = $$(".heading").findBy(Condition.text("Пополнение карты"));
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");

    public CardPage() {heading.shouldBe(Condition.visible);}

    public void validTransfer(String amount, String from) {
        amountField.setValue(amount);
        fromField.setValue(from);
        transferButton.click();
    }

    public void nonValidTransfer(String amount, String from) {
        amountField.setValue(amount);
        fromField.setValue(from);
        transferButton.click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible, Duration.ofSeconds(5));
    }
}
