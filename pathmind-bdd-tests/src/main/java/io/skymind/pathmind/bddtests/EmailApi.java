package io.skymind.pathmind.bddtests;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class EmailApi extends PageObject {

    private static String emailApiUrl = "http://api.guerrillamail.com/ajax.php";

    public String getEmail(){
        Response response = SerenityRest
                .given()
                .queryParam("f", "get_email_address")
                .when()
                .get(emailApiUrl);
        response
                .then()
                .log()
                .all()
                .statusCode(200);
        Serenity.setSessionVariable("email").to(response.jsonPath().get("email_addr"));
        Serenity.setSessionVariable("sessId").to(response.getCookie("PHPSESSID"));
        waitABit(2000);
        deleteEmail(1);
        return response.jsonPath().get("email_addr");
    }

    public String fetchEmail(){

        for(int i = 0; i < 80; i++){
            if(getInboxCount() > 0){
                break;
            }else {
                waitABit(3000);
            }
        }

        Response getMailId = SerenityRest
                .given()
                .cookie("PHPSESSID", Serenity.sessionVariableCalled("sessId"))
                .queryParam("f", "get_email_list")
                .queryParam("offset", "0")
                .when()
                .get(emailApiUrl);
        getMailId
                .then()
                .log()
                .all()
                .statusCode(200);

        Response getMail = SerenityRest
                .given()
                .cookie("PHPSESSID", Serenity.sessionVariableCalled("sessId"))
                .queryParam("f", "fetch_email")
                .queryParam("email_id", getMailId.jsonPath().get("list.mail_id[0]").toString())
                .when()
                .get(emailApiUrl);
        getMail
                .then()
                .log()
                .all()
                .statusCode(200);
        assertThat(getMail.jsonPath().get("mail_from").toString(), containsString("support@pathmind.com"));

        String body = getMail.jsonPath().get("mail_body").toString();

        return body.split("email-verification/")[1].split("<p>")[0];
    }

    private void deleteEmail(int emailId){
        Response deleteEmail = SerenityRest
                .given()
                .cookie("PHPSESSID", Serenity.sessionVariableCalled("sessId"))
                .queryParam("f", "del_email")
                .queryParam("email_ids[]", emailId)
                .when()
                .get(emailApiUrl);
        deleteEmail
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    private int getInboxCount(){
        Response getInboxCount = SerenityRest
                .given()
                .cookie("PHPSESSID", Serenity.sessionVariableCalled("sessId"))
                .queryParam("f", "get_email_list")
                .queryParam("offset", "0")
                .when()
                .get(emailApiUrl);
        getInboxCount
                .then()
                .log()
                .all()
                .statusCode(200);

        return Integer.parseInt(getInboxCount.jsonPath().get("count"));
    }
}
