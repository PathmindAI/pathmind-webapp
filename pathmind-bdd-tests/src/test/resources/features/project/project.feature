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
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
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
    When Click archive/unarchive btn model '1' with package name 'coffeeshop' from left sidebar
    When Change models sidebar list to 'Archived'
    Then Check project page model '1' package name is coffeeshop
    When Change models sidebar list to 'Active'
    When Check project page model '1' not exist in the sidebar list

  Scenario: Check model page Unarchive btn, move model to active tab
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click archive/unarchive btn model '1' with package name 'coffeeshop' from left sidebar
#    When In confirmation dialog click in 'Archive' button
    When Change models sidebar list to 'Archived'
    When Click archive/unarchive btn model '1' with package name 'coffeeshop' from left sidebar
#    When In confirmation dialog click in 'Unarchive' button
    When Change models sidebar list to 'Archived'
    When Check project page model '1' not exist in the sidebar list
    When Change models sidebar list to 'Active'
    Then Check project page model '1' package name is coffeeshop

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
    When In confirmation dialog click in 'Archive' button
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

  Scenario: Check project archived label
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    Then Check that project exist in project list AutotestProject
    When Open project AutotestProject on projects page
    Then Check project/model title label tag is Archived
