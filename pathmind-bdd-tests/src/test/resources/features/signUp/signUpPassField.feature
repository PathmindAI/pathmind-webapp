@signup
Feature: Sign Up password field

  Scenario Outline: Check create new user password error message
    Given Open page sign-up
    When Fill new user form with first name AutotestFirstName
    When Fill new user form with last name AutotestLastName
    When Fill new user form with email autotest@autotest.com
    When Fill new user password '<Password>'
    When Fill new user confirmation password '<Confirm password>'
    When Create new user click sign in button
    Then Create new user check that error message shown <Error>

    Examples:
      | Password | Confirm password | Error                                                                                                                 |
      | Test     | Test             | * 6 min characters                                                                                                    |
      | TEST123  | TEST123          | * 1 lowercase character                                                                                               |
      | testing  | testing          | * 1 uppercase character                                                                                               |
      | 123456   | 123456           | * 1 uppercase character,* 1 lowercase character                                                                       |
      | Testing  | Retesting        | * New Password doesn't match Confirmation password                                                                    |
      | Test     |                  | * 6 min characters                                                                                                    |

  Scenario: Check create new user empty password error message
    Given Open page sign-up
    When Fill new user form with first name AutotestFirstName
    When Fill new user form with last name AutotestLastName
    When Fill new user form with email autotest@autotest.com
    When Create new user click sign in button
    Then Create new user check that error message shown * 6 min characters,* 1 uppercase character,* 1 lowercase character

  Scenario: Check create new user empty password error message
    Given Open page sign-up
    When Fill new user form with first name AutotestFirstName
    When Fill new user form with last name AutotestLastName
    When Fill new user form with email autotest@autotest.com
    When Fill new user password 'Test'
    When Fill new user confirmation password 'Testing'
    When Fill new user password ''
    When Create new user click sign in button
    Then Create new user check that error message shown * New Password doesn't match Confirmation password,* 6 min characters
