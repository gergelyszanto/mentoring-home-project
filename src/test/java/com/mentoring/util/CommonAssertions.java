package com.mentoring.util;

import com.mentoring.pageobject.NotificationContainer;
import org.assertj.core.api.SoftAssertions;

public final class CommonAssertions {

    private CommonAssertions() {
    }

    public static void assertNotificationMessageIsCorrect(SoftAssertions assertion, NotificationContainer nContainer,
                                                          String expectedMessage) {
        assertion.assertThat(nContainer.getNotificationMessages())
                .as(Messages.NOTIFICATION_BUTTON_ASSERTION_MESSAGE)
                .contains(expectedMessage);
    }
}
