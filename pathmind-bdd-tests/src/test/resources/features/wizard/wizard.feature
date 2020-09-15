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
    When Upload model tuple_models/CoffeeShopTuple.zip
    When Check that model successfully uploaded
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
    Then Check page url is https://help.pathmind.com/en/articles/4408884-how-to-update-your-model-to-the-latest-version

  Scenario: Check wizard Reward Variables not required
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model tuple_models/CoffeeShopTuple.zip
    When Check that model successfully uploaded
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check experiment page reward variables is kitchen_cleanliness,successful_customers,balked_customers,service_time

  Scenario: Check goals error msg
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Click wizard model details next btn
    When Input reward variable 'kitchen_cleanliness' goal '≥' value ' '
    When Input reward variable 'successful_customers' goal '≤' value ' '
    When Input reward variable 'balked_customers' goal '≥' value ' '
    When Input reward variable 'service_time' goal '≤' value ' '
    Then Check wizard reward variable 'kitchen_cleanliness' error is shown 'Enter a goal value'
    Then Check wizard reward variable 'successful_customers' error is shown 'Enter a goal value'
    Then Check wizard reward variable 'balked_customers' error is shown 'Enter a goal value'
    Then Check wizard reward variable 'service_time' error is shown 'Enter a goal value'
    When Click wizard reward variables next btn
    Then Check wizard reward variable 'kitchen_cleanliness' error is shown 'Enter a goal value'
    Then Check wizard reward variable 'successful_customers' error is shown 'Enter a goal value'
    Then Check wizard reward variable 'balked_customers' error is shown 'Enter a goal value'
    Then Check wizard reward variable 'service_time' error is shown 'Enter a goal value'
    When Input reward variable 'kitchen_cleanliness' goal '≤' value '1'
    When Input reward variable 'successful_customers' goal '≥' value '2'
    When Input reward variable 'balked_customers' goal '≤' value '3'
    When Input reward variable 'service_time' goal '≥' value '4'
    When Click wizard reward variables next btn
    Then Check that new experiment AutotestProject page is opened
    Then Check that new experiment reward variable 'kitchen_cleanliness' goal is '≤' and value '1.0'
    Then Check that new experiment reward variable 'successful_customers' goal is '≥' and value '2.0'
    Then Check that new experiment reward variable 'balked_customers' goal is '≤' and value '3.0'
    Then Check that new experiment reward variable 'service_time' goal is '≥' and value '4.0'
