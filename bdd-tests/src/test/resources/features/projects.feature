Feature: Projects page

  Scenario Outline: Create new project and check search btn
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When New project wizard click upload model button
    When Upload model CoffeeShopExportedModel.zip
    When Input model details 5, 4, CoffeeShopExportedModelGetObservation.txt
    When Check that project <project name> page is opened
    When Open projects page
    Then Input project name to the search field <project name>
    Then Check that project exist in project list <project name>

    Examples:
      | project name    |
      | AutotestProject |

  Scenario: Check search field clear button
    Given Login to the pathmind
    When Open projects page
    When Input to the projects search field value ClearFieldTest
    When Click search field clear button
    Then Check that search field is empty

  Scenario Outline: Create new project and open project experiment
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When New project wizard click upload model button
    When Upload model CoffeeShopExportedModel.zip
    When Input model details 5, 4, CoffeeShopExportedModelGetObservation.txt
    When Check that project <project name> page is opened
    When Open projects page
    Then Click project name <project name>
    Then Click the model name 1
    Then Click the experiment name 1
    Then Check that project <project name> page is opened

    Examples:
      | project name    |
      | AutotestProject |

  Scenario: Create new project and put it to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Input project name to the search field AutotestProject
    When Click project archive button
    When Confirm archive popup
    When Open projects archived tab
    Then Check that project exist in project list AutotestProject
    When Open projects archived tab
    Then Check that project not exist in project list AutotestProject
    When Open projects archived tab
    When Input project name to the search field AutotestProject
    When Click project archive button
    When Confirm archive popup
    Then Check that project not exist in project list AutotestProject
    When Open projects archived tab
    Then Check that project exist in project list AutotestProject