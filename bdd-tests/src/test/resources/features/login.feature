Feature: Login form tests

  Scenario Outline: Login with valid credentials
    Given Open pathmind page
    When Login with credentials <email>, <password>
    Then Check that user <name> successfully logged in
    Then Logout from pathmind

    Examples:
    |email          |password     |name |
    |bob@skymind.io |pw!skymind19 |Bob  |

  Scenario Outline: Login with invalid credentials
    Given Open pathmind page
    When Login with credentials <email>, <password>
    Then Check that login form warning message is shown

    Examples:
      |email              |password     |
      |bob@skymind.io     |invalidPass  |
      |invalid@skymind.io |pw!skymind19 |
      |fake@invalid.io    |fakePass     |

  Scenario: Check login page elements
    Given Open pathmind page
    Then Check login page elements