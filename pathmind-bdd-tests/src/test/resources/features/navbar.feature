Feature: Nav bar buttons

  Scenario: Click projects btn and check that projects page opened
    Given Login to the pathmind
    When Open projects page
    Then Check that projects page opened

  Scenario: Open home page and check Learn btn
    Given Login to the pathmind
    When Click learn btn
    Then Check that learn page https://help.pathmind.com/en/ opened
    Then Close browser tab

  Scenario: Click user account btn and check that account page opened
    Given Login to the pathmind
    When Open user account page
    Then Check that user account page opened

  Scenario: Click Access Token btn and check that account page opened
    Given Login to the pathmind
    When Click user menu 'Access Token' btn
    Then Check that user account page opened

  Scenario: Check nav bar Projects highlighted button
    Given Login to the pathmind
    Then Check that projects button highlight is true
    When Create new CoffeeShop project with single reward function
    Then Check that projects button highlight is false
    When Click project start run button
    Then Check that projects button highlight is false
    When Open projects page
    Then Check that projects button highlight is true
    When Open project AutotestProject on projects page
    Then Check that projects button highlight is false
    Then Click the experiment name 1
    Then Check that projects button highlight is false
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
