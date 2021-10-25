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
    When Click new experiment reward terms beta switch
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

  Scenario: Check notes autosave on new experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Add note Experiment 1 Note to the experiment page
    When Wait a bit 3000 ms
    When Click side bar new experiment btn
    When Wait a bit 3000 ms
    When Add note Experiment 2 Note to the experiment page
    When Wait a bit 3000 ms
    When Click side bar new experiment btn
    When Click side bar experiment Experiment #1
    Then Check experiment notes is Experiment 1 Note
    When Click side bar experiment Experiment #2
    Then Check experiment notes is Experiment 2 Note

  Scenario Outline: Check experiment autocomplete for commented line not shown
    Given Login to the pathmind
    When Open projects page
    When Create new CoffeeShop project with draft experiment
    When Check new experiment reward function '<reward function>' autocomplete is shown 'false'

    Examples:
      | reward function                         |
      | after.foo - before.foo; // minimize foo |
      | //convey                                |

  Scenario: Check experiment autocomplete is shown
    Given Login to the pathmind
    When Open projects page
    When Create new CoffeeShop project with draft experiment
    When Check new experiment reward function 'after.' autocomplete is shown 'true'

  Scenario Outline: Check highlight changed fields on New Experiment View
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
    When Click side bar new experiment btn
    When Change simulation parameter integer 'recurrence' to '5' on the new experiment page
    When Change simulation parameter integer 'powerZ' to '20' on the new experiment page
    When Change simulation parameter number 'maxVz' to '51' on the new experiment page
    When Change simulation parameter boolean 'display' to 'true' on the new experiment page
    When Change simulation parameter boolean 'tutorial' to 'true' on the new experiment page
    When Click new experiment observation btn 'powerXYZ'
    When Click new experiment observation btn 'distanceXYZ'
    Then Check simulation parameter 'recurrence' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'powerXY' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'powerZ' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'maxVxy' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'maxVz' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'display' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'onOffSwitch' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'tutorial' highlighted 'true' on the new experiment page
    Then Check observation 'powerXYZ' highlighted 'true' on the new experiment page
    Then Check observation 'moduleXYZ' highlighted 'false' on the new experiment page
    Then Check observation 'distanceXYZ' highlighted 'true' on the new experiment page
    When Change highlight difference from dropdown to 'Experiment #1' on the new experiment page
    Then Check simulation parameter 'recurrence' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'powerXY' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'powerZ' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'maxVxy' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'maxVz' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'display' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'onOffSwitch' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'tutorial' highlighted 'true' on the new experiment page
    Then Check observation 'powerXYZ' highlighted 'true' on the new experiment page
    Then Check observation 'moduleXYZ' highlighted 'false' on the new experiment page
    Then Check observation 'distanceXYZ' highlighted 'true' on the new experiment page
    Then Click project save draft btn
    When Click side bar new experiment btn
    When Change highlight difference from dropdown to 'Experiment #2' on the new experiment page
    Then Check simulation parameter 'recurrence' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'powerXY' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'powerZ' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'maxVxy' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'maxVz' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'display' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'onOffSwitch' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'tutorial' highlighted 'false' on the new experiment page
    Then Check observation 'powerXYZ' highlighted 'false' on the new experiment page
    Then Check observation 'moduleXYZ' highlighted 'false' on the new experiment page
    Then Check observation 'distanceXYZ' highlighted 'false' on the new experiment page
    When Change highlight difference from dropdown to 'Experiment #1' on the new experiment page
    Then Check simulation parameter 'recurrence' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'powerXY' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'powerZ' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'maxVxy' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'maxVz' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'display' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'onOffSwitch' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'tutorial' highlighted 'true' on the new experiment page
    Then Check observation 'powerXYZ' highlighted 'true' on the new experiment page
    Then Check observation 'moduleXYZ' highlighted 'false' on the new experiment page
    Then Check observation 'distanceXYZ' highlighted 'true' on the new experiment page
    When Change highlight difference from dropdown to 'Model Default' on the new experiment page
    Then Check simulation parameter 'recurrence' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'powerXY' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'powerZ' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'maxVxy' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'maxVz' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'display' highlighted 'true' on the new experiment page
    Then Check simulation parameter 'onOffSwitch' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'tutorial' highlighted 'true' on the new experiment page
    Then Check observation 'powerXYZ' highlighted 'true' on the new experiment page
    Then Check observation 'moduleXYZ' highlighted 'false' on the new experiment page
    Then Check observation 'distanceXYZ' highlighted 'true' on the new experiment page
    When Change highlight difference from dropdown to 'None' on the new experiment page
    Then Check simulation parameter 'recurrence' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'powerXY' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'powerZ' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'maxVxy' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'maxVz' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'display' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'onOffSwitch' highlighted 'false' on the new experiment page
    Then Check simulation parameter 'tutorial' highlighted 'false' on the new experiment page
    Then Check observation 'powerXYZ' highlighted 'false' on the new experiment page
    Then Check observation 'moduleXYZ' highlighted 'false' on the new experiment page
    Then Check observation 'distanceXYZ' highlighted 'false' on the new experiment page

    Examples:
      | project name    | model                       | reward function file                      |
      | AutotestProject | MoonLanding/MoonLanding.zip | MoonLanding/MoonLandingRewardFunction.txt |
