@experiment @goals
Feature: New Experiment page goals

  Scenario: Create experiment with goals
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Input reward variable 'kitchenCleanlinessLevel' goal 'minimize'
    When Input reward variable 'successfulCustomers' goal 'minimize'
    When Input reward variable 'balkedCustomers' goal 'maximize'
    When Input reward variable 'avgServiceTime' goal 'maximize'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchenCleanlinessLevel' goal is 'minimize'
    Then Check that new experiment reward variable 'successfulCustomers' goal is 'minimize'
    Then Check that new experiment reward variable 'balkedCustomers' goal is 'maximize'
    Then Check that new experiment reward variable 'avgServiceTime' goal is 'maximize'

  Scenario: Create experiment with partial goals
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Input reward variable 'kitchenCleanlinessLevel' goal 'maximize'
    When Input reward variable 'balkedCustomers' goal 'minimize'
    When Input reward variable 'avgServiceTime' goal 'minimize'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchenCleanlinessLevel' goal is 'maximize'
    Then Check that new experiment reward variable 'successfulCustomers' goal is '—'
    Then Check that new experiment reward variable 'balkedCustomers' goal is 'minimize'
    Then Check that new experiment reward variable 'avgServiceTime' goal is 'minimize'
