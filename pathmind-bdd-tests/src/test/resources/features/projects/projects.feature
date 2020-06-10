@projectsPage
Feature: Projects page

  Scenario: Create new project and check project exist on projects page
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Input model details "a user note"
    When Click wizard next step button
    Then Check that experiment page of the AutotestProject opened
    When Open projects page
    Then Check that project exist in project list AutotestProject

  Scenario: Create new project and put it to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When Confirm archive/unarchive popup
    When Open archives tab
    Then Check that project exist in project list AutotestProject
    When Open projects tab
    Then Check that project not exist in project list AutotestProject
    When Open archives tab
    When Click AutotestProject project archive/unarchive button
    When Confirm archive/unarchive popup
    Then Check that project not exist in project list AutotestProject
    When Open projects tab
    Then Check that project exist in project list AutotestProject

  Scenario: Edit project name from projects page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Click edit AutotestProject project icon from projects page
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page

  Scenario: Edit archived project name from projects page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When Confirm archive/unarchive popup
    When Open archives tab
    When Click edit AutotestProject project icon from projects page
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page
