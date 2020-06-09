@modelBreadcrumb
Feature: Models breadcrumbs

  Scenario: Click project breadcrumb from model page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project/ breadcrumb btn
    Then Check that project AutotestProject page is opened

  Scenario: Click projectS breadcrumb from model page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click projects breadcrumb btn
    Then Check that projects page opened
