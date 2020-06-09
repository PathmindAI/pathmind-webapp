@dashboard
Feature: Dashboard breadcrumbs feature

  Scenario: Open project breadcrumb from dashboard
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    When Click project AutotestProject from dashboard
    Then Check that project AutotestProject page is opened

  Scenario: Open model breadcrumb from dashboard
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    When Click model breadcrumb AutotestProject from dashboard
    Then Check that models page opened

  Scenario: Open experiment breadcrumb from dashboard
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    When Click experiment breadcrumb AutotestProject from dashboard
    Then Check that newExperiment page opened
