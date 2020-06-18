@dashboard
Feature: Dashboard page

  @saveModelDraft
  Scenario Outline: Check that Model Drafts are shown correctly
    Given Login to the pathmind
    When Create new CoffeeShop project with draft model
    When Open dashboard page
    Then Check AutotestProject stage Set up simulation is stage-active
    When <Click action>
    Then Check that model details page is opened

    Examples:
      | Click action                              |
      | Click in AutotestProject navigation icon  |
      | Click in AutotestProject stage breadcrumb |

  Scenario: Check dashboard begin screen elements
    Given Create new user Autotest, User with password Pass123456
    When Open pathmind page
    Then Login with new user email and Pass123456
    When Open dashboard page
    When Check dashboard begin screen elements
    When Click dashboard create your first project btn
    When Check that new project page opened
    When Open dashboard page
    When Click Getting Started Guide button
    Then Check that learn page https://help.pathmind.com/en/articles/4004788-getting-started opened

  Scenario: Check start page with the getting started message user see after login
    Given Open page early-access-sign-up
    When Fill new user form with name EditEmail, User
    When Create new user click sign up button
    When Fill new user password Abcd1234
    When Fill new user confirmation password Abcd1234
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and Abcd1234
    Then Check that dashboard page opened with the getting started message

  Scenario: Check dashboard new project btn
    Given Login to the pathmind
    When Create new empty project
    When Open dashboard page
    When Click create new project button
    Then Check Create A New Project page
