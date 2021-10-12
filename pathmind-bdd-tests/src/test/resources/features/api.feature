@api
Feature: Api tests

  Scenario Outline: Create project and check that project exist /projects
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Open user account page
    Then Save account page api key to the environment variable
    Then Check that pathmind API return project with name <project name>
    Then Check API /projects '<project name>' archived 'false'

    Examples:
      | project name   |
      | ApiTestProject |

  Scenario Outline: Create project and check API is archived
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Open projects page
    When Click ApiTestProject project archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open user account page
    Then Save account page api key to the environment variable
    Then Check API /projects '<project name>' archived 'true'

    Examples:
      | project name   |
      | ApiTestProject |
@debug
  Scenario: Check create python model
    Given Login to the pathmind
    When Open user account page
    Then Save account page api key to the environment variable
    When Create project throw API 'python-examples/python_examples.zip', 'examples.mouse.single_agent_mouse_env.MouseAndCheese;null;null'
#    Then Check that pathmind API return project with name <project name>