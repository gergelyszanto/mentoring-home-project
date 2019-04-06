package com.mentoring.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class EmailUtils {

    private static final String SUBJECT = "IP address changed";
    private static final String FOLDER = "IP address change";
    private static final String HOST = "IMAP.gmail.com";
    private static final String MAIL_STORE_TYPE = "IMAP";
    private static final String USERNAME = "learn.ta.2019@gmail.com";
    private static final String PASSWORD = "Test1234!";

    public static List<String> checkEmailsForIpChanges() {
        return getListOfEmailContent(HOST, MAIL_STORE_TYPE, USERNAME, PASSWORD);
    }

    private static List<String> getListOfEmailContent(String host, String storeType, String user,
                                                      String password) {
        try {
            //create properties field
            Properties properties = new Properties();
            properties.put("mail.IMAP.host", host);
            properties.put("mail.IMAP.port", "993");
            properties.put("mail.IMAP.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("imaps");
            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder(FOLDER);
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            List<String> emailContentList = new ArrayList<>();
            log.info("Number of messages found with '{}' subject: {}", SUBJECT, messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                if (message.getSubject().contains(SUBJECT)) {
                    log.info("\n---------------------------------" +
                            "\nEmail Number: " + (i+1) +
                            "\nSubject: " + message.getSubject() +
                            "\nFrom: " + message.getFrom()[0] +
                            "\nSent Date: " + message.getSentDate() +
                            "\nMessage: " + getTextFromMessage(message));
                    emailContentList.add(getTextFromMessage(message));
                }
            }
            //close the store and folder objects
            emailFolder.close(false);
            store.close();
            return emailContentList;
        } catch (Exception e) {
            log.error("Error happened during reading emails.", e);
            throw new RuntimeException("Failed to get application IP address from emails.");
        }
    }

    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

}