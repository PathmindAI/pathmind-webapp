@experiment
Feature: Experiment share

  @tempEmail
  Scenario Outline: Check experiment share
    Given Open page sign-up
    When Fill new user form with name <First Name>, <Last Name>
    When Fill new user password '<Password>'
    When Fill new user confirmation password '<Password>'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that user <First Name> <Last Name> successfully logged in
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click experiment page share with support btn
    When In confirmation dialog click in 'Share Training' button
    When Click pop-up dialog close btn
    Then Check experiment name tag label 'Shared'
    When Save experiment url into the variable 'sharedExperimentUrl'
    When Delete all cookies
    When Login to the pathmind
    When Open page sharedExperimentUrl
    Then Check that experiment page title is 'Experiment #1'

    Examples:
      | First Name | Last Name | Password   |
      | BDD        | Autotest  | Pass123456 |

  @tempEmail
  Scenario Outline: Check that shared experiment not shown to the normal users
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click experiment page share with support btn
    When In confirmation dialog click in 'Share Training' button
    When Click pop-up dialog close btn
    Then Check experiment name tag label 'Shared'
    When Save experiment url into the variable 'sharedExperimentUrl'
    When Delete all cookies
    Given Open page sign-up
    When Fill new user form with name <First Name>, <Last Name>
    When Fill new user password '<Password>'
    When Fill new user confirmation password '<Password>'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that user <First Name> <Last Name> successfully logged in
    When Open page sharedExperimentUrl
    Then Check that experiment page title is 'Experiment #1'

    Examples:
      | First Name | Last Name | Password   |
      | BDD        | Autotest  | Pass123456 |

  @tempEmail
  Scenario Outline: Check that NOT shared experiment not shown to the normal users
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Save experiment url into the variable 'sharedExperimentUrl'
    When Delete all cookies
    Given Open page sign-up
    When Fill new user form with name <First Name>, <Last Name>
    When Fill new user password '<Password>'
    When Fill new user confirmation password '<Password>'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that user <First Name> <Last Name> successfully logged in
    When Open page sharedExperimentUrl
    Then Check that Invalid data error page opened

    Examples:
      | First Name | Last Name | Password   |
      | BDD        | Autotest  | Pass123456 |

  @tempEmail
  Scenario Outline: Check that NOT shared experiment not shown to the support users
    Given Open page sign-up
    When Fill new user form with name <First Name>, <Last Name>
    When Fill new user password '<Password>'
    When Fill new user confirmation password '<Password>'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that user <First Name> <Last Name> successfully logged in
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Save experiment url into the variable 'sharedExperimentUrl'
    When Delete all cookies
    When Login to the pathmind
    When Open page sharedExperimentUrl
    Then Check that Invalid data error page opened

    Examples:
      | First Name | Last Name | Password   |
      | BDD        | Autotest  | Pass123456 |
