Feature: Models page
  @breadcrumb
  Scenario: Click project breadcrumb from model page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project/ breadcrumb btn
    Then Check that project AutotestProject page is opened
  @breadcrumb
  Scenario: Click projectS breadcrumb from model page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Add second model to the exist project
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Input model details "a user note"
    When Click wizard next step button
    Then Check that experiment page of the AutotestProject opened
    When Open projects page
    When Open project AutotestProject on projects page
    Then Project page check that models count is 2

  Scenario: Add experiment to exist model
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click project page new experiment button
    When Click back button
    Then Check that model name 2 Draft exist in archived tab

  @notes
  Scenario: Adding notes to the model
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Add note AutotestNote to the project page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check project note is AutotestNote

  Scenario: Check draft experiment page archive btn, move model to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment archive button
    When Confirm archive/unarchive popup
    When Open projects archived tab
    Then Check that model name 1 Draft exist in archived tab
    When Open projects archived tab
    When Check that model NOT exist in archived tab

  Scenario: Check experiment page Unarchive btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment archive button
    When Confirm archive/unarchive popup
    When Open projects archived tab
    When Click experiment unarchive button
    When Confirm archive/unarchive popup
    When Check that model NOT exist in archived tab
    When Open projects archived tab
    Then Check that model name 1 Draft exist in archived tab

  Scenario: Check experiment page elements
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check experiments page elements

  @saveModelDraft
  Scenario: Check that Model is saved as draft as soon as it is uploaded
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    Then Check that button 'Save Draft' exists
    And Check that there are 1 project(s) with 'Draft' tag in AutotestProject project page

  @saveModelDraft
  Scenario: Check Save Model Draft with notes
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    Then Check that button 'Save Draft' exists
    When Fill Notes field as "a user note"
    When Click in 'Save Draft' button
    Then Check that the notification 'Draft successfully saved' is shown
    And Check that there are 1 project(s) with 'Draft' tag in AutotestProject project page
    When Click the first draft model
    Then Check that resumeUpload page is opened
    And Check that the Notes field has the value "a user note"
    And Check that we can add more info to the draft model
    And resume the upload
    And Check that there are 0 project(s) with 'Draft' tag in AutotestProject project page

  @saveModelDraft
  Scenario: Check that after uploading a model and clicking in next, the model is still a draft
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    Then Click wizard next step button
    Then Check that there are 1 project(s) with 'Draft' tag in AutotestProject project page

  @saveModelDraft
  Scenario: Check Save Model Draft with reward variables
    Given Skip until we have a better idea about how to deal with Features
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Click wizard next step button
    Then Check that button 'Save Draft' exists
    When Input reward variable "aVariableName"
    When Click in 'Save Draft' button
    Then Check that the notification 'Draft successfully saved' is shown
    And Check that there are 1 project(s) with 'Draft' tag in AutotestProject project page
    When Click the first draft model
    Then Check that resumeRewardVariablesNames page is opened
    And Check that there is a variable named "aVariableName"
    When Click wizard next step button
    Then Check that there are 0 project(s) with 'Draft' tag in AutotestProject project page