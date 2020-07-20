@experimentSimulationMetrics
Feature: Experiment page Simulation Metrics

  Scenario: Check reward variables on experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with variable names: kitchen_cleanliness,customers_served,balked_customers,avg_response_time
    When Click project start run button
    Then Check experiment page reward variables is kitchen_cleanliness,customers_served,balked_customers,avg_response_time

  Scenario: Check that simulation metrics block is shown
    Given Login to the pathmind
    When Create new CoffeeShop project with variable names: kitchen_cleanliness,customers_served,balked_customers,avg_response_time
    When Click project start run button
    Then Check that simulation metrics block is shown

  Scenario Outline: Check metrics shown for reward variables
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Input reward variable names <variables>
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Click project start run button
    Then Check that <simulation metrics count> metrics are shown for reward variables
    Then Check that <simulation metrics count> sparklines are shown for reward variables

    Examples:
      | project name    | model                                                            | reward function file                                             | variables                                                               | simulation metrics count |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt | cheese_var                                                              | 1                        |
      | AutotestProject | tuple_models/CoffeeShopTuple.zip                                 | Production_Single_Agent/CoffeeShopPathmindDemo.txt               | kitchen_cleanliness,customers_served,balked_customers,avg_response_time | 4                        |
      | AutotestProject | Production_Single_Agent/FAST_RailModel_4Observation_3Actions.zip | Production_Single_Agent/Two_Variables_Reward_Function.txt        | rail_var,train_var                                                      | 2                        |
