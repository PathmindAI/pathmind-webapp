@experiment
Feature: New Experiment page goals

  Scenario: Create experiment with goals
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard model details next btn
    When Input reward variable 'kitchen_cleanliness' goal '≥' value '1'
    When Input reward variable 'successful_customers' goal '≤' value '2'
    When Input reward variable 'balked_customers' goal '≥' value '3'
    When Input reward variable 'service_time' goal '≤' value '4'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchen_cleanliness' goal is '≥' and value '1'
    Then Check that new experiment reward variable 'successful_customers' goal is '≤' and value '2'
    Then Check that new experiment reward variable 'balked_customers' goal is '≥' and value '3'
    Then Check that new experiment reward variable 'service_time' goal is '≤' and value '4'

  Scenario: Create experiment with partial goals
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard model details next btn
    When Input reward variable 'kitchen_cleanliness' goal '≥' value '0'
    When Input reward variable 'balked_customers' goal '≥' value '1485'
    When Input reward variable 'service_time' goal '≤' value '158'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchen_cleanliness' goal is '≥' and value '0'
    Then Check that new experiment reward variable 'successful_customers' goal is '—' and value ''
    Then Check that new experiment reward variable 'balked_customers' goal is '≥' and value '1485'
    Then Check that new experiment reward variable 'service_time' goal is '≤' and value '158'
