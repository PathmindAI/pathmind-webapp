@search
Feature: Nav bar experiment search

  Scenario: Check experiment name search
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Input '1' to the notes search field
    When Click notes search btn
    When Check search result page contains model name '1'

  Scenario: Check search result redirect to experiment page
    Given Login to the pathmind
    When Choose search option Experiment
    When Input 'Experiment 1' to the notes search field
    When Click notes search btn
    When Wait for search result page
    When Click in 'Experiment #1' button
    When Wait a bit 4000 ms
    Then Check that experiment page is opened

  Scenario Outline: Check search result experiment name
    Given Login to the pathmind
    When Choose search option Experiment
    When Input '<search request>' to the notes search field
    When Click notes search btn
    Then Check Search Results for value is '<search request>'
    When Wait a bit 4000 ms
    Then Check search result tag is 'Experiment'
    When Refresh page
    Then Check search result group experiment is '<search result>'

    Examples:
      | search request | search result |
      | Experiment #1  | Experiment #1 |
      | experiment #1  | Experiment #1 |
      | Experiment 1   | Experiment #1 |
      | experiment 1   | Experiment #1 |
      | 1              | Experiment #1 |
      | #1             | Experiment #1 |

  Scenario: Open experiment in the new tab
    Given Login to the pathmind
    When Choose search option Experiment
    When Input 'Experiment #1' to the notes search field
    When Click notes search btn
    When Click in the new tab 'Experiment #1' button
    When Open tab 1
    Then Check that experiment page title is 'Experiment #1'