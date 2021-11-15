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

  Scenario: Check model upload sidenav
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Check that models sidebar model '1' contains draft tag 'false'
    When Click in 'Upload as Zip' button
    When Upload model MoonLanding/MoonLanding.zip
    When Check that model successfully uploaded
    When Click wizard upload ALP next btn
    When Click upload model btn from project page
    When Wizard model upload check folder upload page
    When Click project/ breadcrumb btn
    When Click upload model btn from project page
    When Click the model name 2
    When Click upload model btn from project page
    When Wizard model upload check folder upload page
