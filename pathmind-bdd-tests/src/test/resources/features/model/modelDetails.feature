@modelDetails
Feature: Models details

  Scenario Outline: Check Model Details package name field
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details package name is <package name>

    Examples:
      | project name    | model                                                            | reward function file                                                        | package name |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt            | icebreaker   |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip               | Production_Single_Agent/CoffeeShopPathmindDemo.txt                          | coffeeshop   |
      | AutotestProject | Production_Single_Agent/FAST_RailModel_4Observation_3Actions.zip | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | railmodel    |

  Scenario Outline: Check Model Details Actions/Observations fields
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details actions is <actions>
    Then Check model page model details observations is <observations>

    Examples:
      | project name    | model                                                            | reward function file                                                        | actions | observations |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt            | 4       | 6            |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip               | Production_Single_Agent/CoffeeShopPathmindDemo.txt                          | 4       | 5            |
      | AutotestProject | Production_Single_Agent/FAST_RailModel_4Observation_3Actions.zip | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | 3       | 4            |

  Scenario Outline: Check Model Details Reward Variables fields
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Input reward variable names <variables>
    When Click wizard reward variables next btn
    Then Check that experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details reward variables order
    Then Check model page model details reward variables is <variables>

    Examples:
      | project name    | model                                                            | reward function file                                                        | variables                                                               |
      | AutotestProject | Production_Single_Agent/CheeseChasing_6Observations_4Actions.zip | Production_Single_Agent/CheeseChasing_6Observations_4Actions.txt            | cheese_var                                                              |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip               | Production_Single_Agent/CoffeeShopPathmindDemo.txt                          | kitchen_cleanliness,customers_served,balked_customers,avg_response_time |
      | AutotestProject | Production_Single_Agent/FAST_RailModel_4Observation_3Actions.zip | Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt | rail_var,train_var                                                      |

  Scenario Outline: Check sorting of reward variables on model view when change variables names
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Click wizard model details next btn
    When Input reward variable names <variables>
    When Click wizard reward variables next btn
    Then Check that experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Open projects page
    When Open project <project name> on projects page
    When Click the model name 1
    Then Check model page model details reward variables order
    When Click the experiment name 1
    When Change reward variable on experiment view 0 to test_variable
    When Click project save draft btn
    When Click model breadcrumb <project name> from dashboard
    Then Check model page model details reward variables order
    Then Check model page model details reward variable 0 name is test_variable
    When Click the experiment name 1
    When Change reward variable on experiment view 1 to SOME_NEW_VARIABLE
    When Click project save draft btn
    When Click model breadcrumb <project name> from dashboard
    Then Check model page model details reward variables order
    Then Check model page model details reward variable 1 name is SOME_NEW_VARIABLE
    When Click the experiment name 1
    When Change reward variable on experiment view 2 to Another One Variable 001
    When Click project save draft btn
    When Click model breadcrumb <project name> from dashboard
    Then Check model page model details reward variables order
    Then Check model page model details reward variable 2 name is Another One Variable 001
    When Click the experiment name 1
    When Change reward variable on experiment view 3 to last_long_long_long_long_variable_3
    When Click project save draft btn
    When Click model breadcrumb <project name> from dashboard
    Then Check model page model details reward variables order
    Then Check model page model details reward variable 3 name is last_long_long_long_long_variable_3

    Examples:
      | project name    | model                                              | reward function file                               | variables                                                               |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip | Production_Single_Agent/CoffeeShopPathmindDemo.txt | kitchen_cleanliness,customers_served,balked_customers,avg_response_time |
      | AutotestProject | Production_Single_Agent/CoffeeShopPathmindDemo.zip |                                                    | zero_variable,first_variable,second_variable,third_variable             |
