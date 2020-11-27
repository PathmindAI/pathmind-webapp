@account
Feature: User accounts tests

  @tempEmail
  Scenario: Edit user email in account page
    Given Open page sign-up
    When Fill new user form with name EditEmail, User
    When Fill new user password 'Abcd1234'
    When Fill new user confirmation password 'Abcd1234'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and Abcd1234
    When Open user account page
    When Click account edit btn
    When Input account page new temp email
    When Click account edit update btn
    When In confirmation dialog click in 'Update' button
    When In confirmation dialog click in 'OK' button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and Abcd1234
    Then Check that user EditEmail successfully logged in
    When Open user account page
    Then Check user email is correct

  @tempEmail
  Scenario: Login without email verify after email change
    Given Open page sign-up
    When Fill new user form with name EditEmail, User
    When Fill new user password 'Abcd1234'
    When Fill new user confirmation password 'Abcd1234'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and Abcd1234
    When Open user account page
    When Click account edit btn
    When Input account page new temp email
    When Click account edit update btn
    When In confirmation dialog click in 'Update' button
    When In confirmation dialog click in 'OK' button
    When Open pathmind page
    Then Login with new user email and Abcd1234
    And Wait for sign-in page anti-flicker script
    Then Check that login form warning message is shown

  @tempEmail
  Scenario: Login with old email after email change
    Given Open page sign-up
    When Fill new user form with name EditEmail, User
    When Fill new user password 'Abcd1234'
    When Fill new user confirmation password 'Abcd1234'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and Abcd1234
    When Open user account page
    When Click account edit btn
    When Input account page new temp email
    When Click account edit update btn
    When In confirmation dialog click in 'Update' button
    When In confirmation dialog click in 'OK' button
    When Get email and verify user email
    When Open pathmind page
    When Login with old user email and password Abcd1234
    And Wait for sign-in page anti-flicker script
    Then Check that login form warning message is shown

  @tempEmail
  Scenario: Check verification email template
    Given Open page sign-up
    When Fill new user form with name EditEmail, User
    When Fill new user password 'Abcd1234'
    When Fill new user confirmation password 'Abcd1234'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and Abcd1234
    When Open user account page
    When Click account edit btn
    When Input account page new temp email
    When Click account edit update btn
    When In confirmation dialog click in 'Update' button
    When In confirmation dialog click in 'OK' button
    When Check user verification email

  Scenario: Check account edit breadcrumb
    Given Login to the pathmind
    When Open page account/edit
    Then Check account page footer components
    When Click account breadcrumb btn
    Then Check that user account page opened
    Then Check account page footer components

  Scenario: Check Change Password breadcrumb
    Given Login to the pathmind
    When Open page account/change-password
    Then Check account page footer components
    When Click account breadcrumb btn
    Then Check that user account page opened
    Then Check account page footer components

  Scenario: Check account name edit
    Given Login to the pathmind
    When Open page account
    When Click account edit btn
    When Input account page first name 'FirstName'
    When Input account page last name 'LastName'
    When Click account edit update btn
    Then Check that user name changed to 'FirstName' 'LastName'

  Scenario: Check account footer components links
    Given Login to the pathmind
    When Open page account
    Then Check account page footer components
    When Click account footer 'Privacy Policy' btn
    When Open tab 1
    When Check page url is https://pathmind.com/privacy-policy/
    When Open tab 0
    When Click account footer 'Terms of Use' btn
    When Open tab 2
    When Check page url is https://pathmind.com/subscription-agreement/
