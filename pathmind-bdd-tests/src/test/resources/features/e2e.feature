@e2e
Feature: E2E

  Scenario Outline: Create new project and run experiment
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    Then Click project start run button
    Then Check experiment status completed with <limit> hours

    Examples:
      | project name    | model                             | reward function file                                       | limit |
      | AutotestProject | tuple_models/CallCenterTuples.zip | Production_Single_Agent/Production_Single_Agent_Reward.txt | 15    |
      | AutotestProject | tuple_models/CoffeeShopTuple.zip  | Production_Single_Agent/CoffeeShopPathmindDemo.txt         | 15    |
