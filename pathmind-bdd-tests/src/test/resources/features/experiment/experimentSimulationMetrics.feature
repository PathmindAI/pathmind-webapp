@experiment
@Ignored
Feature: Experiment page Simulation Metrics

  Scenario: Check reward variables on experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with variable names: kitchen_cleanliness,customers_served,balked_customers,avg_response_time
    Then Click project start run button
    Then Check experiment page reward variables is kitchen_cleanliness,customers_served,balked_customers,avg_response_time
