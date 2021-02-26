package io.skymind.pathmind.bddtests;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.exceptions.SerenityManagedException;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class EmailApi extends PageObject {

    private static String emailApiUrl = "http://api.guerrillamail.com/ajax.php";

    public String getEmail() {

        int attempts = 0;
        Response response = null;
        while (attempts < 10) {
            try {
                response = SerenityRest
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
                waitABit(10000);
                deleteEmail(1);
                break;
            } catch (Exception e) {
                System.out.print("Wait for email API");
                waitABit(60000);
            }
            attempts++;
        }
        return response.jsonPath().get("email_addr");
    }

    public String getVerificationLink() {

        String body = getMailResponse().jsonPath().get("mail_body").toString();

        return body.split("email-verification/")[1].split("<p>")[0];
    }

    private void deleteEmail(int emailId) {
        int attempts = 0;
        Response deleteEmail = null;
        while (attempts < 10) {
            try {
                deleteEmail = SerenityRest
                    .given()
                    .cookie("PHPSESSID", Serenity.sessionVariableCalled("sessId"))
                    .queryParam("f", "del_email")
                    .queryParam("email_ids[]", emailId)
                    .when()
                    .get(emailApiUrl);
                deleteEmail
                    .then()
                    .log()
                    .all();
                break;
            } catch (SerenityManagedException e) {
                System.out.print("Wait for email API");
                waitABit(60000);
            }
            attempts++;
        }
    }

    private int getInboxCount() {
        int attempts = 0;
        Response getInboxCount = null;
        while (attempts < 10) {
            try {
                getInboxCount = SerenityRest
                    .given()
                    .cookie("PHPSESSID", Serenity.sessionVariableCalled("sessId"))
                    .queryParam("f", "get_email_list")
                    .queryParam("offset", "0")
                    .when()
                    .get(emailApiUrl);
                getInboxCount
                    .then()
                    .log()
                    .all();
                if (getInboxCount.statusCode() == 200) {
                    break;
                }
            } catch (SerenityManagedException e) {
                System.out.print("Wait for email API");
                waitABit(60000);
            }
            waitABit(60000);
            attempts++;
        }
        return Integer.parseInt(getInboxCount.jsonPath().get("count"));
    }

    private void waitEmail() {
        for (int i = 0; i < 25; i++) {
            if (getInboxCount() > 0) {
                break;
            } else {
                waitABit(10000);
            }
        }
    }

    private Response getMailResponse() {

        waitEmail();
        int attempts = 0;
        Response getMailId = null;
        Response getMail = null;
        while (attempts < 10) {
            try {
                getMailId = SerenityRest
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

                getMail = SerenityRest
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
                break;
            } catch (SerenityManagedException e) {
                System.out.print("Wait for email API");
                waitABit(60000);
            }
            attempts++;
        }

        assertThat(getMail.jsonPath().get("mail_from").toString(), containsString("support@pathmind.com"));

        return getMail;
    }

    public void checkUserVerificationEmail() {
        Response response = getMailResponse();
        String body = response.jsonPath().get("mail_body").toString();

        assertThat(response.jsonPath().get("mail_from").toString(), is("support@pathmind.com"));
        assertThat(response.jsonPath().get("mail_subject").toString(), is("Verify your new email address"));
        System.out.println("EMAIL: " + body);
        assertThat(body.split("<p>")[1].split("<p>")[0], is("Hi EditEmail User!</p>\n    "));
        assertThat(body.split("Hi EditEmail User!</p>\n    ")[1].split("<a")[0], is("<p>Please verify your email address by clicking "));
        assertThat(body.split("<a href=")[1].split(">")[1].split("</a>")[0], is("this link</a"));
        assertThat(body.split("email-verification/")[1].split("<p>")[1].split("</p>")[0], is("\n        - Team Pathmind<br><br>\n        ----<br>\n        Pathmind.com\n    "));
    }
}
