@experiment
Feature: Experiment page compare feature
@debug
  Scenario: Click new experiment btn from running experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click in 'New Experiment' button
    When Click project start run button
    When Click side nav 'Compare' button from navbarItemMenu for 'Experiment #1'
    When Experiment page check 'primary' experiment-header 'Experiment #2', 'Starting Cluster', Stop Training shown 'true', Share with support shown 'true'


#  When Wait a bit 30000 ms
