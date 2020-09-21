@experiment
Feature: New Experiment page goals

  Scenario: Create experiment with goals
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Input reward variable 'kitchenCleanlinessLevel' goal '≥' value '1'
    When Input reward variable 'successfulCustomers' goal '≤' value '2'
    When Input reward variable 'balkedCustomers' goal '≥' value '3.456'
    When Input reward variable 'avgServiceTime' goal '≤' value '4.99'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchenCleanlinessLevel' goal is '≥' and value '1.0'
    Then Check that new experiment reward variable 'successfulCustomers' goal is '≤' and value '2.0'
    Then Check that new experiment reward variable 'balkedCustomers' goal is '≥' and value '3.456'
    Then Check that new experiment reward variable 'avgServiceTime' goal is '≤' and value '4.99'

  Scenario: Create experiment with partial goals
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Input reward variable 'kitchenCleanlinessLevel' goal '≥' value '0'
    When Input reward variable 'balkedCustomers' goal '≥' value '1485.12'
    When Input reward variable 'avgServiceTime' goal '≤' value '158'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchenCleanlinessLevel' goal is '≥' and value '0.0'
    Then Check that new experiment reward variable 'successfulCustomers' goal is '—' and value ''
    Then Check that new experiment reward variable 'balkedCustomers' goal is '≥' and value '1485.12'
    Then Check that new experiment reward variable 'avgServiceTime' goal is '≤' and value '158.0'
