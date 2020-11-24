@newExperiment
Feature: New experiment page

  Scenario: Create new project and open project experiment
    Given Login to the pathmind
    When Open projects page
    When Create new CoffeeShop project with draft experiment
    Then Check side bar experiment 'Experiment #1' date is 'Created just now'
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the experiment name 1
    Then Check that new experiment AutotestProject page is opened

  Scenario: Click projectS breadcrumb from new experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the experiment name 1
    Then Check that new experiment AutotestProject page is opened
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Click project breadcrumb from new experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the experiment name 1
    Then Check that new experiment AutotestProject page is opened
    When Click project/ breadcrumb btn
    Then Check that project AutotestProject page is opened

  Scenario: Click model breadcrumb from new experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the experiment name 1
    When Click model breadcrumb btn
    Then Check that project page is opened

  Scenario: Edit exist experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the experiment name 1
    Then Input reward function reward -= after.balked_customers - before.balked_customers; // Minimize balked customers test3
    When Click project save draft btn
    When Click back button
    Then Click the experiment name 1
    Then Check reward function is reward -= after.balked_customers - before.balked_customers; // Minimize balked customers test3

  @notes
  Scenario: Adding notes to the experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    Then Add note This is the experiment notes for this Coffee Shop project to the experiment page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check on the model page experiment 1 notes is This is the experiment notes for this Coffee Shop project
    Then Click the experiment name 1
    Then Check experiment notes is This is the experiment notes for this Coffee Shop project

  @notes
  Scenario: Check that subtle checkmark shown after experiment note saved
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the experiment name 1
    Then Add note This is the experiment notes for this Coffee Shop project to the experiment page
    When Click in 'Model #1 (coffeeshop)' button
    Then Click the experiment name 1
    Then Check experiment notes is This is the experiment notes for this Coffee Shop project

  Scenario: Check new experiment opened in the new tab
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Click side bar new experiment btn
    When Click side bar new experiment btn
    When Click side bar new experiment btn
    When Open experiment 'Experiment #2' from sidebar in the new tab
    When Open tab 1
    When Check that experiment page title is 'Experiment #2'

  Scenario: Check new running experiment opened in the new tab
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click side bar new experiment btn
    When Open experiment 'Experiment #1' from sidebar in the new tab
    When Open tab 1
    When Check that experiment page title is 'Experiment #1'
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Create new project and open project experiment
    Given Login to the pathmind
    When Open projects page
    When Create new CoffeeShop project with single reward function
    When Wait a bit 60000 ms
    When Refresh page
    Then Check that new experiment AutotestProject page is opened
    Then Check side bar experiment 'Experiment #1' date is 'Created 1 minute ago'
