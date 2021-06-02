@e2e
Feature: E2E

  Scenario Outline: Create new project and run experiment
    Given Login to the pathmind
    When Open projects page
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
    #    ------------------------
    #Check Simulation Metrics block
    Then Check that 1 metrics are shown for reward variables
    Then Check variable '<variable>' is chosen 'true'
    Then Check variable '<variable>' simulation metric value '1.0⠀±⠀0.0'
    Then Check that 1 sparklines are shown for reward variables
    Then Check Simulation Metrics columns titles
    Then Check that unexpected error alert is Not shown
    Then Click simulation metrics value icon
    When Open tab 1
    Then Check page title tag text is Simulation Metrics | Pathmind Knowledge Base
    Then Check page url is https://help.pathmind.com/en/articles/4305404-simulation-metrics
    When Close browser tab
    When Open tab 0
    #    ------------------------
    #Check observations block
    Then Check experiment page observations list stateChartState
    #    ------------------------
    #Check reward function block
    Then Check experiment page reward function <reward function file>
    #    ------------------------
    #Check ALP btn
    Then Check new experiment page model ALP btn simplestochasticmodel.alp
    #    ------------------------
    #Check Learning Progress block
    Then Check learning progress block title 'Learning Progress'
    Then Check learning progress block tabs 'Metrics,Histogram,Mean Reward Score'
    Then Check learning progress block selected tab 'true' name is 'Metrics'
    Then Check learning progress block selected tab 'false' name is 'Histogram'
    Then Check learning progress block selected tab 'false' name is 'Mean Reward Score'
    Then Check learning progress block metrics hint 'Select any two metrics on the simulation metric names above for comparison.'
    Then Check learning progress block metrics data-chart is shown
    When Click in 'Mean Reward Score' button
    Then Check learning progress block selected tab 'false' name is 'Metrics'
    Then Check learning progress block selected tab 'false' name is 'Histogram'
    Then Check learning progress block selected tab 'true' name is 'Mean Reward Score'
    Then Check learning progress block mean reward score data-chart is shown
    When Click in 'Metrics' button
    Then Check learning progress block selected tab 'true' name is 'Metrics'
    Then Check learning progress block selected tab 'false' name is 'Mean Reward Score'
    Then Check learning progress block selected tab 'false' name is 'Histogram'
    When Click in 'Histogram' button
    Then Check learning progress block selected tab 'true' name is 'Histogram'
    Then Check learning progress block selected tab 'false' name is 'Mean Reward Score'
    Then Check learning progress block selected tab 'false' name is 'Metrics'
    Then Check learning progress block histogram simulation metric is 'goalReached' and 'mean: 1.0⠀±⠀0.0'
    #    ------------------------
    #Check export policy
    When Click in 'Export Policy' button
    When Check side bar experiments list Experiment #1,Experiment #2
    Then Check page title is Experiment #2
    When Click model breadcrumb btn
    When Click models page metrics dropdown
    Then Check model page metrics <variable>
    When Model page choose metric '<variable>' from dropdown
    When Model page check experiment '1' column '<variable>' value is '1.0⠀±⠀0.0'
    Then Check that unexpected error alert is Not shown

    Examples:
      | project name    | model                                 | reward function file                                | alp file                              | variable    | limit |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt | SimpleStochastic/SimpleStochastic.alp | goalReached | 30    |
