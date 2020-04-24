Feature: Experiments page
  @breadcrumb
  Scenario: Click projectS breadcrumb from draft experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Check that newExperiment page opened
    When Click projects breadcrumb btn
    Then Check that projects page opened
  @breadcrumb
  Scenario: Click project breadcrumb from draft experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Check that newExperiment page opened
    When Click project/ breadcrumb btn
    Then Check that project AutotestProject page is opened
  @breadcrumb
  Scenario: Click model breadcrumb from draft experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click model breadcrumb btn
    Then Check that models page opened

  Scenario: Click new experiment btn from running experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Click project start run button
    When Click in 'New Experiment' button
    Then Check that newExperiment page opened
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check running experiment page archive btn, move model to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    Then Click project start run button
    Then Click in 'Archive' button
    When In confirmation dialog click in 'Archive Experiment' button
    When Check that model NOT exist in archived tab
    When Open projects archived tab
    Then Check that model name 1 exist in archived tab
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Edit exist experiment
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Input reward function reward -= after[3] - before[3];
    When Click project save draft btn
    When Click back button
    Then Click the experiment name 1
    Then Check reward function is reward -= after[3] - before[3];

  Scenario: Check discovery run status
    Given Login to the pathmind
    When Create new CoffeeShop project
    Then Click project start run button
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check experiment model status is Starting Cluster
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  @reward-variables
  Scenario: Naming reward function variables
  	Given Login to the pathmind
  	When Create new CoffeeShop project with variable names: kitchen_cleanliness,customers_served,balked_customers,avg_response_time
  	Then Check variable kitchen_cleanliness marked 2 times in row 0 with index 0
  	Then Check variable customers_served marked 2 times in row 1 with index 1
  	Then Check variable balked_customers marked 2 times in row 2 with index 2
  	Then Check variable avg_response_time marked 2 times in row 3 with index 3
  	When Update variable 0 as newVariableName
  	When Click project save draft btn
  	Then Check variable newVariableName marked 2 times in row 0 with index 0

  @notes
  Scenario: Adding notes to the experiment
    Given Login to the pathmind
    When Create new CoffeeShop project
    Then Add note This is the experiment notes for this Coffee Shop project to the experiment page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check on the model page experiment 1 notes is This is the experiment notes for this Coffee Shop project
    Then Click the experiment name 1
    Then Check experiment notes is This is the experiment notes for this Coffee Shop project

  @notes
  Scenario: Adding notes to the started experiment
    Given Login to the pathmind
    When Create new CoffeeShop project
    Then Click project start run button
    And Check that button 'Stop Training' exists
    Then Add note This is the experiment notes for this Coffee Shop project to the experiment page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check on the model page experiment 1 notes is This is the experiment notes for this Coffee Shop project
    Then Click the experiment name 1
    Then Check experiment notes is This is the experiment notes for this Coffee Shop project
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Stop experiment
    Given Login to the pathmind
    When Create new CoffeeShop project
    Then Click project start run button
     And Check that button 'Stop Training' exists
     And Check that button 'Export Policy' doesn't exist
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Cancel' button
    Then Check that no confirmation dialog is shown
     And Check that the experiment status is different from 'Stopping'
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    Then Check that no confirmation dialog is shown
     And Check that the experiment status is 'Stopping'
     And Check that button 'Stop Training' doesn't exist
     And Check that button 'Export Policy' doesn't exist
