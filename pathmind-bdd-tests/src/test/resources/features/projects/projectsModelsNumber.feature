Feature: Projects page models number

  Scenario: Check models number for project empty project
    Given Login to the pathmind
    When Create new empty project
    When Open projects page
    When Projects page check project 'AutotestProject' models number '0'

  Scenario: Check models number for project with draft model
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Open projects page
    When Projects page check project 'AutotestProject' models number '1'

  Scenario: Check models number for project with draft experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Projects page check project 'AutotestProject' models number '1'
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model SimpleStochastic/SimpleStochastic.zip
    Then Check that model successfully uploaded
    When Open projects page
    When Projects page check project 'AutotestProject' models number '2'

  Scenario: Check models number for project with running experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Open projects page
    When Projects page check project 'AutotestProject' models number '1'
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model CoffeeShop/CoffeeShop.zip
    Then Check that model successfully uploaded
    When Open projects page
    When Projects page check project 'AutotestProject' models number '2'
    When Open project AutotestProject on projects page
    When Click the model name 2
    When Click wizard upload ALP next btn
    When Click wizard model details next btn
    When Click wizard reward variables next btn
    When Input reward function reward -= after.balkedCustomers - before.balkedCustomers; // Minimize balked customers test3
    When Click project save draft btn
    When Click project start run button
    When Click side bar new experiment btn
    When Open projects page
    When Projects page check project 'AutotestProject' models number '2'

  Scenario: Check models number for project with archived model
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Open projects page
    When Projects page check project 'AutotestProject' models number '1'
    When Open project AutotestProject on projects page
    When Click archive/unarchive btn model '1' with package name 'coffeeshop' from left sidebar
    When Open projects page
    When Projects page check project 'AutotestProject' models number '1'
    When Open project AutotestProject on projects page
    When Click upload model btn from project page
    When Upload model CoffeeShop/CoffeeShop.zip
    Then Check that model successfully uploaded
    When Click project/ breadcrumb btn
    When Change models sidebar list to 'Active'
    When Click archive/unarchive btn model '2' with package name 'coffeeshop' from left sidebar
    When Open projects page
    When Projects page check project 'AutotestProject' models number '2'
