@projectPage
Feature: Project page

  Scenario: Check breadcrumb projects btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Add second model to the exist project
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Input model details "a user note"
    When Click wizard next step button
    Then Check that experiment page of the AutotestProject opened
    When Open projects page
    When Open project AutotestProject on projects page
    Then Project page check that models count is 2

  Scenario: Check model page archive btn, move model to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click model 1 archive/unarchive button
    When Confirm archive/unarchive popup
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment NOT exist in archived tab

  Scenario: Check model page Unarchive btn, move model to active tab
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click model 1 archive/unarchive button
    When Confirm archive/unarchive popup
    When Open archives tab
    When Click model 1 archive/unarchive button
    When Confirm archive/unarchive popup
    When Check that model/experiment NOT exist in archived tab
    When Open models tab
    Then Check that model/experiment name '1' exist in archived/not archived tab

  Scenario: Edit project name from project page
    Given Login to the pathmind
    When Create new CoffeeShop project
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
    When Create new CoffeeShop project
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When Confirm archive/unarchive popup
    When Open archives tab
    When Open project AutotestProject on projects page
    When Click in 'Rename' button
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    Then Check that project name is AutotestEditName on project page
    When Open projects page
    When Open archives tab
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page
