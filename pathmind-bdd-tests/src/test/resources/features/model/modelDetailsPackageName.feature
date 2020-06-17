@modelDetails
Feature: Check Model Details package name field

  Scenario Outline: Check Model Details package name field
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Click project save draft btn
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details package name is <package name>

    Examples:
      | project name    | model                                                            | reward function file                                                        | package name |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt            | icebreaker   |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip               | Production_Single_Agent/CoffeeShopPathmindDemo.txt                          | coffeeshop   |
      | AutotestProject | Production_Single_Agent/FAST_RailModel_4Observation_3Actions.zip | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | railmodel    |
