package com.rakuten.wsautomation.stepdefs;

import com.rakuten.wsautomation.services.TfaService;
import com.rakuten.wsautomation.utils.json.RegisterUserParent;
import com.rakuten.wsautomation.utils.json.Response;
import com.rakuten.wsautomation.utils.json.ResponseApi;
import com.rakuten.wsautomation.utils.json.ResponseError;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;

@SpringBootTest
@CucumberContextConfiguration

public class Tfa {

    private TfaService tfaService;
    private ValidatableResponse validatableResponse;
    private String deviceTokenOut;
    private String verificationTokenOut;
    private String firstnameOut;

    @Value("${test.property}")
    private String value;

    public Tfa(TfaService tfaService) {
        this.tfaService = tfaService;
    }

    @When("I send a POST request to the URL \"([^\"]*)\"$")
    public void iSendAPOSTRequestToTheURL(String deviceToken) {
        System.out.println(value);
        validatableResponse = tfaService.generateDeviceTokenRequest(deviceToken);
    }

    @Then("^The request will return (\\d+)$")
    public void the_request_will_return(int statusCode) {
        validatableResponse.assertThat().statusCode(statusCode);
    }

    @And("The response should have deviceToken")
    public void theResponseShouldHaveDeviceToken() {
        String responseJson = validatableResponse.extract().asPrettyString();
        if (responseJson != null) {
            try {
                deviceTokenOut = tfaService.mapperResponse(responseJson,Response.class).getDeviceToken();
                System.out.print("device token:" + deviceTokenOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @When("^I send a POST request to the URL$")
    public void i_send_a_POST_request_to_the_URL() {
        System.out.println("device token output :" + deviceTokenOut);
        validatableResponse = tfaService.generateDeviceTokenRequest(deviceTokenOut);
    }

    @And("The response should have the same deviceToken")
    public void theResponseShouldHaveTheSameDeviceToken() {
        Assert.assertTrue(validatableResponse.extract().asPrettyString().contains(deviceTokenOut));
    }

    @When("I send a POST request to generate a token")
    public void iSendAPOSTRequestToGenerateAToken() {
        validatableResponse = tfaService.generateTokenRequest(deviceTokenOut);
        String responseJson = validatableResponse.extract().asPrettyString();
        if (responseJson != null) {
            try {
                verificationTokenOut = tfaService.mapperResponse(responseJson, ResponseError.class).getVerificationToken();
                System.out.print("verification Token:" + verificationTokenOut);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Then("The request will return bad http code (\\d+)$")
    public void theRequestWillReturnBadHttpCode(int httpCode) {
        validatableResponse.assertThat().statusCode(httpCode);
    }

    @When("I send a PUT request to verify the verification token")
    public void iSendAPUTRequestToVerifyTheVerificationToken() {
        validatableResponse = tfaService.verificationTokenRequest(verificationTokenOut);
    }

    @Then("^The response should have a token$")
    public void the_response_should_have_a_token() {
        String responseJson = validatableResponse.extract().asPrettyString();
        if (responseJson != null) {
            String token = null;
            try {
                token = tfaService.mapperResponse(responseJson, ResponseApi.class).getToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Token:" + token);
            Assert.assertEquals(verificationTokenOut, token);
        }
    }

    @When("I send a POST request to validate the two factor authentification")
    public void iSendAPOSTRequestToValidateTheTwoFactorAuthentification() {
        validatableResponse = tfaService.authentificationRequest(verificationTokenOut,deviceTokenOut);
    }

    @And("The response returns the user info which means that the tfa authentication is successful")
    public void theResponseReturnsTheUserInfoWhichMeansThatTheTfaAuthenticationIsSuccessful() {
        String responseJson = validatableResponse.extract().asPrettyString();
        if (responseJson != null) {
            try {
                firstnameOut = tfaService.mapperResponse(responseJson, RegisterUserParent.class).getUsers().get(0).getFirstName();
                System.out.print("firstname:" + firstnameOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @When("I send a POST request to create an user")
    public void iSendAPOSTRequestToCreateAnUser() {
        System.out.println(tfaService.createUserRequest().extract().asPrettyString());
    }

    @And("The response should have userid")
    public void theResponseShouldHaveUserid() {
    }

    @When("I send a POST request to add a phone number to an user")
    public void iSendAPOSTRequestToAddAPhoneNumberToAnUser() {
        validatableResponse=tfaService.setPhoneNumberRequest("+33666475063","119427192");
    }

    @When("I send a PUT request to add a method of authentification")
    public void iSendAPUTRequestToAddAMethodOfAuthentification() {
        validatableResponse=tfaService.setTfaMethodRequest("119427192");

    }
}
