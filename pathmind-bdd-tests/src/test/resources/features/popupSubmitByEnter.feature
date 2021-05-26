Feature: Submit popup with enter button

  Scenario: Edit project name from project page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click in 'Rename' button
    When Input project name AutotestEditName to the edit popup
    When Click keyboard enter btn
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
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    When Open project AutotestProject on projects page
    When Click in 'Rename' button
    When Input project name AutotestEditName to the edit popup
    When Click keyboard enter btn
    Then Check that project name is AutotestEditName on project page
    When Open projects page
    When Open projects/model/experiment archived tab
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page

  Scenario: Create new project and put it to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When Click keyboard enter btn on confirmation popup
    When Open projects/model/experiment archived tab
    Then Check that project exist in project list AutotestProject
    When Open projects tab
    Then Check that project not exist in project list AutotestProject
    When Open projects/model/experiment archived tab
    When Click AutotestProject project archive/unarchive button
    When Click keyboard enter btn on confirmation popup
    Then Check that project not exist in project list AutotestProject
    When Open projects tab
    Then Check that project exist in project list AutotestProject
