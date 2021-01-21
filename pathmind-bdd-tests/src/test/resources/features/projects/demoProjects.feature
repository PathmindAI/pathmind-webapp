@projectsPage
Feature: Demo projects

  Scenario: Check demo projects btn
    Given Login to the pathmind
    When Open projects page
    When Click in 'Example Projects' button
    When Check that demo projects popup is shown 'true'
    When Close demo projects pop-up
    When Check that demo projects popup is shown 'false'
  @debug
  Scenario: Check demo projects btn
    Given Login to the pathmind
    When Open projects page
    When Click in 'Example Projects' button
    When Check demo popup elements
