@account
Feature: Check Subscription Plans

  Scenario: Check Subscription Plans page
    Given Register and login with new user
    When Open user account page
    When Click in 'Upgrade' button
    Then Check subscription plans page

  Scenario: Check Subscription Plans page breadcrumb
    Given Register and login with new user
    When Open user account page
    When Click in 'Upgrade' button
    Then Check subscription plans page
    When Click account breadcrumb btn
    Then Check that user account page opened

  @disabled
  Scenario: Check Subscription Plans upgrade page breadcrumb
    Given Register and login with new user
    When Open user account page
    When Click in 'Upgrade' button
    When Click in 'Choose Pro' button
    Then Check subscription plans page

  @disabled
  Scenario: Subscribe free plan to professional
    Given Register and login with new user
    When Open user account page
    When Click in 'Upgrade' button
    When Click in 'Choose Pro' button
    When Fill payment form with stripe test card
    When Payment page click Upgrade btn
    When Check Upgraded to Professional page is shown
    When Click in 'Done' button
    When Check account subscription is Professional
    When Click in 'Cancel' button
    When Check cancel subscription pop-up
    When Click pop-up dialog 'Keep My Subscription' btn
    When Click in 'Cancel' button
    When Click pop-up dialog 'Yes, Cancel'
    When Check account subscription is Professional
    When Check account subscription hint
