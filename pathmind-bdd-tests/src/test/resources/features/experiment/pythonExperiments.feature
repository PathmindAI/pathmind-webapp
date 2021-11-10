@experiment
Feature: Python experiments

  Scenario Outline: Check python experiments
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Wizard model upload click model type switch
    When Wizard model upload input env '<env>'
    When Upload model <model>
    When Click python upload model next btn
    When Check that new experiment AutotestProject page is opened
    When Check new experiment observations list contains 'mouse_row'
    When Check new experiment observations list contains 'mouse_col'
    When Check new experiment observations list contains 'mouse_row_dist'
    When Check new experiment observations list contains 'mouse_col_dist'

    Examples:
      | env                                                         | model                              |
      | examples.mouse.multi_mouse_env_pathmind.MultiMouseAndCheese | pythonExamplesWithObs/examples.zip |
      | examples.mouse.mouse_env_pathmind.MouseAndCheese            | pythonExamplesWithObs/examples.zip |
