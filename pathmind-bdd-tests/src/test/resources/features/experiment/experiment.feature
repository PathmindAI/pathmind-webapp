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

  Scenario: Stop experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
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

  Scenario: Check reward function on started experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    Then Check experiment page reward function Production_Single_Agent/Production_Single_Agent_Reward.txt
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check experiment side bar when an experiment is created in other tab
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Duplicate current tab
    When Click in 'New Experiment' button
    When Open tab 0
    Then Check that Experiment #2 Draft exist on the experiment page
    When Click in 'New Experiment' button
    Then Click project start run button
    When Open tab 1
    Then Check that Experiment #3 Draft exist on the experiment page
    When Open tab 0
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click in 'Experiment #1' button
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
