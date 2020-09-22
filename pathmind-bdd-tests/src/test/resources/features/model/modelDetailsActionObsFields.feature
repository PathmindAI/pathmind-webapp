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
    Then Check model page model details observations is <observations>

    Examples:
      | project name    | model                             | reward function file                                                        | observations |
      | AutotestProject | tuple_models/MoonLanding.zip      | tuple_models/MoonLandingRewardFunction.txt                                  | 9            |
      | AutotestProject | tuple_models/CoffeeShopTuple.zip  | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | 5            |
      | AutotestProject | tuple_models/SimpleStochastic.zip | tuple_models/SimpleStochasticRewardFunction.txt                             | 3            |
      | AutotestProject | tuple_models/ProductDelivery.zip  | tuple_models/ProductDeliveryRewardFunction.txt                              | 24           |
      | AutotestProject | tuple_models/Warehouse.zip        | tuple_models/WarehouseRewardFunction.txt                                    | 2            |
