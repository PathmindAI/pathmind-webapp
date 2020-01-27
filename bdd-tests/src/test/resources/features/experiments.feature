Feature: Experiments page
  Scenario: Check breadcrumb models btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click models breadcrumb btn
    Then Check that models page opened

  Scenario: Check breadcrumb projects btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Check breadcrumb projects btn from experiment edit page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Check breadcrumb models btn from experiment edit page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click models breadcrumb btn
    Then Check that models page opened

  Scenario: Check breadcrumb experiments btn from experiment edit page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click experiments breadcrumb btn
    Then Check that experiments page opened

  Scenario: Check experiment page archive btn, move model to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project archive button
    When Confirm archive popup
    When Open projects archived tab
    Then Check that model name 1 Draft exist in archived tab
    When Open projects archived tab
    When Check that model NOT exist in archived tab

  Scenario: Check experiment page Unarchive btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project archive button
    When Confirm archive popup
    When Open projects archived tab
    When Click project archive button
    When Confirm archive popup
    When Check that model NOT exist in archived tab
    When Open projects archived tab
    Then Check that model name 1 Draft exist in archived tab

  Scenario: Check show reward function btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project page new experiment button
    Then Input reward function reward -= after[3] - before[3];
    When Click project save draft btn
    When Click back button
    Then Click 2 experiment show reward function btn
    Then Check reward function is reward -= after[3] - before[3];
    Then Click 1 experiment show reward function btn
    Then Check reward function is reward -= after[1] - before[1];

  Scenario: Check experiment page elements
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check experiments page elements

  Scenario: Edit exist experiment
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Input reward function reward -= after[3] - before[3];
    When Click project save draft btn
    When Click back button
    Then Click 1 experiment show reward function btn
    Then Check reward function is reward -= after[3] - before[3];