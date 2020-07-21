@modelDetails
Feature: Check Model Details Reward Variables fields

  Scenario Outline: Check Model Details Reward Variables fields
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    When Click wizard model details next btn
    When Input reward variable names <variables>
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details reward variables order
    Then Check model page model details reward variables is <variables>

    Examples:
      | project name    | model                                       | reward function file                                             | variables                                                               |
      | AutotestProject | tuple_models/CallCenterTuples.zip           | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt | cheese_var                                                              |
      | AutotestProject | tuple_models/CoffeeShopTuple.zip            | Production_Single_Agent/CoffeeShopPathmindDemo.txt               | kitchen_cleanliness,customers_served,balked_customers,avg_response_time |
      | AutotestProject | tuple_models/SimpleSchedulingTuplesTest.zip | Production_Single_Agent/Production_Single_Agent_Reward.txt       | rail_var,train_var                                                      |
