@projectNotes
Feature: Project page

  @notes
  Scenario: Adding notes to the project
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Add note AutotestNote to the project page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check project note is AutotestNote

  @notes
  Scenario: Check that subtle checkmark shown after project note saved
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Add note AutotestNote to the project page
    Then Check that checkmark is shown
