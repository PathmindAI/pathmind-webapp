@modelDetails
Feature: Check Model Details package name field
#  TO DO Add new models
  Scenario Outline: Check Model Details package name field
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    Then Check that model successfully uploaded
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Check model page model breadcrumb package name is <package name>
    Then Input from file reward function <reward function file>
    When Click project save draft btn
    When Open dashboard page
    Then Check dashboard <project name> model breadcrumb <package name>
    When Open projects page
    When Open project <project name> on projects page
    Then Check project page model '1' package name is <package name>
    When Click the model name 1
    Then Check model page model details package name is <package name>
    Then Check model page model breadcrumb package name is <package name>

    Examples:
      | project name    | model                            | reward function file                                                        | package name |
#      | AutotestProject | tuple_models/CallCenterTuples.zip           | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt | interconnected_cc_tuple |
      | AutotestProject | tuple_models/CoffeeShopTuple.zip | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | coffeeshop   |
#      | AutotestProject | tuple_models/SimpleSchedulingTuplesTest.zip | Production_Single_Agent/Production_Single_Agent_Reward.txt       | simplescheduling_tuple  |
