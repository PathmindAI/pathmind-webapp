@signup
Feature: Sign-Up email field

  Scenario Outline: Check create new user page wrong email alert
    Given Open page early-access-sign-up
    When Fill new user form with wrong email <Email>
    When Create new user click sign up button
    Then Check new user page email alert message

    Examples:
      | Email                  |
      | test_email             |
      | test_email@            |
      | test_email@testdomain  |
      | test_email@testdomain. |
      | test_email@.com        |
      | test_email@.           |
      | @testdomain            |
      | @testdomain.com        |
      | @.com                  |
      | test@@testdomain.com   |

  Scenario: Check create new user email already in use error message
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
