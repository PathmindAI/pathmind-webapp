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

    Examples:
      | project name   |
      | ApiTestProject |
