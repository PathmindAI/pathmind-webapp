@modelDetails
Feature: Check Model Details Actions/Observations fields

  Scenario Outline: Check Model Details Actions/Observations fields
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
    Then Click project save draft btn
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details actions is <actions>
    Then Check model page model details observations is <observations>

    Examples:
      | project name    | model                                       | reward function file                                             | actions | observations |
      | AutotestProject | tuple_models/CallCenterTuples.zip           | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt | 5       | 60            |
      | AutotestProject | tuple_models/CoffeeShopTuple.zip            | Production_Single_Agent/CoffeeShopPathmindDemo.txt               | 4       | 5            |
      | AutotestProject | tuple_models/SimpleSchedulingTuplesTest.zip | Production_Single_Agent/Two_Variables_Reward_Function.txt        | 6       | 21            |
