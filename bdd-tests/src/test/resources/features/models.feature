Feature: Models page
  Scenario: Add second model to the exist project
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model CoffeeShopExportedModel.zip
    When Input model details 5, 4, CoffeeShopExportedModelGetObservation.txt
    When Check that project AutotestProject page is opened
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Project page check that models count is 2

  Scenario: Check breadcrumb projects btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Check model page archive btn, move model to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    When Click project archive button
    When Confirm archive popup
    When Open projects archived tab
    Then Check that model name 1 exist in archived tab
    When Open projects archived tab
    When Check that model NOT exist in archived tab

  Scenario: Check model page Unarchive btn, move model to active tab
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    When Click project archive button
    When Confirm archive popup
    When Open projects archived tab
    When Click project archive button
    When Confirm archive popup
    When Check that model NOT exist in archived tab
    When Open projects archived tab
    Then Check that model name 1 exist in archived tab

  Scenario: Add experiment to exist model
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project page new experiment button
    When Click back button
    Then Check that model name 2 Draft exist in archived tab