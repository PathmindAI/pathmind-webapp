@e2e
Feature: E2E
  Scenario Outline: Create new project and run experiment
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    Then Check that experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    Then Click project start run button
    Then Check experiment status completed with <limit> hours
    Then Check experiment score greater than <score>

    Examples:
      | project name    | model                                                              | reward function file                                              | score    | limit   |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip   | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt  | 0.9      | 15      |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip                 | Production_Single_Agent/CoffeeShopPathmindDemo.txt                | 120      | 15      |