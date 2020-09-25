Feature: Experiment charts

  @e2e
  Scenario Outline: Check training charts
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model <model>
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment <project name> page is opened
    Then Input from file reward function <reward function file>
    Then Click project save draft btn
    When Click new experiment observation btn 'Select All'
    Then Click project start run button
    Then Check experiment status completed with 20 minutes
    When Click experiment page show sparkline btn for variable 'goalReached'
    When Click experiment page show sparkline btn for variable 'goalReached'
    Then Check experiment page chart pop-up is shown for variable 'goalReached'
    When Click pop-up dialog close btn
    Then Check that no confirmation dialog is shown

    Examples:
      | project name    | model                                 | reward function file                                |
      | AutotestProject | SimpleStochastic/SimpleStochastic.zip | SimpleStochastic/SimpleStochasticRewardFunction.txt |
