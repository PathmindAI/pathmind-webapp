@notifications
Feature: Notifications

  Scenario: Check new version notification is shown
    Given Login to the pathmind
    When Trigger API new version notification
    Then Check that new version notification is shown

  Scenario: Check new version notification text
    Given Login to the pathmind
    When Trigger API new version notification
    Then Check that new version notification text is Pathmind has been updated. Please log in again to get the latest improvements.

  Scenario: Check that notification exist after surfing webapp
    Given Login to the pathmind
    When Trigger API new version notification
    When Check that new version notification is shown
    When Create new CoffeeShop project with single reward function
    When Check that new version notification is shown
    Then Click project start run button
    When Check that new version notification is shown
    When Open projects page
    Then Check that new version notification is shown
    When Open project AutotestProject on projects page
    Then Check that new version notification is shown
    Then Click the model name 1
    Then Check that new version notification is shown
    Then Click the experiment name 1
    Then Check that new version notification is shown
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    Then Check that new version notification is shown

  Scenario: Check that notification exist after page refresh
    Given Login to the pathmind
    When Trigger API new version notification
    When Check that new version notification is shown
    When Refresh page
    Then Check that new version notification is shown

  Scenario: Check that there are only one notification
    Given Login to the pathmind
    When Trigger API new version notification
    When Trigger API new version notification
    When Trigger API new version notification
    Then Check that new version notification is shown

  Scenario: Check new version notification sign out btn
    Given Login to the pathmind
    When Trigger API new version notification
    When Click in notification Sign out button
    Then Check that login page opened