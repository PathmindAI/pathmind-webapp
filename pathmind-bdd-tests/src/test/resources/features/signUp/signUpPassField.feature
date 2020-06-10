@signup
Feature: Sign-Up password field

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
      | Password | Confirm password | Error                                                                                                                 |
      | Test     | Test             | * 6 min characters                                                                                                    |
      | TEST123  | TEST123          | * 1 lowercase character                                                                                               |
      | testing  | testing          | * 1 uppercase character                                                                                               |
      | 123456   | 123456           | * 1 uppercase character,* 1 lowercase character                                                                       |
      | Testing  | Retesting        | * New Password doesn't match Confirmation password                                                                    |
      | Test     |                  | * 6 min characters                                                                                                    |
      |          |                  | * 6 min characters,* 1 uppercase character,* 1 lowercase character                                                    |
      |          | Testing          | * 6 min characters,* New Password doesn't match Confirmation password,* 1 uppercase character,* 1 lowercase character |
