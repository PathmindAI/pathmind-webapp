Feature: Not found page

  Scenario: Check 404 page title and its error message
    Given Login to the pathmind
    When Open 404 page
    Then Check that 404 page opened