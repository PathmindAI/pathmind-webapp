@search
Feature: Nav bar model search

  Scenario: Check model name search
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Input '1' to the notes search field
    When Click notes search btn
    When Check search result page contains model name '1'

  Scenario: Check search result redirect to model page
    Given Login to the pathmind
    When Choose search option Model
    When Input 'Model 1' to the notes search field
    When Click notes search btn
    When Wait for search result page
    When Click in 'Model #1' button
    When Wait a bit 4000 ms
    Then Check that models page opened

  Scenario Outline: Check search result model name
    Given Login to the pathmind
    When Choose search option Model
    When Input '<search request>' to the notes search field
    When Click notes search btn
    Then Check Search Results for value is '<search request>'
    When Wait a bit 4000 ms
    Then Check search result tag is 'Model'
    When Refresh page
    Then Check search result group model is '<search result>'

    Examples:
      | search request | search result |
      | Model #1       | Model #1      |
      | model #1       | Model #1      |
      | Model 1        | Model #1      |
      | model 1        | Model #1      |
      | 1              | Model #1      |
      | #1             | Model #1      |

  Scenario: Open model in the new tab
    Given Login to the pathmind
    When Choose search option Model
    When Input 'Model #1' to the notes search field
    When Click notes search btn
    When Click in the new tab 'Model #1' button
    When Open tab 1
    Then Check that models page opened