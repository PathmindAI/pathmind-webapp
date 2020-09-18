@model
Feature: Model page

  @smoke
  Scenario: Add experiment to exist model
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project page new experiment button
    Then Check that experiment page title is 'Experiment #2'
    When Click back button
    Then Check that model/experiment name '2 Draft' exist in archived/not archived tab

  Scenario: Check model page experiment archive btn, move experiment to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment archive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment name '1 Draft' NOT exist in archived/not archived tab

  Scenario: Check model page experiment Unarchive btn
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment archive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    When Click experiment unarchive button
    When In confirmation dialog click in 'Unarchive' button
    When Check that model/experiment name '1 Draft' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab

  Scenario: Check model page model archive btn
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check model page model archived tag is shown false
    When Click model page model archive/unarchive button
    Then Check model page model archived tag is shown true
    When Click project/ breadcrumb btn
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab
    When Click the model name 1
    Then Check model page model archived tag is shown true
    When Click model page model archive/unarchive button
    Then Check model page model archived tag is shown false
    When Click project/ breadcrumb btn
    Then Check that model/experiment name '1' exist in archived/not archived tab
    When Click the model name 1
    Then Check model page model archived tag is shown false

  Scenario: Check model page elements
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check model page elements

  Scenario: Check duplicated experiment names on model view
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open dashboard page
    When Click archive btn from dashboard
    When In confirmation dialog click in 'Archive' button
    When Click stage write reward function AutotestProject from dashboard
    When Open projects page
    When Open project AutotestProject on projects page
    When Click the model name 1
    When Check that model/experiment name '2 Draft' exist in archived/not archived tab
    When Check that model/experiment name '1 Draft' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment name '1 Draft' exist in archived/not archived tab
    When Check that model/experiment name '2 Draft' NOT exist in archived/not archived tab

  Scenario: Check model archived label
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click model '1' archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    When Click the model name 1
    Then Check project/model title label tag is Archived

  Scenario: Check draft model archived label
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Open projects page
    When Open project AutotestProject on projects page
    When Click model '1' archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    When Click the model name 1
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    When Click model breadcrumb btn
    Then Check project/model title label tag is Archived