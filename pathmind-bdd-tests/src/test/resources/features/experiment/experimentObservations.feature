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
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click model breadcrumb btn
    Then Check observations list contains <observations>

    Examples:
      | project name    | model                       | reward function file                      | observations                   |
      | AutotestProject | MoonLanding/MoonLanding.zip | MoonLanding/MoonLandingRewardFunction.txt | powerXYZ,moduleXYZ,distanceXYZ |

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
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click model breadcrumb btn
    Then Check observations list contains <observations>

    Examples:
      | project name    | model                     | reward function file                    | observations                                                                       |
      | AutotestProject | CoffeeShop/CoffeeShop.zip | CoffeeShop/CoffeeShopRewardFunction.txt | orderQueueSize,collectQueueSize,payBillQueueSize,kitchenCleanlinessLevel,timeOfDay |

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
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click model breadcrumb btn
    Then Check observations list contains <observations>

    Examples:
      | project name    | model                               | reward function file                              | observations                        |
      | AutotestProject | ProductDelivery/ProductDelivery.zip | ProductDelivery/ProductDeliveryRewardFunction.txt | stocks,vehicles,freeVehicles,orders |

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
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click model breadcrumb btn
    Then Check observations list contains <observations>

    Examples:
      | project name    | model                   | reward function file                  | observations          |
      | AutotestProject | Warehouse/Warehouse.zip | Warehouse/WarehouseRewardFunction.txt | delayTime1,delayTime2 |

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
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click model breadcrumb btn
    Then Check observations list contains <observations>

    Examples:
      | project name    | model                       | reward function file                      | observations                                                                                                                                                                           |
      | AutotestProject | SupplyChain/SupplyChain.zip | SupplyChain/SupplyChainRewardFunction.txt | retailerI,retailerDemandsSize,retailerExpected,retailerBacklog,wholesalerI,wholesalerOrdersSize,wholesalerExpected,wholesalerBacklog,factoryI,factoryOrdersSize,factoryBacklog,simTime |

  Scenario Outline: Check SimpleStochastic.zip observations
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
    When Click model breadcrumb btn
    Then Check observations list contains <observations>

    Examples:
      | project name    | model                                 | reward function file                                | observations    |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | stateChartState |

  Scenario Outline: Check observations not overwritten when switch between experiments
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
    When Click project save draft btn
    When Click project page new experiment button
    When Wait a bit 3000 ms
    When Click project page new experiment button
    When Wait a bit 3000 ms
    When Click project page new experiment button
    When Wait a bit 3000 ms
    When Click new experiment page observation checkbox 'wholesalerOrdersSize'
    Then Check experiment page observation 'retailerI' is selected 'true'
    Then Check experiment page observation 'retailerDemandsSize' is selected 'true'
    Then Check experiment page observation 'retailerExpected' is selected 'true'
    Then Check experiment page observation 'retailerBacklog' is selected 'true'
    Then Check experiment page observation 'wholesalerI' is selected 'true'
    Then Check experiment page observation 'wholesalerOrdersSize' is selected 'false'
    Then Check experiment page observation 'wholesalerExpected' is selected 'true'
    Then Check experiment page observation 'wholesalerBacklog' is selected 'true'
    Then Check experiment page observation 'factoryI' is selected 'true'
    Then Check experiment page observation 'factoryOrdersSize' is selected 'true'
    Then Check experiment page observation 'factoryBacklog' is selected 'true'
    Then Check experiment page observation 'simTime' is selected 'true'
    When Wait a bit 3000 ms
    When Click side bar experiment Experiment #2
    When Wait a bit 3000 ms
    Then Check experiment page observation 'retailerI' is selected 'true'
    Then Check experiment page observation 'retailerDemandsSize' is selected 'true'
    Then Check experiment page observation 'retailerExpected' is selected 'true'
    Then Check experiment page observation 'retailerBacklog' is selected 'true'
    Then Check experiment page observation 'wholesalerI' is selected 'true'
    Then Check experiment page observation 'wholesalerOrdersSize' is selected 'true'
    Then Check experiment page observation 'wholesalerExpected' is selected 'true'
    Then Check experiment page observation 'wholesalerBacklog' is selected 'true'
    Then Check experiment page observation 'factoryI' is selected 'true'
    Then Check experiment page observation 'factoryOrdersSize' is selected 'true'
    Then Check experiment page observation 'factoryBacklog' is selected 'true'
    Then Check experiment page observation 'simTime' is selected 'true'
    When Click new experiment page observation checkbox 'retailerI'
    When Click new experiment page observation checkbox 'retailerDemandsSize'
    When Click new experiment page observation checkbox 'retailerExpected'
    When Click project page new experiment button
    When Wait a bit 3000 ms
    When Wait for loading bar disappear
    Then Check experiment page observation 'retailerI' is selected 'true'
    Then Check experiment page observation 'retailerDemandsSize' is selected 'true'
    Then Check experiment page observation 'retailerExpected' is selected 'true'
    Then Check experiment page observation 'retailerBacklog' is selected 'true'
    Then Check experiment page observation 'wholesalerI' is selected 'true'
    Then Check experiment page observation 'wholesalerOrdersSize' is selected 'false'
    Then Check experiment page observation 'wholesalerExpected' is selected 'true'
    Then Check experiment page observation 'wholesalerBacklog' is selected 'true'
    Then Check experiment page observation 'factoryI' is selected 'true'
    Then Check experiment page observation 'factoryOrdersSize' is selected 'true'
    Then Check experiment page observation 'factoryBacklog' is selected 'true'
    Then Check experiment page observation 'simTime' is selected 'true'

    Examples:
      | project name    | model                       | reward function file                      |
      | AutotestProject | SupplyChain/SupplyChain.zip | SupplyChain/SupplyChainRewardFunction.txt |

  Scenario Outline: Check observations not overwritten when switch from running experiment
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
    When Click new experiment page observation checkbox 'Select All'
    When Click new experiment page observation checkbox 'wholesalerOrdersSize'
    When Click project save draft btn
    When Click project start run button
    When Click project page new experiment button
    When Wait a bit 3000 ms
    When Wait for loading bar disappear
    Then Check experiment page observation 'retailerI' is selected 'false'
    Then Check experiment page observation 'retailerDemandsSize' is selected 'false'
    Then Check experiment page observation 'retailerExpected' is selected 'false'
    Then Check experiment page observation 'retailerBacklog' is selected 'false'
    Then Check experiment page observation 'wholesalerI' is selected 'false'
    Then Check experiment page observation 'wholesalerOrdersSize' is selected 'true'
    Then Check experiment page observation 'wholesalerExpected' is selected 'false'
    Then Check experiment page observation 'wholesalerBacklog' is selected 'false'
    Then Check experiment page observation 'factoryI' is selected 'false'
    Then Check experiment page observation 'factoryOrdersSize' is selected 'false'
    Then Check experiment page observation 'factoryBacklog' is selected 'false'
    Then Check experiment page observation 'simTime' is selected 'false'
    When Click side bar experiment Experiment #1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

    Examples:
      | project name    | model                       | reward function file                      |
      | AutotestProject | SupplyChain/SupplyChain.zip | SupplyChain/SupplyChainRewardFunction.txt |
