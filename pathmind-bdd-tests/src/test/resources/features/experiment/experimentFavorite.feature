@experiment
Feature: Experiment favorite feature

  Scenario: Check favorite buttons on the experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click experiment page Experiment #1 star button
    Then Check experiment page side bar Experiment #1 is favorite true
    When Click in 'New Experiment' button
    Then Check experiment page side bar Experiment #1 is favorite true
    Then Check experiment page side bar Experiment #2 is favorite false
    When Refresh page
    Then Check experiment page side bar Experiment #1 is favorite true
    Then Check experiment page side bar Experiment #2 is favorite false
    When Click side bar experiment Experiment #1
    When Click project start run button
    When Refresh page
    Then Check experiment page side bar Experiment #1 is favorite true
    Then Check experiment page side bar Experiment #2 is favorite false
    When Click experiment page Experiment #1 star button
    When Click experiment page Experiment #2 star button
    When Refresh page
    Then Check experiment page side bar Experiment #2 is favorite true
    Then Check experiment page side bar Experiment #1 is favorite false
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Refresh page
    Then Check experiment page side bar Experiment #2 is favorite true
    Then Check experiment page side bar Experiment #1 is favorite false

  Scenario: Check favorite buttons on the dashboard page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Open dashboard page
    When Click dashboard page 'AutotestProject' 'Experiment #1' favorite button
    When Refresh page
    Then Check dashboard page 'AutotestProject' 'Experiment #1' is favorite true
    When Click dashboard page 'AutotestProject' 'Experiment #1' favorite button
    When Refresh page
    Then Check dashboard page 'AutotestProject' 'Experiment #1' is favorite false

  Scenario: Check favorite buttons on the model page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click in 'New Experiment' button
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    When Click model page experiment '1 ' star button
    When Refresh page
    Then Check model page experiment '1 ' is favorite true
    When Click model page experiment '1 ' star button
    When Click model page experiment '2 ' star button
    When Refresh page
    Then Check model page experiment '1 ' is favorite false
    Then Check model page experiment '2 ' is favorite true
    When Click model page experiment '1 ' archive/unarchive btn
    When In confirmation dialog click in 'Archive' button
    When Click model page experiment '2 ' archive/unarchive btn
    When In confirmation dialog click in 'Archive' button
    When Open projects/model/experiment archived tab
    Then Check model page experiment '1 ' is favorite false
    Then Check model page experiment '2 ' is favorite true
    When Click model page experiment '1 ' star button
    When Click model page experiment '2 ' star button
    When Click model page experiment '1 ' archive/unarchive btn
    When In confirmation dialog click in 'Unarchive' button
    When Click model page experiment '2 ' archive/unarchive btn
    When In confirmation dialog click in 'Unarchive' button
    When Open projects/model/experiment archived tab
    Then Check model page experiment '1 ' is favorite true
    Then Check model page experiment '2 ' is favorite false

  Scenario: Check favorite buttons on the model page run experiment and archive
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click experiment page Experiment #1 star button
    When Click side nav archive button for 'Experiment #1'
    When In confirmation dialog click in 'Archive' button
    When Click in 'Model #1 (coffeeshop_v1)' button
    When Open projects/model/experiment archived tab
    Then Check model page experiment '1 ' is favorite true
    When Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button