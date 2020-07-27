package io.skymind.pathmind.bddtests;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

public class ApiService extends PageObject {

    private static final EnvironmentVariables VARIABLES = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String PATHMIND_URL = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("base.url");
    private static final String PATHMIND_API_KEY = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("pathmind.api.key");

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
}
