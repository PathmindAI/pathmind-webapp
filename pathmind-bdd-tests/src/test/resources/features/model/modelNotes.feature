@modelNotes
Feature: Models notes

  @notes
  Scenario: Adding notes to the model
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Add note AutotestNote to the project page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check project note is AutotestNote

  @notes
  Scenario: Check that subtle checkmark shown after model note saved
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Add note AutotestNote to the project page
    Then Check that checkmark is shown
