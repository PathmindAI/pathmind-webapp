@modelDetails
Feature: Check Model Details package name field

  Scenario Outline: Check Model Details package name field
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    Then Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Check model page model breadcrumb package name is <package name>
    # When Open dashboard page
    # Then Check dashboard <project name> model breadcrumb <package name>
    When Open projects page
    When Open project <project name> on projects page
    Then Check project page model '1' package name is <package name>
    When Click the model name 1
    Then Check model page model title package name is <package name>
    Then Check model page model breadcrumb package name is <package name>

    Examples:
      | project name    | model                                 | reward function file                                | package name            |
      | AutotestProject | MoonLanding/MoonLanding.zip           | MoonLanding/MoonLandingRewardFunction.txt           | moonLanding             |
      | AutotestProject | CoffeeShop/CoffeeShop.zip             | CoffeeShop/CoffeeShopRewardFunction.txt             | coffeeshop              |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | simple_stochastic_model |
      | AutotestProject | ProductDelivery/ProductDelivery.zip   | ProductDelivery/ProductDeliveryRewardFunction.txt   | product_delivery        |
      | AutotestProject | Warehouse/Warehouse.zip               | Warehouse/WarehouseRewardFunction.txt               | warehouse_pathmind_demo |
