Feature: Wizard page
  Scenario: Open create new project page and check elements
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    Then Check Create A New Project page

  Scenario: Project wizard click download it here button
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Project wizard click download it here btn
    Then Check that learn page https://help.pathmind.com/en/articles/3354371-using-the-pathmind-helper/ opened
    Then Close browser tab

  Scenario: Project wizard click For more details, see our documentation button
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Project wizard click For more details, see our documentation btn
    Then Check that learn page https://help.pathmind.com/en/articles/3354371-using-the-pathmind-helper opened
    Then Close browser tab

  Scenario: Project wizard check empty project text
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    Then Check text in the project page

  Scenario: Project error if name already exist
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Click create new project button
    When Input already exist name of the project to the project name
    Then Check that error shown Project name should be unique

  Scenario: Project wizard click breadcrumb to projects btn
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Click projects breadcrumb btn
    Then Check that projects page opened