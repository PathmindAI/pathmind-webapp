@api
Feature: Api model upload tests
@debug
  Scenario: Create project and check that project exist /projects
    Given Login to the pathmind
    When Open user account page
    Then Save account page api key to the environment variable
    When Api upload model 'SimpleStochastic/SimpleStochastic.zip' to the new project
    When Open projects page
    When Projects page click 'Created' column to 'desc' sorting
    When Projects page click first project from list
