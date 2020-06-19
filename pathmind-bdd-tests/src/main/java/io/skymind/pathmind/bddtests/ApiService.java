package io.skymind.pathmind.bddtests;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class ApiService extends PageObject {

    private static final EnvironmentVariables VARIABLES = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String PATHMIND_URL = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("base.url");
    private static final String PATHMIND_API_KEY = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("pathmind.api.key");

    public void sendLatestVersionNotification() {
        SerenityRest.
            given().
            header("Authorization", "Basic " + PATHMIND_API_KEY).
            when().
            post(PATHMIND_URL + "api/newVersionAvailable").
            then().log().body().statusCode(200);
    }
}
