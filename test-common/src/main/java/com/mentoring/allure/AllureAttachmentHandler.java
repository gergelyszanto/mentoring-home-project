package com.mentoring.allure;

import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllureAttachmentHandler {

    @Attachment(value = "{0}", type = "application/json")
    public String attachJson(String attachmentName, String textOfAttachment) {
        if (textOfAttachment != null) {
            return textOfAttachment;
        } else {
            return "";
        }
    }

    @Attachment(value = "{0}", type = "text/html")
    public String attachText(String attachmentName, String text) {
        if (text != null) {
            return text;
        } else {
            return "";
        }
    }
}
