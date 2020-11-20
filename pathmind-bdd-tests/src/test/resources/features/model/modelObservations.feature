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
    Then Check observations list contains <observations>

    When Click new experiment page observation checkbox 'powerXYZ'
    When Click new experiment page observation checkbox 'distanceXYZ'
    Then Click project start run button
    Then Check experiment page observation 'powerXYZ' is selected 'false'
    Then Check experiment page observation 'moduleXYZ' is selected 'true'
    Then Check experiment page observation 'distanceXYZ' is selected 'false'

    Examples:
      | project name    | model                               | reward function file                              | observations |
      | AutotestProject | MoonLanding/MoonLanding.zip         | MoonLanding/MoonLandingRewardFunction.txt         |              |
      | AutotestProject | CoffeeShop/CoffeeShop.zip           | CoffeeShop/CoffeeShopRewardFunction.txt           |              |
      | AutotestProject | ProductDelivery/ProductDelivery.zip | ProductDelivery/ProductDeliveryRewardFunction.txt |              |
      | AutotestProject | Warehouse/Warehouse.zip             | Warehouse/WarehouseRewardFunction.txt             |              |
      | AutotestProject | SupplyChain/SupplyChain.zip         | SupplyChain/SupplyChainRewardFunction.txt         |retailerI,retailerDemandsSize,retailerExpected,retailerBacklog,wholesalerI,wholesalerOrdersSize,wholesalerExpected,wholesalerBacklog,factoryI,factoryOrdersSize,factoryBacklog,simTime              |
