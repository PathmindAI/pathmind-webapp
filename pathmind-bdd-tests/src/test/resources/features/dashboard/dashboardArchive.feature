@dashboard
Feature: Dashboard archive feature

  @dashArchiveItem
  Scenario: Archive empty project from dashboard
    Given Login to the pathmind
    When Create new empty project
    When Open dashboard page
    When Click archive btn from dashboard
    When In confirmation dialog click in 'Archive' button
    When Open projects page
    When Check that project not exist in project list AutotestProject
    When Open projects/model/experiment archived tab
    When Check that project exist in project list AutotestProject

  @dashArchiveItem
  Scenario: Archive project with model from dashboard
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model tuple_models/CoffeeShopTuple.zip
    When Check that model successfully uploaded
    When Open dashboard page
    When Click archive btn from dashboard
    When In confirmation dialog click in 'Archive' button
    When Open projects page
    When Open project AutotestProject on projects page
    When Check that model/experiment name 'AutotestProject' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab

  @dashArchiveItem
  Scenario: Archive project with reward function from dashboard
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Open dashboard page
    When Click archive btn from dashboard
    When In confirmation dialog click in 'Archive' button
    When Open projects page
    When Open project AutotestProject on projects page
    When Click the model name 1
    When Check that model/experiment name 'AutotestProject' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab

  @dashArchiveItem
  Scenario: Archive project with started experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Open dashboard page
    When Click archive btn from dashboard
    When In confirmation dialog click in 'Archive' button
    When Open projects page
    When Open project AutotestProject on projects page
    When Click the model name 1
    When Check that model/experiment name 'AutotestProject' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
