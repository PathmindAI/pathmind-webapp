@experimentSimulationMetrics
Feature: Experiment page Simulation Metrics

  @smoke
  Scenario: Check reward variables on experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with 4 variables reward function
    When Click project start run button
    Then Check experiment page simulation metrics kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check that simulation metrics block is shown
    Given Login to the pathmind
    When Create new CoffeeShop project with 4 variables reward function
    When Click project start run button
    Then Check that simulation metrics block is shown
    Then Check running experiment page reward variables is kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  @smoke
  Scenario: Check that simulation metrics block is shown when switch to other experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with 4 variables reward function
    When Click project start run button
    Then Click in 'New Experiment' button
    When Wait a bit 4000 ms
    Then Click side bar experiment Experiment #1
    Then Check that simulation metrics block is shown
    Then Check running experiment page reward variables is kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check that simulation metrics block is shown for archived experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with 4 variables reward function
    When Click project start run button
    Then Click archive button for 'Experiment #1'
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    Then Check that simulation metrics block is shown
    Then Check running experiment page reward variables is kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check that simulation metrics are clickable
    Given Login to the pathmind
    When Create new CoffeeShop project with 4 variables reward function
    When Click project start run button
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot click reward variable 'successfulCustomers'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot click reward variable 'balkedCustomers'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot click reward variable 'avgServiceTime'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'true'
