package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement card01 = $("[data-test-id=92df3f1c-a033-48e6-8390-206f6b1f56c0]");
    private SelenideElement card02= $("[data-test-id=0f3f5c2a-249e-4c3d-8287-09f7a039391d]");
    private SelenideElement depositButton01 = card01.find("[data-test-id=action-deposit]");
    private SelenideElement depositButton02 = card02.find("[data-test-id=action-deposit]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String id) {
        String text = cards.findBy(attribute("data-test-id", id)).getText();
        // TODO: перебрать все карты и найти по атрибуту data-test-id
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public CardPage pressDeposit(String id) {
        SelenideElement card = cards.findBy(attribute("data-test-id", id));
        card.find("[data-test-id=action-deposit]").click();
        return new CardPage();
    }
}
