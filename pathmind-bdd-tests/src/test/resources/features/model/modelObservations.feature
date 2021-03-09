@experiment
Feature: Model observations

  Scenario Outline: Check model page observations list
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    Then Check that wizard upload alp file page is opened
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    Then Check new experiment observations list contains '<observations>'
    When Click model breadcrumb btn
    Then Check model page experiment name '1' selected observations is '<observations>'

    Examples:
      | project name    | model                               | reward function file                              | observations                                                                                                                                                                           |
      | AutotestProject | MoonLanding/MoonLanding.zip         | MoonLanding/MoonLandingRewardFunction.txt         | powerXYZ,moduleXYZ,distanceXYZ                                                                                                                                                         |
      | AutotestProject | CoffeeShop/CoffeeShop.zip           | CoffeeShop/CoffeeShopRewardFunction.txt           | orderQueueSize,collectQueueSize,payBillQueueSize,kitchenCleanlinessLevel,timeOfDay                                                                                                     |
      | AutotestProject | ProductDelivery/ProductDelivery.zip | ProductDelivery/ProductDeliveryRewardFunction.txt | stocks,vehicles,freeVehicles,orders                                                                                                                                                    |
      | AutotestProject | Warehouse/Warehouse.zip             | Warehouse/WarehouseRewardFunction.txt             | delayTime1,delayTime2                                                                                                                                                                  |
      | AutotestProject | SupplyChain/SupplyChain.zip         | SupplyChain/SupplyChainRewardFunction.txt         | retailerI,retailerDemandsSize,retailerExpected,retailerBacklog,wholesalerI,wholesalerOrdersSize,wholesalerExpected,wholesalerBacklog,factoryI,factoryOrdersSize,factoryBacklog,simTime |
