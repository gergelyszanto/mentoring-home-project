package com.mentoring.framework.utils;

import com.mentoring.framework.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
public final class InitData {

    private InitData() {
    }

    public static void registerUser(String username, String password, String email) {
        try {
            HttpClient client = HttpClientBuilder.create().build();

            HttpPost postRequest = new HttpPost(Config.getApplicationUrl() + "/user");

            StringEntity input = new StringEntity(
                    "{\"username\":\""+ username +"\"" +
                    ", \"password\":\""+ password +"\"" +
                    ", \"email\": \""+ email +"\"}");

            input.setContentType("application/json");
            postRequest.setEntity(input);

            client.execute(postRequest);
        } catch (UnsupportedEncodingException e) {
            log.error("Error happened during user registration (Unsupported Encoding Exception).", e);
        } catch (ClientProtocolException e) {
            log.error("Error happened during user registration (Client Protocol Exception).", e);
        } catch (IOException e) {
            log.error("Error happened during user registration (IO Exception).", e);
        }

    }
}
