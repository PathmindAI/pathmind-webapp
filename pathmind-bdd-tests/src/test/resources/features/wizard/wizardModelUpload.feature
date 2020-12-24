@wizard
Feature: Wizard model upload page

  Scenario: Check wizard folder upload page
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Wizard model upload check folder upload page

  Scenario: Check wizard archive upload page
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Click in 'Upload as Zip' button
    When Wizard model upload check archive upload page
