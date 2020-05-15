Feature: Create new user

  Scenario Outline: Create new user
    Given Open page early-access-sign-up
    When Fill new user form with name <First Name>, <Last Name>
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
    When Fill new user form with name <First Name>, <Last Name>
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

  Scenario Outline: Check create new user page wrong email alert
    Given Open page early-access-sign-up
    When Fill new user form with wrong email <Email>
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
    When Fill new user form with first name AutotestFirstName
    When Fill new user form with last name AutotestLastName
    When Fill new user form with email autotest@autotest.com
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
      |Test        |                  |* 6 min characters                                                                                                           |
      |            |                  |* 6 min characters,* 1 uppercase character,* 1 lowercase character                                                           |
      |            |Testing           |* 6 min characters,* New Password doesn't match Confirmation password,* 1 uppercase character,* 1 lowercase character        |

  Scenario: Check create new user email error message
    Given Open page early-access-sign-up
    When Fill new user form with first name AutotestFirstName
    When Fill new user form with last name AutotestLastName
    When Fill new user form with exist email evegeniy@skymind.io
    When Create new user click sign up button
    Then Create new user check that error message for email field shown This email is already used
    Then Create new user check that forgot password btn exist

  Scenario: Check create new user email recovery page
    Given Open page early-access-sign-up
    When Fill new user form with first name AutotestFirstName
    When Fill new user form with last name AutotestLastName
    When Fill new user form with exist email evegeniy@skymind.io
    When Create new user click sign up button
    When Create new user click reset password btn
    Then Check password recovery page elements

  Scenario: Check early access First/Last Name, email required messages are shown
    Given Open page early-access-sign-up
    When Create new user click sign up button
    Then Check that early access error message First Name is required is shown for First Name field
    Then Check that early access error message Last Name is required is shown for Last Name field
    Then Check that early access error message Email is required is shown for Work Email field

  Scenario: Check early access First/Last Name required messages are shown
    Given Open page early-access-sign-up
    When Fill temporary email to the new user form
    When Create new user click sign up button
    Then Check that early access error message First Name is required is shown for First Name field
    Then Check that early access error message Last Name is required is shown for Last Name field

  Scenario: Check early access First Name required messages is shown
    Given Open page early-access-sign-up
    When Fill new user form with last name AutotestLastName
    When Fill temporary email to the new user form
    When Create new user click sign up button
    Then Check that early access error message First Name is required is shown for First Name field

  Scenario: Check early access Last Name required messages is shown
    Given Open page early-access-sign-up
    When Fill new user form with first name AutotestFirstName
    When Fill temporary email to the new user form
    When Create new user click sign up button
    Then Check that early access error message Last Name is required is shown for Last Name field

  Scenario: Check early access email required messages is shown
    Given Open page early-access-sign-up
    When Fill new user form with first name AutotestFirstName
    When Fill new user form with last name AutotestLastName
    When Create new user click sign up button
    Then Check that early access error message Email is required is shown for Work Email field

  Scenario Outline: Check First/Last Name max length
    Given Open page early-access-sign-up
    When Fill new user form with first name <text>
    When Fill new user form with last name <text>
    When Fill temporary email to the new user form
    When Create new user click sign up button
    Then Check new password page opened

    Examples:
      | characters  | text                                                                                                                                                                                                                                                          |
      | 250         | Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium.    |
      | 249         | Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium     |

  Scenario: Check First/Last Name 251 chars length error
    Given Open page early-access-sign-up
    When Fill new user form with first name Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium q
    When Fill new user form with last name Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium q
    When Fill temporary email to the new user form
    When Create new user click sign up button
    Then Check that early access error message First Name must not exceed 250 characters is shown for First Name field
    Then Check that early access error message Last Name must not exceed 250 characters is shown for Last Name field

  Scenario: Check password 51 chars length error
    Given Open page early-access-sign-up
    When Fill new user form with first name AutotestFirstName
    When Fill new user form with last name AutotestLastName
    When Fill temporary email to the new user form
    When Create new user click sign up button
    When Fill new user password Lorem ipsum dolor sit amet, consectetuer adipiscing
    When Fill new user confirmation password Lorem ipsum dolor sit amet, consectetuer adipiscing
    When Create new user click sign in button
    Then Create new user check that error message shown * 50 max characters