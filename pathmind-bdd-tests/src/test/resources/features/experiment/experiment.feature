@experiment
Feature: Experiment page

  Scenario: Click new experiment btn from running experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click in 'New Experiment' button
    Then Check that new experiment AutotestProject page is opened
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check experiment run status Starting Cluster
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    When Click project/ breadcrumb btn
    Then Check experiment '1' status is 'Starting Cluster'
    Then Click the experiment name 1
    Then Check that the experiment status is 'Starting Cluster'
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Adding notes to the started experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    And Check that button 'Stop Training' exists
    Then Add note This is the experiment notes for this Coffee Shop project to the experiment page
    When Open projects page
    When Open project AutotestProject on projects page
    Then Check on the model page experiment 1 notes is This is the experiment notes for this Coffee Shop project
    Then Click the experiment name 1
    Then Check experiment notes is This is the experiment notes for this Coffee Shop project
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check reward function on started experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    Then Check experiment page reward function new 'CoffeeShop/CoffeeShopRewardFunction.txt'
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check that experiment not shown in other projects
    Given Login to the pathmind
    When Create new CoffeeShop project with 4 variables reward function
    When Click project save draft btn
    Then Check side bar experiments list Experiment #1
    When Click in 'New Experiment' button
    When Wait a bit 3000 ms
    Then Check side bar experiments list Experiment #1,Experiment #2
    When Duplicate current tab
    When Open tab 1
    When Create new CoffeeShop project with 4 variables reward function
    Then Check side bar experiments list Experiment #1
    When Click in 'New Experiment' button
    When Wait a bit 3000 ms
    Then Check side bar experiments list Experiment #1,Experiment #2
    When Click in 'New Experiment' button
    When Wait a bit 3000 ms
    Then Check side bar experiments list Experiment #1,Experiment #2,Experiment #3
    When Open tab 0
    Then Check side bar experiments list Experiment #1,Experiment #2

  Scenario: Check notes autosave on experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Add note Experiment 1 Note to the experiment page
    When Click side bar new experiment btn
    When Add note Experiment 2 Note to the experiment page
    When Click side bar new experiment btn
    When Click side bar experiment Experiment #1
    Then Check experiment notes is Experiment 1 Note
    When Click side bar experiment Experiment #2
    Then Check experiment notes is Experiment 2 Note
    When Click side bar experiment Experiment #3
    When Click project start run button
    When Click side bar new experiment btn
    When Add note Experiment 4 Note to the experiment page
    When Click side bar experiment Experiment #3
    When Click side bar experiment Experiment #4
    Then Check experiment notes is Experiment 4 Note
    When Click side bar experiment Experiment #1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click side bar new experiment btn
    When Add note Experiment 5 Note to the experiment page
    When Click side bar experiment Experiment #1
    When Click side bar experiment Experiment #5
    Then Check experiment notes is Experiment 5 Note
    When Click side bar experiment Experiment #3
    When Add note Experiment 3 Note to the experiment page
    When Click side bar experiment Experiment #5
    Then Check experiment notes is Experiment 5 Note
    When Click side bar experiment Experiment #3
    Then Check experiment notes is Experiment 3 Note
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check commented reward function
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Input from file reward function CoffeeShop/CoffeeShopRewardFunctionCommentTest.txt
    Then Click project start run button
    Then Check reward variable is commented '//reward += after.successfulCustomers - before.successfulCustomers; // Maximize successful exits test2'

  Scenario: Check commented reward function start training btn
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Input from file reward function CoffeeShop/CoffeeShopRewardFunctionCommentTestDisabledBtn.txt
    Then Check experiment page start run btn is active 'false'

  Scenario: Check commented reward function start training btn with reward //
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Input reward function //
    Then Check experiment page start run btn is active 'false'
