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
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details reward variables order
    Then Check model page model details reward variables is <variables>

    Examples:
      | project name    | model                                 | reward function file                                | variables                                                                                                                                                           |
      | AutotestProject | MoonLanding/MoonLanding.zip           | MoonLanding/MoonLandingRewardFunction.txt           | fuelRemaining,distanceToX,distanceToY,distanceToZ,landed,crashed,gotAway,throttlingUp,thottlingDown,movingDown,movingUp,movingLeft,movingRight,speedX,speedY,speedZ |
      | AutotestProject | CoffeeShop/CoffeeShop.zip             | CoffeeShop/CoffeeShopRewardFunction.txt             | kitchen_cleanliness,successful_customers,balked_customers,service_time                                                                                              |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | goalReached                                                                                                                                                         |
      | AutotestProject | ProductDelivery/ProductDelivery.zip   | ProductDelivery/ProductDeliveryRewardFunction.txt   | avgWaitTime,avgDistanceKM                                                                                                                                           |
      | AutotestProject | Warehouse/Warehouse.zip               | Warehouse/WarehouseRewardFunction.txt               | profitableDeliveries,unprofitableDeliveries                                                                                                                         |
