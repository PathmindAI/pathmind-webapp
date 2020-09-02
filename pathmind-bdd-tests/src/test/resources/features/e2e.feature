@e2e
Feature: E2E

  Scenario Outline: Create new project and run experiment
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Wait a bit 5000 ms
    Then Click project save draft btn
    Then Click in 'New Experiment' button
    When Wait a bit 5000 ms
    When Click project start run button
    Then Check experiment status completed with <limit> hours
    When Refresh page
    When Click in 'Export Policy' button
    When Click in '< Back to Experiment #2' button
    When Check side bar experiments list Experiment #1,Experiment #2
    Then Check page title is Experiment #2

    Examples:
      | project name    | model                             | reward function file                            | limit |
      | AutotestProject | tuple_models/SimpleStochastic.zip | tuple_models/SimpleStochasticRewardFunction.txt | 1     |
