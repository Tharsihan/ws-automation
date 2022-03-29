package com.rakuten.wsautomation.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakuten.wsautomation.utils.json.RegisterUserParent;
import com.rakuten.wsautomation.utils.json.Response;
import com.rakuten.wsautomation.utils.json.ResponseApi;
import com.rakuten.wsautomation.utils.json.ResponseError;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.HashMap;
import static io.restassured.RestAssured.given;

@Service
public class TfaService {

    private final String endpointR1 = "https://www8.rakqa.fr/restpublic/buy-apps/authentication/device/generate?version=1";
    private final String endpointR2 = "https://www8.rakqa.fr/restpublic/buy-apps/authentication?version=1";
    private final String endpointR3 = "http://172.21.101.86:33500/restprivate/login/verification";
    private final String endpointR4 = "https://www8.rakqa.fr/restpublic/buy-apps/authentication/token?version=1";
    private final String endpointR5 = "http://172.21.101.86:34000/restprivate/migrate";



    public static Response getResponse(String dataJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(dataJson, Response.class);
    }

    public static ResponseError getErrorResponse(String dataJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(dataJson, ResponseError.class);
    }

    public static ResponseApi getResponseAPI(String dataJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(dataJson, ResponseApi.class);
    }

    public static RegisterUserParent getUserInfo(String dataJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(dataJson, RegisterUserParent.class);
    }
    public <T> T mapResponse (String body, Class<T> reponseType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, reponseType);
    }

    public ValidatableResponse GenerateDeviceTokenRequest(String deviceToken) {
        HashMap<String, String> info = new HashMap<>();
        info.put("deviceToken", deviceToken);
         ValidatableResponse response= given()
                .contentType(ContentType.JSON)
                .body(info)
                .when()
                .post(endpointR1).then().assertThat();
         return response;

    }

    public ValidatableResponse GenerateTokenRequest(String deviceToken) {
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

    public ValidatableResponse VerificationTokenRequest(String token) {
        HashMap<String, String> info = new HashMap<>();
        info.put("token", token);
        return given()
                .contentType(ContentType.JSON)
                .body(info)
                .when().put(endpointR3).then();
    }

    public ValidatableResponse AuthentificationRequest(String token, String deviceToken) {
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

    public ValidatableResponse CreateUserRequest() {
        return given().contentType(ContentType.JSON)
                .queryParam("version", "1")
                .queryParam("deviceId", "133456787")
                .queryParam("deviceName", "qaphone")
                .body(setUserDetails())
                .body(setAdditionalInfo())
                .when()
                .put("https://www8.rakqa.fr/restpublic/buy-apps/user").then();
    }

    public ValidatableResponse SetPhoneNumberRequest(String phoneNumber, String userId) {
        HashMap<String, String> info = new HashMap<>();
        info.put("channel", "SMS");
        info.put("phoneNumber", phoneNumber );
        info.put("userId", userId);
        return given()
                .contentType(ContentType.JSON)
                .body(info)
                .when().post(endpointR5).then();
    }

    public ValidatableResponse SetTfaMethodRequest(String userId) {
        HashMap<String, String> info = new HashMap<>();
        info.put("preferredLoginMethod", "TFA");
        return given()
                .contentType(ContentType.JSON)
                .body(info)
                .when().put("http://172.21.101.86:33500/restprivate/user/"+userId+"/login").then();
    }

}

