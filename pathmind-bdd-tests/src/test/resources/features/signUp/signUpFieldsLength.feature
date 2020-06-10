@signup
Feature: Sign-Up fields length

  Scenario Outline: Check First/Last Name max length
    Given Open page early-access-sign-up
    When Fill new user form with first name <text>
    When Fill new user form with last name <text>
    When Fill temporary email to the new user form
    When Create new user click sign up button
    Then Check new password page opened

    Examples:
      | characters | text                                                                                                                                                                                                                                                       |
      | 250        | Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium. |
      | 249        | Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium  |

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