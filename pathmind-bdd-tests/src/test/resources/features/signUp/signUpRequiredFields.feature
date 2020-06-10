@signup
Feature: SignUp required fields

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
