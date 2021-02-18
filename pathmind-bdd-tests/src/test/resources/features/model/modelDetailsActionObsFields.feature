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
    When Click project/ breadcrumb btn
    Then Check experiment '1' observations list contains <observations>

    Examples:
      | project name    | model                                 | reward function file                                | actions | observations                                                                       |
      | AutotestProject | MoonLanding/MoonLanding.zip           | MoonLanding/MoonLandingRewardFunction.txt           | 3       | powerXYZ,moduleXYZ,distanceXYZ |
      | AutotestProject | CoffeeShop/CoffeeShop.zip             | CoffeeShop/CoffeeShopRewardFunction.txt             | 4       | orderQueueSize,collectQueueSize,payBillQueueSize,kitchenCleanlinessLevel,timeOfDay |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | 2       | stateChartState                                                                                  |
      | AutotestProject | ProductDelivery/ProductDelivery.zip   | ProductDelivery/ProductDeliveryRewardFunction.txt   | 3       | stocks,vehicles,freeVehicles,orders                                                                                  |
      | AutotestProject | Warehouse/Warehouse.zip               | Warehouse/WarehouseRewardFunction.txt               | 2       | delayTime1,delayTime2                                                                                  |
