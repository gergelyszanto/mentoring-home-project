package com.mentoring.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class EmailUtils {

    private static final String SUBJECT = "IP address changed";
    private static final String FOLDER = "IP address change";
    private static final String HOST = "IMAP.gmail.com";
    private static final String MAIL_STORE_TYPE = "IMAP";
    private static final String USERNAME = "learn.ta.2019@gmail.com";
    private static final String PASSWORD = "Test1234!";

    public static String checkLastEmailForIpChanges() {
        return getLastEmailContent(HOST, MAIL_STORE_TYPE, USERNAME, PASSWORD);
    }

    public static String getIpAddressFromEmail() {
        String emailContent = checkLastEmailForIpChanges();
        String ipAddress = "";

        // find the ip address right after 'SkyXplore: http://'
        Pattern pattern = Pattern.compile("(?<=SkyXplore: )http(s)*:\\/\\/(\\d{2,3}\\.){3}\\d{2,3}");
        Matcher matcher = pattern.matcher(emailContent);

        if (matcher.find()) {
            ipAddress = matcher.group();
            log.info("SkyXplore IP address in mail: " + matcher.group());
        }

        return ipAddress;
    }

    private static String getLastEmailContent(String host, String storeType, String user,
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

            // retrieve the last message from the folder
            Message message = emailFolder.getMessages(emailFolder.getMessageCount(), emailFolder.getMessageCount())[0];

            String latestMessageBody = getTextFromMessage(message);

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
            return latestMessageBody;
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