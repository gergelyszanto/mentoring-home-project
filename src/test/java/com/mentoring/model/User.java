package com.mentoring.model;

import java.util.Random;

public class User {

    public static String generateRandomEmail() {
        return "testmail-" + generateRandomNumber() + "@test.com";
    }

    public static String generateRandomUsername() {
        return "User-" + generateRandomNumber();
    }

    public static String generateRandomCharacterName() {
        return "Char-" + generateRandomNumber();
    }

    private static int generateRandomNumber() {
        return new Random().nextInt(900000) + 100000;
    }
}
