@newExperiment
Feature: New experiment page

  Scenario: Check reward variables exist when switch experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project page new experiment button
    Then Check that experiment page title is 'Experiment #2'
    Then Check experiment page reward variables is kitchen_cleanliness,successful_customers,balked_customers,service_time
    When Click side bar experiment Experiment #1
    Then Check experiment page reward variables is kitchen_cleanliness,successful_customers,balked_customers,service_time
