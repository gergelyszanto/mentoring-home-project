package com.mentoring.framework.utils;

import java.util.Random;

public class UserUtils {

    public static String generateRandomEmail() {
        return "testmail-" + generateRandomNumber() + "@test.com";
    }

    public static String generateRandomUsername() {
        return "User-" + generateRandomNumber();
    }

    private static int generateRandomNumber() {
        return new Random().nextInt(900000) + 100000;
    }
}
