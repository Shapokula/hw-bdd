package ru.netology.web.data;

import lombok.Value;

public class DataHelper {

    private static final CardInfo card01 = new CardInfo("5559 0000 0000 0001", "92df3f1c-a033-48e6-8390-206f6b1f56c0");
    private static final CardInfo card02 = new CardInfo("5559 0000 0000 0002", "0f3f5c2a-249e-4c3d-8287-09f7a039391d");
    private static final CardInfo nonExistentCard = new CardInfo("5559 0000 0000 0003", "0");
    private static CardInfo[] cards = {card01, card02, nonExistentCard};

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class CardInfo {
        private String number;
        private String id;
    }

    public static CardInfo getCardInfo(int cardNumber) {
        return cards[cardNumber - 1];
    }
}