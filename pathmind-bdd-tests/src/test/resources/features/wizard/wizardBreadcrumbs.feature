@wizard
Feature: Wizard page breadcrumbs

  Scenario: Check wizard projects breadcrumb
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Check wizard project breadcrumb
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click project/ breadcrumb btn
    Then Check that project AutotestProject page is opened

  Scenario: Check wizard model upload breadcrumb is shown
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    Then Check wizard model upload breadcrumb is shown
    When Upload model tuple_models/CoffeeShopTuple.zip
    When Check that model successfully uploaded
    Then Check wizard model upload breadcrumb is shown
    When Click wizard model details next btn
    Then Check wizard model upload breadcrumb is shown