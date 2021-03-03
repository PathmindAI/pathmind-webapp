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
    When Click project/ breadcrumb btn
    Then Check project page model '1' package name is <package name>
    Then Check model page model title package name is <package name>
    Then Check model page model breadcrumb package name is <package name>

    Examples:
      | project name    | model                                 | package name            |
      | AutotestProject | MoonLanding/MoonLanding.zip           | moonLanding             |
      | AutotestProject | CoffeeShop/CoffeeShop.zip             | coffeeshop              |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | simple_stochastic_model |
      | AutotestProject | ProductDelivery/ProductDelivery.zip   | product_delivery        |
      | AutotestProject | Warehouse/Warehouse.zip               | warehouse_pathmind_demo |
      | AutotestProject | Test3ALPModel.zip                     | testpathmindrunner      |
