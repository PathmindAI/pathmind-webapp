@signup
Feature: Sign-Up

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
      | First Name | Last Name | Password   |
      | Evgeniy    | Autotest  | Pass123456 |

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
      | First Name | Last Name | Password   |
      | Evgeniy    | Autotest  | Pass123456 |

  Scenario: Check create new user page elements
    Given Open page early-access-sign-up
    When Check create new user page elements
