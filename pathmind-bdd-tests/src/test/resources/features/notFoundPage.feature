Feature: Not found page

  Scenario Outline: Check 404 page title and its error message
    Given Login to the pathmind
    When Open page <page>
    Then Check that 404 page opened

    Examples:
    | page                    |
    | incorrect-path-page     |
    | error                   |