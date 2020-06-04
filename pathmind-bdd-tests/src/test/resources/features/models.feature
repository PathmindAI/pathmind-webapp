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
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment NOT exist in archived tab

  Scenario: Check experiment page Unarchive btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click experiment archive button
    When Confirm archive/unarchive popup
    When Open projects/model/experiment archived tab
    When Click experiment unarchive button
    When Confirm archive/unarchive popup
    When Check that model/experiment NOT exist in archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab

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
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Click wizard next step button
    Then Check that button 'Save Draft' exists
    When Input reward variable names kitchen_cleanliness
    When Click wizard reward variables save draft btn
    Then Check that the notification 'Draft successfully saved' is shown
    And Check that there are 1 project(s) with 'Draft' tag in AutotestProject project page
    When Click the first draft model
    When Click wizard model details next btn
    And Check that there is a variable named kitchen_cleanliness
    When Click wizard next step button
    Then Check that there are 0 project(s) with 'Draft' tag in AutotestProject project page

  @modelDetails
  Scenario Outline: Check Model Details package name field
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details package name is <package name>

    Examples:
      | project name    | model                                                            | reward function file                                                        | package name |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt            | icebreaker   |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip               | Production_Single_Agent/CoffeeShopPathmindDemo.txt                          | coffeeshop   |
      | AutotestProject | Production_Single_Agent/FAST_RailModel_4Observation_3Actions.zip | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | railmodel    |

  @modelDetails
  Scenario Outline: Check Model Details Actions/Observations fields
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details actions is <actions>
    Then Check model page model details observations is <observations>

    Examples:
      | project name    | model                                                            | reward function file                                                        | actions | observations |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt            | 4       | 6            |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip               | Production_Single_Agent/CoffeeShopPathmindDemo.txt                          | 4       | 5            |
      | AutotestProject | Production_Single_Agent/FAST_RailModel_4Observation_3Actions.zip | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | 3       | 4            |

  @modelDetails
  Scenario Outline: Check Model Details Reward Variables fields
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Input reward variable names <variables>
    When Click wizard reward variables next btn
    Then Check that experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details reward variables order
    Then Check model page model details reward variables is <variables>

    Examples:
      | project name    | model                                                            | reward function file                                                        | variables                                                               |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt            | cheese_var                                                              |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip               | Production_Single_Agent/CoffeeShopPathmindDemo.txt                          | kitchen_cleanliness,customers_served,balked_customers,avg_response_time |
      | AutotestProject | Production_Single_Agent/FAST_RailModel_4Observation_3Actions.zip | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | rail_var,train_var                                                      |

  Scenario: Check duplicated experiment names on model view
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    When Click archive btn from dashboard
    When In confirmation dialog click in 'Archive Experiment' button
    When Click stage write reward function AutotestProject from dashboard
    When Open projects page
    When Open project AutotestProject on projects page
    When Click the model name 1
    When Check that model/experiment name '2 Draft' exist in archived/not archived tab
    When Check that model/experiment name '1 Draft' NOT exist in archived/not archived tab
    When Open archives tab
    When Check that model/experiment name '1 Draft' exist in archived/not archived tab
    When Check that model/experiment name '2 Draft' NOT exist in archived/not archived tab
