package com.mentoring.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Browser {
    FIREFOX, CHROME, IE, SAFARI, EDGE, REMOTE_CHROME, REMOTE_IE, REMOTE_FIREFOX, REMOTE_EDGE, CHROME_WITH_PROXY, FIREFOX_WITH_PROXY;

    public static Browser parse(String stringToBrowser) {
        for (Browser browser : Browser.values()) {
            if (browser.name().equalsIgnoreCase(stringToBrowser)) {
                return browser;
            }
        }
        log.warn("Unknown browser '{}'. Setting to default Chrome", stringToBrowser);
        return CHROME;
    }
}
