@experiment @goals
Feature: New Experiment page goals

  Scenario: Create experiment with goals
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Input reward variable 'kitchenCleanlinessLevel' goal 'Minimize'
    When Input reward variable 'successfulCustomers' goal 'Minimize'
    When Input reward variable 'balkedCustomers' goal 'Maximize'
    When Input reward variable 'avgServiceTime' goal 'Maximize'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchenCleanlinessLevel' goal is 'Minimize'
    Then Check that new experiment reward variable 'successfulCustomers' goal is 'Minimize'
    Then Check that new experiment reward variable 'balkedCustomers' goal is 'Maximize'
    Then Check that new experiment reward variable 'avgServiceTime' goal is 'Maximize'

  Scenario: Create experiment with partial goals
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Input reward variable 'kitchenCleanlinessLevel' goal 'Maximize'
    When Input reward variable 'balkedCustomers' goal 'Minimize'
    When Input reward variable 'avgServiceTime' goal 'Minimize'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchenCleanlinessLevel' goal is 'Maximize'
    Then Check that new experiment reward variable 'successfulCustomers' goal is '—'
    Then Check that new experiment reward variable 'balkedCustomers' goal is 'Minimize'
    Then Check that new experiment reward variable 'avgServiceTime' goal is 'Minimize'
