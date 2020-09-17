@e2e
Feature: Experiment ALP

  Scenario Outline: Upload ALP with experiment
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    When Upload ALP file '<ALP>'
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    Then Click project save draft btn
    When Click in 'Model ALP' button
    Then Compare '<ALP>' file with downloaded file

    Examples:
      | project name    | model                                 | reward function file                                | ALP                                   |
      | AutotestProject | MoonLanding/MoonLanding.zip           | MoonLanding/MoonLandingRewardFunction.txt           | MoonLanding/MoonLanding.alp           |
      | AutotestProject | CoffeeShop/CoffeeShop.zip             | CoffeeShop/CoffeeShopRewardFunction.txt             | CoffeeShop/CoffeeShop.alp             |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | SimpleStochastic/SimpleStochastic.alp |
      | AutotestProject | ProductDelivery/ProductDelivery.zip   | ProductDelivery/ProductDeliveryRewardFunction.txt   | ProductDelivery/ProductDelivery.alp   |
      | AutotestProject | Warehouse/Warehouse.zip               | Warehouse/WarehouseRewardFunction.txt               | Warehouse/Warehouse.alp               |
