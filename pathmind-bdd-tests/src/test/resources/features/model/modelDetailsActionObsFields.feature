@modelDetails
Feature: Check Model Details Actions/Observations fields

  Scenario Outline: Check Model Details Actions/Observations fields
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
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
      | project name    | model                                 | reward function file                                | actions | observations |
      | AutotestProject | MoonLanding/MoonLanding.zip           | MoonLanding/MoonLandingRewardFunction.txt           | 3       | 9            |
      | AutotestProject | CoffeeShop/CoffeeShop.zip             | CoffeeShop/CoffeeShopRewardFunction.txt             | 4       | 5            |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | 2       | 3            |
      | AutotestProject | ProductDelivery/ProductDelivery.zip   | ProductDelivery/ProductDeliveryRewardFunction.txt   | 3       | 24           |
      | AutotestProject | Warehouse/Warehouse.zip               | Warehouse/WarehouseRewardFunction.txt               | 2       | 2            |
