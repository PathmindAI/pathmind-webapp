@wizard
Feature: Wizard page

  Scenario: Open create new project page and check elements
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    Then Check Create A New Project page

  Scenario: Project error if name already exist
    Given Login to the pathmind
    When Create new empty project
    When Open projects page
    When Click create new project button
    When Input already exist name of the project to the project name
    Then Check that error shown Project name should be unique

  Scenario Outline: Create new project with max length 99 and 100
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <text> and click Create project button
    Then Check that model upload page opened

    Examples:
      | characters | text                                                                                    |
      | 100        | Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget do |
      | 99         | Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget d  |

  Scenario: Check Reward Function default value
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
    Then Check Reward Function default value <>

  Scenario: Check update model article link on model upload step
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model problematic_models/NonTupleModel.zip
    Then Wait for text "Checking your model" to disappear
    When Click in 'this article' button
    When Open tab 1
    Then Check page url is https://help.pathmind.com/en/articles/3354371-1-install-pathmind-helper

  Scenario: Check wizard Reward Variables not required
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Check wizard warning label 'This draft model is archived.' is shown 'false'
    When Click wizard upload ALP next btn
    When Check wizard warning label 'This draft model is archived.' is shown 'false'
    When Click wizard model details next btn
    When Check wizard warning label 'This draft model is archived.' is shown 'false'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check experiment page reward variables is kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime

  @goals
  Scenario: Check goals on the experiment view
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Input reward variable 'kitchenCleanlinessLevel' goal 'minimize'
    When Input reward variable 'successfulCustomers' goal 'maximize'
    When Input reward variable 'balkedCustomers' goal 'maximize'
    When Input reward variable 'avgServiceTime' goal 'minimize'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchenCleanlinessLevel' goal is 'minimize'
    Then Check that new experiment reward variable 'successfulCustomers' goal is 'maximize'
    Then Check that new experiment reward variable 'balkedCustomers' goal is 'maximize'
    Then Check that new experiment reward variable 'avgServiceTime' goal is 'minimize'

  Scenario: Check upload model AnyLogic link
    Given Login to the pathmind
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Click in 'Export your model as a standalone Java application.' button
    When Open tab 1
    Then Check That model upload link 'Export your model as a standalone Java application' opened

  Scenario: Check wizard `This draft model is archived.` label
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Click model breadcrumb btn
    When Click upload model btn from project page
    When Upload model CoffeeShop/CoffeeShop.zip
    When Check that model successfully uploaded
    When Click project/ breadcrumb btn
    When Click archive/unarchive btn model '2' with package name 'coffeeshop' from left sidebar
    When Change models sidebar list to 'Archived'
    When Click the model name 2
    When Check wizard warning label 'This draft model is archived.' is shown 'true'
    When Click wizard upload ALP next btn
    When Check wizard warning label 'This draft model is archived.' is shown 'true'
    When Click wizard model details next btn
    When Check wizard warning label 'This draft model is archived.' is shown 'true'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened

  Scenario Outline: Check that upload model page is not accessed for different users
    Given Open page sign-up
    When Fill new user form with name <First Name>, <Last Name>
    When Fill new user password '<Password>'
    When Fill new user confirmation password '<Password>'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that user <First Name> <Last Name> successfully logged in
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Save experiment url into the variable 'modelUploadUrl'
    Then Delete all cookies
    When Login to the pathmind
    When Open url from the variable 'modelUploadUrl'
    When Check that Invalid data error page opened

    Examples:
      | First Name | Last Name | Password   |
      | Evgeniy    | Autotest  | Pass123456 |
