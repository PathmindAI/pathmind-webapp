@signin
Feature: Sign In form tests

  Scenario Outline: Login with valid credentials
    Given Open pathmind page
    When Login with credentials <email>, <password>
    Then Check that user <name> successfully logged in
    Then Logout from pathmind
    When Open page /
    Then Check login page elements

    Examples:
      | email          | password     | name |
      | bob@skymind.io | pw!skymind19 | Bob  |

  Scenario Outline: Login with invalid credentials
    Given Open pathmind page
#    And Wait for sign-in page anti-flicker script
    When Login with credentials <email>, <password>
#    And Wait for sign-in page anti-flicker script
    Then Check that login form warning message is shown

    Examples:
      | email              | password     |
      | bob@skymind.io     | invalidPass  |
      | invalid@skymind.io | pw!skymind19 |
      | fake@invalid.io    | fakePass     |

  Scenario: Check login page elements
    Given Open pathmind page
    And Wait for sign-in page anti-flicker script
    When Click in 'Already have an account?' button
    Then Check login page elements
    # Then Check network errors

  Scenario: Check Get started btn
    Given Open pathmind page
    When Click in 'Already have an account?' button
    Then Click in 'Get started' button
    Then Check create new user page elements

  Scenario Outline: Check css styles error the name "vaadin-device-detector" has already been used with this registry
    Given Open pathmind page
    Then Check console error <error>

    Examples:
      | error                                                                                                                       |
      | Uncaught DOMException: Failed to execute 'define' on 'CustomElementRegist…etector" has already been used with this registry |
      | Uncaught TypeError: $0.page is not a function                                                                               |
