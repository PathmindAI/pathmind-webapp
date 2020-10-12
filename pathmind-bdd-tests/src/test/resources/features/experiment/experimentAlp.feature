Feature: Experiment ALP

  Scenario Outline: Check upload alp exist on newExperiment/experiment/model view
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    Then Check that wizard upload alp file page is opened
    When Upload ALP file '<ALP>'
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    Then Click project save draft btn
    Then Check new experiment page model ALP btn <alp file name>
    When Click model breadcrumb btn
    Then Check new experiment page model ALP btn <alp file name>
    When Click the experiment name 1
    Then Click project start run button
    Then Check new experiment page model ALP btn <alp file name>
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

    Examples:
      | project name    | model                                 | reward function file                                | ALP                                   | alp file name             |
      | AutotestProject | MoonLanding/MoonLanding.zip           | MoonLanding/MoonLandingRewardFunction.txt           | MoonLanding/MoonLanding.alp           | moonLanding.alp           |
      | AutotestProject | CoffeeShop/CoffeeShop.zip             | CoffeeShop/CoffeeShopRewardFunction.txt             | CoffeeShop/CoffeeShop.alp             | coffeeshop.alp            |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | SimpleStochastic/SimpleStochastic.alp | simplestochasticmodel.alp |
      | AutotestProject | ProductDelivery/ProductDelivery.zip   | ProductDelivery/ProductDeliveryRewardFunction.txt   | ProductDelivery/ProductDelivery.alp   | productdelivery.alp       |
      | AutotestProject | Warehouse/Warehouse.zip               | Warehouse/WarehouseRewardFunction.txt               | Warehouse/Warehouse.alp               | warehousepathminddemo.alp |
