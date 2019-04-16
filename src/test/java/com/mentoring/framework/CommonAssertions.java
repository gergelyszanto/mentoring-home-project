package com.mentoring.framework;

import com.mentoring.pageobject.NotificationContainer;

public final class CommonAssertions {

    private CommonAssertions () {
    }

    public static boolean hasButtonsListErrorText(NotificationContainer notificationContainer, String errorMessage) {
        return notificationContainer.getButtonsText()
                .stream()
                .anyMatch(s -> s.equals(errorMessage));
    }
}
