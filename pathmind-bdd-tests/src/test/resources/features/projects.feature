Feature: Projects page

  @breadcrumb
  Scenario: Check breadcrumb projects btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click projects breadcrumb btn
    Then Check that projects page opened

  Scenario: Create new project and check project exist on projects page
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Input model details "a user note"
    When Click wizard next step button
    Then Check that experiment page of the AutotestProject opened
    When Open projects page
    Then Check that project exist in project list AutotestProject

  Scenario Outline: Create new project and open project experiment
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Upload model Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip
    When Input model details "a user note"
    When Click wizard next step button
    Then Check that experiment page of the AutotestProject opened
    When Open projects page
    Then Click project name <project name>
    Then Click the model name 1
    Then Click the experiment name 1
    Then Check that experiment page of the AutotestProject opened

    Examples:
      | project name    |
      | AutotestProject |

  Scenario: Create new project and put it to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When Confirm archive/unarchive popup
    When Open archives tab
    Then Check that project exist in project list AutotestProject
    When Open projects tab
    Then Check that project not exist in project list AutotestProject
    When Open archives tab
    When Click AutotestProject project archive/unarchive button
    When Confirm archive/unarchive popup
    Then Check that project not exist in project list AutotestProject
    When Open projects tab
    Then Check that project exist in project list AutotestProject

  @notes
  Scenario: Adding notes to the project
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Add note AutotestNote to the project page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check project note is AutotestNote

  Scenario: Check model page archive btn, move model to archived
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click model 1 archive/unarchive button
    When Confirm archive/unarchive popup
    When Open projects archived tab
    Then Check that model name 1 exist in archived tab
    When Open projects archived tab
    When Check that model NOT exist in archived tab

  Scenario: Check model page Unarchive btn, move model to active tab
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click model 1 archive/unarchive button
    When Confirm archive/unarchive popup
    When Open archives tab
    When Click model 1 archive/unarchive button
    When Confirm archive/unarchive popup
    When Check that model NOT exist in archived tab
    When Open models tab
    Then Check that model name 1 exist in archived tab

  Scenario: Edit project name
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Open project AutotestProject on projects page
    When Click in 'Rename' button
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    Then Check that project name is AutotestEditName on project page
    When Open projects page
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page

  Scenario: Edit archived project name
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When Confirm archive/unarchive popup
    When Open archives tab
    When Open project AutotestProject on projects page
    When Click in 'Rename' button
    When Input project name AutotestEditName to the edit popup
    When Click in 'Rename Project' button
    Then Check that project name is AutotestEditName on project page
    When Open projects page
    When Open archives tab
    When Check that project exist in project list AutotestEditName
    When Open project AutotestEditName on projects page
    Then Check that project name is AutotestEditName on project page
