Feature: Wizard page

  Scenario: Open create new project page and check elements
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    Then Check Create A New Project page

  Scenario: Project error if name already exist
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Click create new project button
    When Input already exist name of the project to the project name
    Then Check that error shown Project name should be unique

  Scenario Outline: Create new project with max length 99 and 100
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <text> and click Create project button
    Then Check that model upload page opened

    Examples:
      | characters | text                                                                                    |
      | 100        | Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget do |
      | 99         | Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget d  |

  Scenario: Check Reward Function default value
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that experiment AutotestProject page is opened
    Then Check Reward Function default value reward = after[0] - before[0];