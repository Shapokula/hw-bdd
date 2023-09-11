package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    static String card01Number = "5559 0000 0000 0001";
    static String card01ID = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    static String card02Number = "5559 0000 0000 0002";
    static String card02ID = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";
    static String nonExistingCardNumber = "5559 0000 0000 0003";

    @AfterAll
    static void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int card01Balance = dashboardPage.getCardBalance(card01ID);
        if (card01Balance < 10_000) {
            var cardPage = dashboardPage.pressDeposit(card01ID);
            int amountToTransfer = 10_000 - card01Balance;
            cardPage.validTransfer(Integer.toString(amountToTransfer), card02Number);
        } else if (card01Balance > 10_000) {
            var cardPage = dashboardPage.pressDeposit(card02ID);
            int amountToTransfer = card01Balance - 10_000;
            cardPage.validTransfer(Integer.toString(amountToTransfer), card01Number);
        }
    }

    @Test
    void shouldTransferMoneyFromCard02ToCard01() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int card01balance = dashboardPage.getCardBalance(card01ID);
        int card02balance = dashboardPage.getCardBalance(card02ID);
        var cardPage = dashboardPage.pressDeposit(card01ID);
        int amountToTransfer = card02balance / 2;
        cardPage.validTransfer(Integer.toString(amountToTransfer), card02Number);

        int actual01 = dashboardPage.getCardBalance(card01ID);
        int expected01 = card01balance + amountToTransfer;
        Assertions.assertEquals(expected01, actual01);

        int actual02 = dashboardPage.getCardBalance(card02ID);
        int expected02 = card02balance - amountToTransfer;
        Assertions.assertEquals(expected02, actual02);
    }

    @Test
    void shouldTransferMoneyFromCard01ToCard02() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int card01balance = dashboardPage.getCardBalance(card01ID);
        int card02balance = dashboardPage.getCardBalance(card02ID);
        var cardPage = dashboardPage.pressDeposit(card02ID);
        int amountToTransfer = card01balance / 2;
        cardPage.validTransfer(Integer.toString(amountToTransfer), card01Number);

        int actual01 = dashboardPage.getCardBalance(card01ID);
        int expected01 = card01balance - amountToTransfer;
        Assertions.assertEquals(expected01, actual01);

        int actual02 = dashboardPage.getCardBalance(card02ID);
        int expected02 = card02balance + amountToTransfer;
        Assertions.assertEquals(expected02, actual02);
    }

    @Test
    void shouldNotTransferMoneyIfBalanceNotEnough() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int card01balance = dashboardPage.getCardBalance(card01ID);
        int card02balance = dashboardPage.getCardBalance(card02ID);
        var cardPage = dashboardPage.pressDeposit(card02ID);
        int amountToTransfer = card01balance + 1;
        cardPage.validTransfer(Integer.toString(amountToTransfer), card01Number);

        int actual01 = dashboardPage.getCardBalance(card01ID);
        int expected01 = card01balance;
        Assertions.assertEquals(expected01, actual01);

        int actual02 = dashboardPage.getCardBalance(card02ID);
        int expected02 = card02balance;
        Assertions.assertEquals(expected02, actual02);
    }

    @Test
    void shouldNotTransferNegativeAmount() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int card01balance = dashboardPage.getCardBalance(card01ID);
        int card02balance = dashboardPage.getCardBalance(card02ID);
        var cardPage = dashboardPage.pressDeposit(card01ID);
        int amountToTransfer = -card02balance;
        cardPage.validTransfer(Integer.toString(amountToTransfer), card02Number);

        int actual01 = dashboardPage.getCardBalance(card01ID);
        int expected01 = card01balance;
        Assertions.assertEquals(expected01, actual01);

        int actual02 = dashboardPage.getCardBalance(card02ID);
        int expected02 = card02balance;
        Assertions.assertEquals(expected02, actual02);
    }

    @Test
    void shouldNotTransferFromNonExistingCard() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int card01balance = dashboardPage.getCardBalance(card01ID);
        int card02balance = dashboardPage.getCardBalance(card02ID);
        var cardPage = dashboardPage.pressDeposit(card01ID);
        int amountToTransfer = 2000;
        cardPage.validTransfer(Integer.toString(amountToTransfer), nonExistingCardNumber);
        $("[data-test-id=error-notification]").shouldBe(Condition.visible, Duration.ofSeconds(5));
    }
}
