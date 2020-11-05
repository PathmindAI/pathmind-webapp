package io.skymind.pathmind.bddtests;

import io.restassured.response.ValidatableResponse;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

import javax.xml.bind.DatatypeConverter;

public class ApiService extends PageObject {

    private static final EnvironmentVariables VARIABLES = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String PATHMIND_URL = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("base.url");
    private static final String PATHMIND_API_KEY = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("pathmind.api.key");
    private static final String PATHMIND_API_URL = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("api.url");

    public void sendLatestVersionNotification() {
        waitABit(5000);
        try {
            System.out.println(PATHMIND_URL + "api/newVersionAvailable");
            SerenityRest.
                    given().
                    header("Authorization", "Basic " + DatatypeConverter.printBase64Binary(("api:" + PATHMIND_API_KEY).getBytes("UTF-8"))).
                    when().
                    post(PATHMIND_URL + "api/newVersionAvailable").
                    then().log().all().statusCode(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ValidatableResponse getUserProjects() {
        return SerenityRest.
            given().
            header("X-PM-API-TOKEN", Serenity.sessionVariableCalled("apiKey")).
            when().
            get(PATHMIND_API_URL + "projects").
            then().
            log().
            all().
            statusCode(200);
    }
}
