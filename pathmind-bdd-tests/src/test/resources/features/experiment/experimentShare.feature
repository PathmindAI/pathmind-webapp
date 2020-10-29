@experiment
Feature: Experiment share

  Scenario: Check experiment share
    Given Open page sign-up
    When Fill new user form with name <First Name>, <Last Name>
    When Create new user click sign up button
    When Fill new user password <Password>
    When Fill new user confirmation password <Password>
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that user <First Name> <Last Name> successfully logged in
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click in 'Share with support' button
    When In confirmation dialog click in 'Share Training' button
    Then Check experiment name tag label 'Shared with Support'
    When Save experiment url into the variable 'sharedExperimentUrl'
    When Delete all cookies
    When Login to the pathmind
    When Open page sharedExperimentUrl
   Then Check experiment name tag label 'Shared with Support'
