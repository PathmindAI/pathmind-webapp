@projectPage
Feature: Project page

  Scenario: Check breadcrumb projects btn
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Add second model to the exist project
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    When Open projects page
    When Open project AutotestProject on projects page
    Then Project page check that models count is 2

  Scenario: Check model page archive btn, move model to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click model 1 archive/unarchive button
    When In confirmation dialog click in 'Archive Model' button
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment name 'AutotestProject' NOT exist in archived/not archived tab

  Scenario: Check model page Unarchive btn, move model to active tab
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click model 1 archive/unarchive button
    When In confirmation dialog click in 'Archive Model' button
    When Open projects/model/experiment archived tab
    When Click model 1 archive/unarchive button
    When In confirmation dialog click in 'Unarchive Model' button
    When Check that model/experiment name 'AutotestProject' NOT exist in archived/not archived tab
    When Open models tab
    Then Check that model/experiment name '1' exist in archived/not archived tab

  Scenario: Edit project name from project page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click in 'Rename' button
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    Then Check that project name is AutotestEditName on project page
    When Open projects page
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page

  Scenario: Edit archived project name
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When In confirmation dialog click in 'Archive Project' button
    When Open projects/model/experiment archived tab
    When Open project AutotestProject on projects page
    When Click in 'Rename' button
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    Then Check that project name is AutotestEditName on project page
    When Open projects page
    When Open projects/model/experiment archived tab
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page
