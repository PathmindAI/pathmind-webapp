Feature: User accounts tests

  Scenario: Edit user email in account page
    Given Open page early-access-sign-up
    When Fill new user form with name EditEmail, User
    When Create new user click sign up button
    When Fill new user password Abcd1234
    When Fill new user confirmation password Abcd1234
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

  Scenario: Login with old email after email change
    Given Open page early-access-sign-up
    When Fill new user form with name EditEmail, User
    When Create new user click sign up button
    When Fill new user password Abcd1234
    When Fill new user confirmation password Abcd1234
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
    Then Check that login form warning message is shown