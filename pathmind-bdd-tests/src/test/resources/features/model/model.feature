@model
Feature: Model page

  @smoke
  Scenario: Add experiment to exist model
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project page new experiment button
    Then Check that experiment page title is 'Experiment #2'
    When Click model breadcrumb btn
    Then Check that model/experiment name '2' exist in archived/not archived tab

  Scenario: Check model page experiment archive btn, move experiment to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment '1' archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment name '1' NOT exist in archived/not archived tab

  Scenario: Check model page experiment Unarchive btn
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment '1' archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    When Click experiment unarchive button
    When In confirmation dialog click in 'Unarchive' button
    When Check that model/experiment name '1' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab

  Scenario: Check model page elements
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check model page elements

  Scenario: Check model archived label
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click project page archive model btn
    When Click pop-up dialog id 'confirm'
    When Change models sidebar list to 'Archived'
    Then Check model title label tag is Archived

  Scenario: Check draft model archived label
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click project/ breadcrumb btn
    When Click project page archive model btn
    When Click pop-up dialog id 'confirm'
    When Change models sidebar list to 'Archived'
    Then Check that models sidebar model '2' contains draft tag 'true'

  Scenario: Check Model date created
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    When Click model breadcrumb btn
    Then Check side bar models 'Model #1' date is 'Created just now'
    Then Check model page model '1' created is 'just now'
    When Wait a bit 60000 ms
    When Refresh page
    Then Check side bar models 'Model #1' date is 'Created 1 minute ago'
    Then Check model page model '1' created is '1 minute ago'

  Scenario: Check model notes autosave
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Click model breadcrumb btn
    When Click upload model btn from project page
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    Then Check that wizard upload alp file page is opened
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    When Click model breadcrumb btn
    When Add model note Model #2 note to the project page
    When Click the model name 1
    When Add model note Model #1 note to the project page
    When Click the model name 2
    Then Check model note Model #2 note on the project page
    When Click the model name 1
    Then Check model note Model #1 note on the project page
