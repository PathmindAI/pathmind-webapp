@experiment
Feature: Experiment page

  Scenario: Click new experiment btn from running experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click in 'New Experiment' button
    Then Check that new experiment AutotestProject page is opened
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check experiment run status Starting Cluster
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Check experiment status is Starting Cluster
    Then Click the experiment name 1
    Then Check that the experiment status is 'Starting Cluster'
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Adding notes to the started experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
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

#  Scenario: Stop experiment
#    Given Login to the pathmind
#    When Create new CoffeeShop project with single reward function
#    Then Click project start run button
#    And Check that button 'Stop Training' exists
#    And Check that button 'Export Policy' doesn't exist
#    When Click in 'Stop Training' button
#    Then Check that the 'Stop Training' confirmation dialog is shown
#    When In confirmation dialog click in 'Cancel' button
#    Then Check that no confirmation dialog is shown
#    And Check that the experiment status is different from 'Stopping'
#    When Click in 'Stop Training' button
#    Then Check that the 'Stop Training' confirmation dialog is shown
#    When In confirmation dialog click in 'Stop Training' button
#    Then Check that no confirmation dialog is shown
#    And Check that the experiment status is 'Stopping'
#    And Check that button 'Stop Training' doesn't exist
#    And Check that button 'Export Policy' doesn't exist
  @smoke
  Scenario: Check reward function on started experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    Then Check experiment page reward function CoffeeShop/CoffeeShopRewardFunctionOneFunction.txt
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check copy reward function button 1 line reward
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project save draft btn
    When Click project start run button
    When Click copy reward function btn and paste text to the notes field
    Then Check experiment notes is reward += after.kitchenCleanlinessLevel - before.kitchenCleanlinessLevel; // Maximize kitchen cleanliness test1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check copy reward function button 4 variables reward
    Given Login to the pathmind
    When Create new CoffeeShop project with 4 variables reward function
    When Click project save draft btn
    When Click project start run button
    When Click copy reward function btn and paste text to the notes field
    Then Check experiment notes is reward += after.kitchenCleanlinessLevel - before.kitchenCleanlinessLevel; // Maximize kitchen cleanliness test1/nreward += after.successfulCustomers - before.successfulCustomers; // Maximize successful exits test2/nreward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3/nreward -= after.avgServiceTime - before.avgServiceTime; // Minimize average service time test4
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check that experiment not shown in other projects
    Given Login to the pathmind
    When Create new CoffeeShop project with 4 variables reward function
    When Click project save draft btn
    Then Check side bar experiments list Experiment #1
    When Click in 'New Experiment' button
    When Wait a bit 3000 ms
    Then Check side bar experiments list Experiment #1,Experiment #2
    When Duplicate current tab
    When Open tab 1
    When Create new CoffeeShop project with 4 variables reward function
    Then Check side bar experiments list Experiment #1
    When Click in 'New Experiment' button
    When Wait a bit 3000 ms
    Then Check side bar experiments list Experiment #1,Experiment #2
    When Click in 'New Experiment' button
    When Wait a bit 3000 ms
    Then Check side bar experiments list Experiment #1,Experiment #2,Experiment #3
    When Open tab 0
    Then Check side bar experiments list Experiment #1,Experiment #2
