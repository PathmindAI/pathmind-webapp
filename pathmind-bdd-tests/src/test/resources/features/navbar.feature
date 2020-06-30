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

  @Skip
  Scenario Outline: Check notes search
    Given Login to the pathmind
    When Create new CoffeeShop project with experiment note '<note>'
    When Input '<note>' to the notes search field
    When Click notes search btn
    When Check search result page notes contains '<note>'

    Examples:
      | note               |
      | AutotestNoteSearch |
      | AutotestProject    |

  @Skip
  Scenario: Check search field clear button
    Given Login to the pathmind
    When Input 'AutotestNoteSearch' to the notes search field
    When Click notes clear btn
    Then Check notes search field text is ''