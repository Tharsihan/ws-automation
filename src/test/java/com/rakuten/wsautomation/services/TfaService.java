package com.rakuten.wsautomation.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import static io.restassured.RestAssured.given;

@Service
public class TfaService {

    @Value("${test.endpointR1}")
    private String endpointR1;
    @Value("${test.endpointR2}")
    private String endpointR2;
    @Value("${test.endpointR3}")
    private String endpointR3;
    @Value("${test.endpointR4}")
    private String endpointR4;
    @Value("${test.endpointR5}")
    private String endpointR5;


    public <T> T mapperResponse (String body, Class<T> reponseType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, reponseType);
    }

    public ValidatableResponse generateDeviceTokenRequest(String deviceToken) {
        HashMap<String, String> info = new HashMap<>();
        info.put("deviceToken", deviceToken);
         ValidatableResponse response= given()
                .contentType(ContentType.JSON)
                .body(info)
                .when()
                .post(endpointR1).then();
         return response;

    }

    public ValidatableResponse generateTokenRequest(String deviceToken) {
        HashMap<String, String> info = new HashMap<>();
        info.put("deviceId", "123456789");
        info.put("identifier", "qa-16415666491579999@yopmail.com");
        info.put("password", "Rakuten2020");
        info.put("deviceToken", deviceToken);
        return given()
                .contentType(ContentType.JSON)
                .header("X-Forwarded-For", "62.23.27.114")
                .body(info)
                .when().post(endpointR2).then();
    }

    public ValidatableResponse verificationTokenRequest(String token) {
        HashMap<String, String> info = new HashMap<>();
        info.put("token", token);
        return given()
                .contentType(ContentType.JSON)
                .body(info)
                .when().put(endpointR3).then();
    }

    public ValidatableResponse authentificationRequest(String token, String deviceToken) {
        HashMap<String, String> info = new HashMap<>();
        info.put("verificationToken", token);
        info.put("deviceToken", deviceToken);
        return given()
                .contentType(ContentType.JSON)
                .body(info)
                .when().post(endpointR4).then();
    }

    public HashMap<String, String> setUserDetails() {
        HashMap<String, String> user_info = new HashMap<>();
        user_info.put("email", "qa1234567@yopmail.com");
        user_info.put("email_confirmation", "qa1234567@yopmail.com");
        user_info.put("password", "Provence@05");
        user_info.put("salutation", "30");
        user_info.put("first_name", "Marco");
        user_info.put("last_name", "Thibaut");
        user_info.put("dob", "01/01/1980");
        return user_info;
    }

    public HashMap<String, Boolean> setAdditionalInfo() {
        HashMap<String, Boolean> info = new HashMap<>();
        info.put("accept_terms", true);
        return info;
    }

    public ValidatableResponse createUserRequest() {
        return given().contentType(ContentType.JSON)
                .queryParam("version", "1")
                .queryParam("deviceId", "133456787")
                .queryParam("deviceName", "qaphone")
                .body(setUserDetails())
                .body(setAdditionalInfo())
                .when()
                .put("https://www8.rakqa.fr/restpublic/buy-apps/user").then();
    }

    public ValidatableResponse setPhoneNumberRequest(String phoneNumber, String userId) {
        HashMap<String, String> info = new HashMap<>();
        info.put("channel", "SMS");
        info.put("phoneNumber", phoneNumber );
        info.put("userId", userId);
        return given()
                .contentType(ContentType.JSON)
                .body(info)
                .when().post(endpointR5).then();
    }

    public ValidatableResponse setTfaMethodRequest(String userId) {
        HashMap<String, String> info = new HashMap<>();
        info.put("preferredLoginMethod", "TFA");
        return given()
                .contentType(ContentType.JSON)
                .body(info)
                .when().put("http://172.21.101.86:33500/restprivate/user/"+userId+"/login").then();
    }

}

