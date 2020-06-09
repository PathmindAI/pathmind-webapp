@dashboard
Feature: Dashboard notes feature

  Scenario: Check experiment notes for dashboard item without experiment
    Given Login to the pathmind
    When Create new empty project
    When Open dashboard page
    Then Check that AutotestProject experiment notes does not exist

  Scenario: Check experiment notes for dashboard item with experiment that has empty notes
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    Then Check dashboard AutotestProject experiment notes is —

  Scenario: Check experiment notes for dashboard item with experiment that has some notes
    Given Login to the pathmind
    When Create new CoffeeShop project with experiment notes
    When Open dashboard page
    Then Check dashboard AutotestProject experiment notes is This is the experiment notes for this Coffee Shop project fast speed model.
