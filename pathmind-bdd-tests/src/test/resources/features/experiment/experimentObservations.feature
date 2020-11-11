@experiment
Feature: Experiment observations

  Scenario Outline: Check MoonLanding.zip observations checkboxes status
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
    When Click new experiment page observation checkbox 'powerXYZ'
    When Click new experiment page observation checkbox 'distanceXYZ'
    Then Click project start run button
    Then Check experiment page observation 'powerXYZ' is selected 'false'
    Then Check experiment page observation 'moduleXYZ' is selected 'true'
    Then Check experiment page observation 'distanceXYZ' is selected 'false'

    Examples:
      | project name    | model                       | reward function file                      |
      | AutotestProject | MoonLanding/MoonLanding.zip | MoonLanding/MoonLandingRewardFunction.txt |

  Scenario Outline: Check CoffeeShop.zip observations checkboxes status
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
    When Click new experiment page observation checkbox 'kitchenCleanlinessLevel'
    When Click new experiment page observation checkbox 'timeOfDay'
    When Click new experiment page observation checkbox 'collectQueueSize'
    Then Click project start run button
    Then Check experiment page observation 'payBillQueueSize' is selected 'true'
    Then Check experiment page observation 'kitchenCleanlinessLevel' is selected 'false'
    Then Check experiment page observation 'timeOfDay' is selected 'false'
    Then Check experiment page observation 'orderQueueSize' is selected 'true'
    Then Check experiment page observation 'collectQueueSize' is selected 'false'

    Examples:
      | project name    | model                     | reward function file                    |
      | AutotestProject | CoffeeShop/CoffeeShop.zip | CoffeeShop/CoffeeShopRewardFunction.txt |

  Scenario Outline: Check ProductDelivery.zip observations checkboxes status
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
    When Click new experiment page observation checkbox 'orders'
    When Click new experiment page observation checkbox 'stocks'
    When Click new experiment page observation checkbox 'vehicles'
    Then Click project start run button
    Then Check experiment page observation 'freeVehicles' is selected 'true'
    Then Check experiment page observation 'orders' is selected 'false'
    Then Check experiment page observation 'stocks' is selected 'false'
    Then Check experiment page observation 'vehicles' is selected 'false'

    Examples:
      | project name    | model                               | reward function file                              |
      | AutotestProject | ProductDelivery/ProductDelivery.zip | ProductDelivery/ProductDeliveryRewardFunction.txt |

  Scenario Outline: Check Warehouse.zip observations checkboxes status
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
    When Click new experiment page observation checkbox 'delayTime1'
    Then Click project start run button
    Then Check experiment page observation 'delayTime1' is selected 'false'
    Then Check experiment page observation 'delayTime2' is selected 'true'

    Examples:
      | project name    | model                   | reward function file                  |
      | AutotestProject | Warehouse/Warehouse.zip | Warehouse/WarehouseRewardFunction.txt |

  Scenario Outline: Check SupplyChain.zip observations checkboxes status
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
    When Click new experiment page observation checkbox 'retailerI'
    When Click new experiment page observation checkbox 'retailerExpected'
    When Click new experiment page observation checkbox 'retailerBacklog'
    When Click new experiment page observation checkbox 'wholesalerOrdersSize'
    When Click new experiment page observation checkbox 'wholesalerBacklog'
    When Click new experiment page observation checkbox 'factoryI'
    When Click new experiment page observation checkbox 'factoryBacklog'
    When Click new experiment page observation checkbox 'simTime'
    Then Click project start run button
    Then Check experiment page observation 'retailerI' is selected 'false'
    Then Check experiment page observation 'retailerDemandsSize' is selected 'true'
    Then Check experiment page observation 'retailerExpected' is selected 'false'
    Then Check experiment page observation 'retailerBacklog' is selected 'false'
    Then Check experiment page observation 'wholesalerI' is selected 'true'
    Then Check experiment page observation 'wholesalerOrdersSize' is selected 'false'
    Then Check experiment page observation 'wholesalerExpected' is selected 'true'
    Then Check experiment page observation 'wholesalerBacklog' is selected 'false'
    Then Check experiment page observation 'factoryI' is selected 'false'
    Then Check experiment page observation 'factoryOrdersSize' is selected 'true'
    Then Check experiment page observation 'factoryBacklog' is selected 'false'
    Then Check experiment page observation 'simTime' is selected 'false'

    Examples:
      | project name    | model                       | reward function file                      |
      | AutotestProject | SupplyChain/SupplyChain.zip | SupplyChain/SupplyChainRewardFunction.txt |
