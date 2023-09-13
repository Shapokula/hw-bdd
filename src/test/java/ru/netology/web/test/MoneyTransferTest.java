package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    static DataHelper.CardInfo card01Info = DataHelper.getCardInfo(1);
    static DataHelper.CardInfo card02Info = DataHelper.getCardInfo(2);
    static DataHelper.CardInfo nonExistentCardInfo = DataHelper.getCardInfo(3);

    @BeforeEach
    void makeBalancesEqual() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int card01Balance = dashboardPage.getCardBalance(card01Info.getId());
        if (card01Balance < 10_000) {
            var cardPage = dashboardPage.pressDeposit(card01Info.getId());
            int amountToTransfer = 10_000 - card01Balance;
            cardPage.validTransfer(Integer.toString(amountToTransfer), card02Info.getNumber());
        } else if (card01Balance > 10_000) {
            var cardPage = dashboardPage.pressDeposit(card02Info.getId());
            int amountToTransfer = card01Balance - 10_000;
            cardPage.validTransfer(Integer.toString(amountToTransfer), card01Info.getNumber());
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
        int card01balance = dashboardPage.getCardBalance(card01Info.getId());
        int card02balance = dashboardPage.getCardBalance(card02Info.getId());
        var cardPage = dashboardPage.pressDeposit(card01Info.getId());
        int amountToTransfer = card02balance / 2;
        cardPage.validTransfer(Integer.toString(amountToTransfer), card02Info.getNumber());

        int actual01 = dashboardPage.getCardBalance(card01Info.getId());
        int expected01 = card01balance + amountToTransfer;
        Assertions.assertEquals(expected01, actual01);

        int actual02 = dashboardPage.getCardBalance(card02Info.getId());
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
        int card01balance = dashboardPage.getCardBalance(card01Info.getId());
        int card02balance = dashboardPage.getCardBalance(card02Info.getId());
        var cardPage = dashboardPage.pressDeposit(card02Info.getId());
        int amountToTransfer = card01balance / 2;
        cardPage.validTransfer(Integer.toString(amountToTransfer), card01Info.getNumber());

        int actual01 = dashboardPage.getCardBalance(card01Info.getId());
        int expected01 = card01balance - amountToTransfer;
        Assertions.assertEquals(expected01, actual01);

        int actual02 = dashboardPage.getCardBalance(card02Info.getId());
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
        int card01balance = dashboardPage.getCardBalance(card01Info.getId());
        int card02balance = dashboardPage.getCardBalance(card02Info.getId());
        var cardPage = dashboardPage.pressDeposit(card02Info.getId());
        int amountToTransfer = card01balance + 1;
        cardPage.validTransfer(Integer.toString(amountToTransfer), card01Info.getNumber());

        int actual01 = dashboardPage.getCardBalance(card01Info.getId());
        int expected01 = card01balance;
        Assertions.assertEquals(expected01, actual01);

        int actual02 = dashboardPage.getCardBalance(card02Info.getId());
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
        int card01balance = dashboardPage.getCardBalance(card01Info.getId());
        int card02balance = dashboardPage.getCardBalance(card02Info.getId());
        var cardPage = dashboardPage.pressDeposit(card01Info.getId());
        int amountToTransfer = 2000;
        cardPage.nonValidTransfer(Integer.toString(amountToTransfer), nonExistentCardInfo.getNumber());
    }
}