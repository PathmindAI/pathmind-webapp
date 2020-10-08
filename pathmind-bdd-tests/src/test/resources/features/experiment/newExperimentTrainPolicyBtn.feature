@newExperiment
Feature: New experiment page train policy btn

  Scenario: Check train policy btn with one experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Check new experiment page train policy btn enabled 'false'
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'false'

  Scenario: Check train policy btn with few experiments
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Click in 'New Experiment' button
    When Check new experiment page train policy btn enabled 'false'
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'false'

  Scenario: Check train policy btn with few experiments and reward variables
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project save draft btn
    When Click in 'New Experiment' button
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'false'

  Scenario: Check train policy btn with started experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project start run button
    When Wait a bit 3000 ms
    When Click in 'New Experiment' button
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'false'
    When Click side bar experiment Experiment #1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check train policy btn with stopped experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project start run button
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Wait a bit 3000 ms
    When Click in 'New Experiment' button
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'true'
    When Clean new experiment reward function field
    When Check new experiment page train policy btn enabled 'false'
    When Click project save draft btn
    When Check new experiment page train policy btn enabled 'false'
    When Click side bar experiment Experiment #1