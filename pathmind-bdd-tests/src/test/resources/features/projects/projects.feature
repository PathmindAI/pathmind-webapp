@projectsPage
Feature: Projects page

  Scenario: Check projects page title
    Given Login to the pathmind
    When Open projects page
    Then Check page title is Projects

  Scenario: Create new project and check project exist on projects page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    Then Check that project exist in project list AutotestProject

  Scenario: Create new project and put it to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    Then Check that project exist in project list AutotestProject
    When Open projects tab
    Then Check that project not exist in project list AutotestProject
    When Open projects/model/experiment archived tab
    When Click AutotestProject project archive/unarchive button
    When In confirmation dialog click in 'Unarchive' button
    Then Check that project not exist in project list AutotestProject
    When Open projects tab
    Then Check that project exist in project list AutotestProject

  Scenario: Edit project name from projects page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Click edit AutotestProject project icon from projects page
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page

  Scenario: Edit archived project name from projects page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    When Click edit AutotestProject project icon from projects page
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page
