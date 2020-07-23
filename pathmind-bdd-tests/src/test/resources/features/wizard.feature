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

  Scenario: Check tuple article link on model upload step
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model problematic_models/NonTupleModel.zip
    Then Wait for text "Checking your model" to disappear
    When Click in 'this article' button
    When Open tab 1
    Then Check page title tag text is Converting models to support Tuples | Pathmind Knowledge Base
    Then Check page url is https://help.pathmind.com/en/articles/4219921-converting-models-to-support-tuples
    Then Check Converting models to support Tuples page elements