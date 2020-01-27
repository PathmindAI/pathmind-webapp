Feature: Create new user

  Scenario Outline: Create new user
    Given Open page early-access-sign-up
    When Fill new user form with <First Name>, <Last Name>
    When Create new user click sign up button
    When Fill new user password <Password>
    When Fill new user confirmation password <Password>
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that user <First Name> <Last Name> successfully logged in
    Then Delete all cookies

    Examples:
    |First Name    |Last Name     |Password   |
    |Evgeniy       |Autotest      |Pass123456 |

  Scenario Outline: Create new user and login without email approve
    Given Open page early-access-sign-up
    When Fill new user form with <First Name>, <Last Name>
    When Create new user click sign up button
    When Fill new user password <Password>
    When Fill new user confirmation password <Password>
    When Create new user click sign in button
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that Create new user error Email is not verified shown
    Then Check that Create new user Resend btn is shown

    Examples:
      |First Name    |Last Name     |Password   |
      |Evgeniy       |Autotest      |Pass123456 |

  Scenario: Check create new user page elements
    Given Open page early-access-sign-up
    When Check create new user page elements

  Scenario: Check create new user page cancel btn
    Given Open page early-access-sign-up
    When Click create new user cancel btn
    Then Check that login page opened

  Scenario Outline: Check create new user page wrong email alert
    Given Open page early-access-sign-up
    When Fill new user form with email <Email>
    When Create new user click sign up button
    Then Check new user page email alert message

    Examples:
      |Email                    |
      |test_email               |
      |test_email@              |
      |test_email@testdomain    |
      |test_email@testdomain.   |
      |test_email@.com          |
      |test_email@.             |
      |@testdomain              |
      |@testdomain.com          |
      |@.com                    |
      |test@@testdomain.com     |

  Scenario Outline: Check create new user password error message
    Given Open page early-access-sign-up
    When Fill new user form with email test@test.com
    When Create new user click sign up button
    When Fill new user password <Password>
    When Fill new user confirmation password <Confirm password>
    When Create new user click sign in button
    Then Create new user check that error message shown <Error>

    Examples:
      |Password    |Confirm password  |Error                                                                                                                        |
      |Test        |Test              |* 6 min characters                                                                                                           |
      |TEST123     |TEST123           |* 1 lowercase character                                                                                                      |
      |testing     |testing           |* 1 uppercase character                                                                                                      |
      |123456      |123456            |* 1 uppercase character,* 1 lowercase character                                                                              |
      |Testing     |Retesting         |* New Password doesn't match Confirmation password                                                                           |
      |Test        |                  |* 6 min characters,* New Password doesn't match Confirmation password                                                        |
      |            |Testing           |* 6 min characters,* New Password doesn't match Confirmation password,* 1 uppercase character,* 1 lowercase character        |

  Scenario: Check create new user email error message
    Given Open page early-access-sign-up
    When Fill new user form with email evegeniy@skymind.io
    When Create new user click sign up button
    Then Create new user check that error message for email field shown This email is already used.
    Then Create new user check that forgot password btn exist

  Scenario: Check create new user email recovery page
    Given Open page early-access-sign-up
    When Fill new user form with email evegeniy@skymind.io
    When Create new user click sign up button
    When Create new user click reset password btn
    Then Check password recovery page elements