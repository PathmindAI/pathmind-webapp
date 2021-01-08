@model
Feature: Save Model draft

  @saveModelDraft
  Scenario: Check that Model is saved as draft as soon as it is uploaded
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model CoffeeShop/CoffeeShop.zip
    Then Check that model successfully uploaded
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check that there are 1 model(s) with 'Draft' tag in project page

  @saveModelDraft
  Scenario: Check that after uploading a model and clicking in next, the model is still a draft
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    Then Click wizard model details next btn
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check that there are 1 model(s) with 'Draft' tag in project page

  @saveModelDraft
  Scenario: Check Save Model Draft with reward variables
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check that there are 1 model(s) with 'Draft' tag in project page
    When Click the model name 2
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    And Check that there is a variable named kitchenCleanlinessLevel
    When Click wizard reward variables next btn
    When Click project/ breadcrumb btn
    Then Check that models sidebar model '1' contains draft tag 'false'
    Then Check that models sidebar model '2' contains draft tag 'false'
