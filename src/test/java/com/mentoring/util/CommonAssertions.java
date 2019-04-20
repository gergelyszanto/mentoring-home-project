package com.mentoring.util;

import com.mentoring.pageobject.NotificationContainer;
import org.assertj.core.api.SoftAssertions;

public final class CommonAssertions {

    private CommonAssertions () {
    }

    private static boolean isNotificationMessagePresent(NotificationContainer nContainer, String message) {
        return nContainer.getButtonsText()
                .stream()
                .anyMatch(s -> s.equals(message));
    }

    public static void assertNotificationMessageIsCorrect(SoftAssertions assertion, NotificationContainer nContainer, String message) {
        assertion.assertThat(isNotificationMessagePresent(nContainer, message))
                .as(Messages.NOTIFICATION_BUTTON_ASSERTION_MESSAGE)
                .isTrue();
    }
}
