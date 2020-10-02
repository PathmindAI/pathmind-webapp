@e2e
Feature: E2E

  Scenario Outline: Create new project and run experiment
    Given Login to the pathmind
    When Open projects page
#    When Open page experiment/767
    
    #    ------------------------
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    When Upload ALP file '<alp file>'
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    When Wait a bit 5000 ms
    Then Click project save draft btn
    Then Click in 'New Experiment' button
    When Wait a bit 5000 ms
    When Click project start run button
    Then Check experiment status completed with <limit> minutes
    Then Check that 1 metrics are shown for reward variables
    Then Check that 1 sparklines are shown for reward variables
    Then Check Simulation Metrics columns titles
    Then Click simulation metrics value icon
    When Open tab 1
    Then Check page title tag text is Simulation Metrics | Pathmind Knowledge Base
    Then Check page url is https://help.pathmind.com/en/articles/4305404-simulation-metrics
    When Close browser tab
    When Open tab 0
    Then Click simulation metrics overview icon
    When Open tab 1
    Then Check page title tag text is Simulation Metrics | Pathmind Knowledge Base
    Then Check page url is https://help.pathmind.com/en/articles/4305404-simulation-metrics
    When Close browser tab
    When Open tab 0
#    ------------------------
    Then Check new experiment page model ALP btn simplestochasticmodel.alp
    When Click experiment page show sparkline btn for variable '<variable>'
    Then Check experiment page chart pop-up is shown for variable '<variable>'
    When Click pop-up dialog close btn
    Then Check that no confirmation dialog is shown
    When Refresh page
    When Click in 'Export Policy' button
    When Click in '< Back to Experiment #2' button
    When Check side bar experiments list Experiment #1,Experiment #2
    Then Check page title is Experiment #2

    Examples:
      | project name    | model                                 | reward function file                                | alp file                              | variable    | limit |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | SimpleStochastic/SimpleStochastic.alp | goalReached | 30    |
