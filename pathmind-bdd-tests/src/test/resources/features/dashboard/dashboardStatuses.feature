@dashboard
Feature: Dashboard statuses feature

  Scenario: Check stage status for empty project
    Given Login to the pathmind
    When Create new empty project
    When Open dashboard page
    Then Check AutotestProject stage Set up simulation is stage-active
    Then Check AutotestProject stage Write reward function is stage-next
    Then Check AutotestProject stage Train policy is stage-next
    Then Check AutotestProject stage Export is stage-next

  Scenario: Check stage status for project with model
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Open dashboard page
    Then Check AutotestProject stage Set up simulation is stage-done
    Then Check AutotestProject stage Write reward function is stage-active
    Then Check AutotestProject stage Train policy is stage-next
    Then Check AutotestProject stage Export is stage-next

  Scenario: Check stage status for experiment run
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Open dashboard page
    Then Check AutotestProject stage Set up simulation is stage-done
    Then Check AutotestProject stage Write reward function is stage-done
    Then Check AutotestProject stage Training... is stage-active
    Then Check AutotestProject stage Export is stage-next
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
