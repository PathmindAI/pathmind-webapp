@experiment
Feature: Experiment page compare feature
@debug @inProgress
  Scenario: Click new experiment btn from running experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click in 'New Experiment' button
    When Click project start run button
    When Click side nav 'Compare' button from navbarItemMenu for 'Experiment #1'
    Then Experiment page Check 'primary' experiment-header 'Experiment #2', 'Starting Cluster', Stop Training btn shown 'true', Share with support btn shown 'true', Share with support label shown 'false'
    Then Experiment page Check 'secondary' experiment-header 'Experiment #1', 'Starting Cluster', Stop Training btn shown 'true', Share with support btn shown 'true', Share with support label shown 'false'
    Then Experiment page Check 'primary' middle panel
    Then Experiment page Check 'secondary' middle panel
    Then Experiment page Check 'primary' bottom panel
    Then Experiment page Check 'secondary' bottom panel
