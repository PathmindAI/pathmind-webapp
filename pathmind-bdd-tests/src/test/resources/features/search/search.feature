@search
Feature: Nav bar search

  Scenario: Check notes search
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Input unique experiment page note
    When Click project save draft btn
    When Input unique note to the notes search field
    When Click notes search btn
    When Check search result page notes contains unique note
    When Click to the unique note on the search result page
    When Check that new experiment AutotestProject page is opened
    Then Check notes search field text is ''

  Scenario: Check project name search
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Input project name to the notes search field
    When Click notes search btn
    When Check search result page contains project name
    Then Click project name AutotestProject from search page
    Then Check that project AutotestProject page is opened

  Scenario: Check project name archived tag
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Open projects page
    When Click AutotestProject project archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Input project name to the notes search field
    When Click notes search btn
    Then Check search result page project name contains archived tag

  Scenario: Check search field clear button
    Given Login to the pathmind
    When Input 'AutotestNoteSearch' to the notes search field
    When Click notes clear btn
    Then Check notes search field text is ''

  Scenario: Check search field clear after loupe btn click
    Given Login to the pathmind
    When Input 'AutotestNoteSearch' to the notes search field
    When Click notes search btn
    Then Check notes search field text is ''

  Scenario: Check search result counter
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Open projects page
    When Generate unique number 'searchRandomNumber'
    When Click edit AutotestProject project icon from projects page
    When Rename project name to 1_SearchTest
    When Click in 'Rename Project' button
    When Create new CoffeeShop project with single reward function
    When Open projects page
    When Click edit AutotestProject project icon from projects page
    When Rename project name to 2_SearchTest
    When Click in 'Rename Project' button
    When Input 'SearchTest' to the notes search field with unique number
    When Click notes search btn
    Then Check that search counter is '2'

  Scenario: Check search field empty case
    Given Login to the pathmind
    When Click notes search btn
    Then Check that projects page opened

  Scenario: Check search field space case
    Given Login to the pathmind
    When Input ' ' to the notes search field
    When Click notes search btn
    Then Check that projects page opened

  Scenario: Check Search Results for value
    Given Login to the pathmind
    When Input 'AutotestNoteSearch' to the notes search field
    When Click notes search btn
    Then Check Search Results for value is 'AutotestNoteSearch'

  Scenario: Check Search Results for value is limited to 200 characters
    Given Login to the pathmind
    When Input 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec qua' to the notes search field
    When Click notes search btn
    Then Check Search Results for value is 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec qu'

  Scenario: Check search result redirect to project page
    Given Login to the pathmind
    When Input 'Model 1' to the notes search field
    # Input 'Model 1' to ensure the project has at least one non-draft models
    # otherwise if the project only has a draft model / no model,
    # the project page will redirect the user to the upload model page on the server side
    When Click notes search btn
    When Wait for search result page
    When Wait a bit 3000 ms
    When Click search result 'AutotestProject'
    When Wait a bit 3000 ms
    Then Check that project page is opened

  Scenario: Check search result project name
    Given Login to the pathmind
    When Choose search option Project
    When Input 'AutotestProject15' to the notes search field
    When Click notes search btn
    Then Check Search Results for value is 'AutotestProject15'
    When Wait a bit 4000 ms
    Then Check search result tag is 'Project'
    When Refresh page
    Then Check search result group project is 'AutotestProject15'

  Scenario: Open project in the new tab
    Given Login to the pathmind
    When Choose search option Project
    When Input 'AutotestProject' to the notes search field
    When Click notes search btn
    When Click in the new tab 'AutotestProject' button
    When Open tab 1
    Then Check that project page is opened

  Scenario: Check search by enter btn
    Given Login to the pathmind
    When Choose search option Project
    When Input 'AutotestProject16' to the notes search field
    When Click and send enter btn to the search field
    Then Check Search Results for value is 'AutotestProject16'

  Scenario: Check that correct page opened with value in the search field
    Given Login to the pathmind
    When Choose search option Project
    When Input 'AutotestProject16' to the notes search field
    When Click create new project button
    Then Check that new project page opened

