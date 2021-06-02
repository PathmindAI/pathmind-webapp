@projectsPage
Feature: Projects page

  Scenario: Check projects page title
    Given Login to the pathmind
    When Open projects page
    Then Check page title is Projects

  Scenario: Create new project and check project exist on projects page
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Click projects breadcrumb btn
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

  @tempEmail
  Scenario: Check pathmind begin screen is projects page
    Given Create new user Autotest, User with password Pass123456
    When Open pathmind page
    Then Login with new user email and Pass123456
    When Check that projects page opened
