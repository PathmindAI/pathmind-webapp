Feature: Not found page

  Scenario Outline: Check 404 page title and its error message
    Given Login to the pathmind
    When Open page <page>
    Then Check that '404' page opened

    Examples:
      | page                |
      | incorrect-path-page |
      | error               |

  Scenario: Check Oops page title and its error message
    Given Login to the pathmind
    When Open page project/wrongUrl
    Then Check that Oops page opened

  Scenario: Check Invalid data error page title and its error message
    Given Login to the pathmind
    When Open page project/905461
    Then Check that Invalid data error page opened

  Scenario: Check that status page opened after click status.pathmind.com button
    Given Login to the pathmind
    When Open page incorrect-path-page
    When Click in 'status.pathmind.com' button
    Then Check that pathmind status page opened https://status.pathmind.com/
    Then Close browser tab
    When Open page project/wrongUrl
    When Click in 'status.pathmind.com' button
    Then Check that pathmind status page opened https://status.pathmind.com/
    Then Close browser tab
    When Open page project/905461
    When Click in 'status.pathmind.com' button
    Then Check that pathmind status page opened https://status.pathmind.com/
    Then Close browser tab
