@model
Feature: Model page

  Scenario: Add experiment to exist model
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project page new experiment button
    When Click back button
    Then Check that model/experiment name '2 Draft' exist in archived/not archived tab

  Scenario: Check model page archive btn, move experiment to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment archive button
    When Confirm archive/unarchive popup
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment name '1 Draft' NOT exist in archived/not archived tab

  Scenario: Check model page Unarchive btn
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment archive button
    When Confirm archive/unarchive popup
    When Open projects/model/experiment archived tab
    When Click experiment unarchive button
    When Confirm archive/unarchive popup
    When Check that model/experiment name '1 Draft' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab

  Scenario: Check model page elements
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check model page elements

  @saveModelDraft
  Scenario: Check that Model is saved as draft as soon as it is uploaded
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    Then Check that model successfully uploaded
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check that there are 1 model(s) with 'Draft' tag in project page

  @saveModelDraft
  Scenario: Check that after uploading a model and clicking in next, the model is still a draft
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    Then Click wizard model details next btn
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check that there are 1 model(s) with 'Draft' tag in project page

  @saveModelDraft
  Scenario: Check Save Model Draft with reward variables
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Click wizard model details next btn
    Then Check that button 'Save Draft' exists
    When Input reward variable names kitchen_cleanliness
    When Click wizard reward variables save draft btn
    Then Check that the notification 'Draft successfully saved' is shown
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check that there are 1 model(s) with 'Draft' tag in project page
    When Click the model name 2
    When Click wizard model details next btn
    And Check that there is a variable named kitchen_cleanliness
    When Click wizard reward variables next btn
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check that there are 0 model(s) with 'Draft' tag in project page

  Scenario: Check duplicated experiment names on model view
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open dashboard page
    When Click archive btn from dashboard
    When In confirmation dialog click in 'Archive Experiment' button
    When Click stage write reward function AutotestProject from dashboard
    When Open projects page
    When Open project AutotestProject on projects page
    When Click the model name 1
    When Check that model/experiment name '2 Draft' exist in archived/not archived tab
    When Check that model/experiment name '1 Draft' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment name '1 Draft' exist in archived/not archived tab
    When Check that model/experiment name '2 Draft' NOT exist in archived/not archived tab
