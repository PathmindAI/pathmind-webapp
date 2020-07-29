@newExperiment
Feature: New experiment page

  Scenario: Create new project and open project experiment
    Given Login to the pathmind
    When Open projects page
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Check that new experiment AutotestProject page is opened

  Scenario: Click projectS breadcrumb from new experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Check that new experiment AutotestProject page is opened
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Click project breadcrumb from new experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Check that new experiment AutotestProject page is opened
    When Click project/ breadcrumb btn
    Then Check that project AutotestProject page is opened

  Scenario: Click model breadcrumb from new experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click model breadcrumb btn
    Then Check that models page opened

  Scenario: Edit exist experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Input reward function reward -= after[3] - before[3];
    When Click project save draft btn
    When Click back button
    Then Click the experiment name 1
    Then Check reward function is reward -= after[3 var-3] - before[3 var-3];

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
    When Create new CoffeeShop project with draft experiment
    Then Add note This is the experiment notes for this Coffee Shop project to the experiment page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check on the model page experiment 1 notes is This is the experiment notes for this Coffee Shop project
    Then Click the experiment name 1
    Then Check experiment notes is This is the experiment notes for this Coffee Shop project

  @notes
  Scenario: Check that subtle checkmark shown after experiment note saved
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Add note This is the experiment notes for this Coffee Shop project to the experiment page
    Then Click project save draft btn
    Then Refresh page
    Then Check experiment notes is This is the experiment notes for this Coffee Shop project

  Scenario: Check that reward variable with 99 chars saved
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model tuple_models/CoffeeShopTuple.zip
    When Check that model successfully uploaded
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Input reward variable names Lorem ipsum dolor sit amet consectetuer adipiscing elit Aenean commodo ligula eget dolor Aenean mass
    Then Click project save draft btn
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    Then Check experiment page reward variables is Lorem ipsum dolor sit amet consectetuer adipiscing elit Aenean commodo ligula eget dolor Aenean mass

  Scenario: Check confirmation popup is shown when reward variable is more than 100 characters
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model tuple_models/CoffeeShopTuple.zip
    When Check that model successfully uploaded
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Input reward variable names Lorem ipsum dolor sit amet consectetuer adipiscing elit Aenean commodo ligula eget dolor Aenean massa
    Then Check newExperiment page reward variable error is shown Variable name must not exceed 100 characters
    When Click in 'Projects' button
    Then Check that Before you leave.... pop-up is shown with error Your changes in the reward variables cannot be saved. Please check and fix the errors.
    Then In confirmation dialog click in 'Stay' button
    Then Check that new experiment AutotestProject page is opened
    When Click in 'Projects' button
    Then In confirmation dialog click in 'Leave' button
    Then Check that projects page opened

  Scenario: Check confirmation popup is shown when reward variable is empty
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model tuple_models/CoffeeShopTuple.zip
    When Check that model successfully uploaded
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    When Wait a bit 3500 ms
    Then Change reward variable on experiment view '0' to ''
    Then Check newExperiment page reward variable error is shown Variable name is required
    When Click in 'Projects' button
    Then Check that Before you leave.... pop-up is shown with error Your changes in the reward variables cannot be saved. Please check and fix the errors.
    Then In confirmation dialog click in 'Stay' button
    Then Check that new experiment AutotestProject page is opened
    When Click in 'Projects' button
    Then In confirmation dialog click in 'Leave' button
    Then Check that projects page opened
