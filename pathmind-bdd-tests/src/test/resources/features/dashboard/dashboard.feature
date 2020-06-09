@dashboard
Feature: Dashboard page

  @saveModelDraft
  Scenario Outline: Check that Model Drafts are shown correctly
    Given Login to the pathmind
    When Create a Model Draft
    When Open dashboard page
    Then Check AutotestProject stage Set up simulation is stage-active
    When <Click action>
    Then Check that resumeUpload page is opened
    And Check that we can add more info to the draft model

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

  Scenario: Check dashboard new project btn
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    When Click create new project button
    Then Check Create A New Project page
