@model
Feature: Model page metrics

  Scenario Outline: Check model page metrics dropdown
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    When Open projects page
    When Open project <project name> on projects page
    When Click models page metrics dropdown
    Then Check model page metrics <variables>

    Examples:
      | project name    | model                                 | variables                                                                                                                                                           |
      | AutotestProject | MoonLanding/MoonLanding.zip           | fuelRemaining,distanceToX,distanceToY,distanceToZ,landed,crashed,gotAway,throttlingUp,thottlingDown,movingDown,movingUp,movingLeft,movingRight,speedX,speedY,speedZ |
      | AutotestProject | CoffeeShop/CoffeeShop.zip             | kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime                                                                                          |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | goalReached                                                                                                                                                         |
      | AutotestProject | ProductDelivery/ProductDelivery.zip   | avgWaitTime,avgDistanceKM                                                                                                                                           |
      | AutotestProject | Warehouse/Warehouse.zip               | profitableDeliveries,unprofitableDeliveries                                                                                                                         |

  Scenario: Check model page columns dropdown
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model SimpleStochastic/SimpleStochastic.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    When Click model breadcrumb btn
    Then Check model page columns multiselect 'Favorite, Status, Selected Observations, Reward Function, Id #, Notes, Created'
