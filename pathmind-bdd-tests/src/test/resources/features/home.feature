Feature: Nav bar buttons

  Scenario: Click dashboard btn and check that dashboard opened
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Open projects page
    When Open dashboard page
    Then Check that dashboard page opened

  Scenario: Click projects btn and check that projects page opened
    Given Login to the pathmind
    When Open projects page
    Then Check that projects page opened

  Scenario: Open home page and check Learn btn
    Given Login to the pathmind
    When Click learn btn
    Then Check that learn page https://help.pathmind.com/en/ opened
    Then Close browser tab

  Scenario: Click user account btn and check that account page opened
    Given Login to the pathmind
    When Open user account page
    Then Check that user account page opened