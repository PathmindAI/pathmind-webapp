Feature: E2E
  @disabled
  Scenario Outline: Create new project and run experiment
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When New project wizard click upload model button
    When Upload model <model>
    When Input model details <Number of Observations for Training>, <Number of Possible Actions>, <getObservation for Reward Function file>
    Then Check that project <project name> page is opened
    Then Input from file reward function <reward function file>
    Then Check that observation function displayed <getObservation for Reward Function file>
    Then Click project start discovery run button
    Then Click Okay in the "Starting the training..." popup
    Then Check experiment status completed

    Examples:
      | project name    | model                       | Number of Observations for Training | Number of Possible Actions | getObservation for Reward Function file    | reward function file               |
      #  https://help.pathmind.com/en/articles/3329544-getting-started
      | AutotestProject | CoffeeShopExportedModel.zip | 5                                   | 4                          | CoffeeShopExportedModelGetObservation.txt  | CoffeeShopExportedModelReward.txt  |