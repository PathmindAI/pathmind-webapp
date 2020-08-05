@newExperiment
Feature: New experiment page

  Scenario: Check reward variables exist when switch experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with variable names: kitchen_cleanliness,customers_served,balked_customers,avg_response_time
    When Click project page new experiment button
    Then Check experiment page reward variables is kitchen_cleanliness,customers_served,balked_customers,avg_response_time
    When Click side bar experiment Experiment #1
    Then Check experiment page reward variables is kitchen_cleanliness,customers_served,balked_customers,avg_response_time
  
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
