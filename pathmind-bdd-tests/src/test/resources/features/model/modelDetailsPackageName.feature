@modelDetails
Feature: Check Model Details package name field

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
      | project name    | model                             | reward function file                                                        | package name            |
      | AutotestProject | tuple_models/MoonLanding.zip      | tuple_models/MoonLandingRewardFunction.txt                                  | moonLanding             |
      | AutotestProject | tuple_models/CoffeeShopTuple.zip  | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | coffeeshop              |
      | AutotestProject | tuple_models/SimpleStochastic.zip | tuple_models/SimpleStochasticRewardFunction.txt                             | simple_stochastic_model |
      | AutotestProject | tuple_models/ProductDelivery.zip  | tuple_models/ProductDeliveryRewardFunction.txt                              | product_delivery        |
      | AutotestProject | tuple_models/Warehouse.zip        | tuple_models/WarehouseRewardFunction.txt                                    | warehouse_pathmind_demo |
