@account
Feature: Check Subscription Plans

  Scenario: Check Subscription Plans page
    Given Login to the pathmind
    When Open user account page
    When Click in 'Upgrade' button
    Then Check subscription plans page

  Scenario: Check Subscription Plans page breadcrumb
    Given Login to the pathmind
    When Open user account page
    When Click in 'Upgrade' button
    Then Check subscription plans page
    When Click account breadcrumb btn
    Then Check that user account page opened

  Scenario: Check Subscription Plans upgrade page breadcrumb
    Given Login to the pathmind
    When Open user account page
    When Click in 'Upgrade' button
    When Click in 'Choose Pro' button
    When Click account breadcrumb btn
    Then Check that user account page opened

  Scenario: Check Subscription Plans upgrade page elements
    Given Login to the pathmind
    When Open user account page
    When Click in 'Upgrade' button
    When Click in 'Choose Pro' button
    Then Check subscription plans upgrade page

  Scenario: Subscribe free plan to professional
    Given Open page sign-up
    When Fill new user form with name Evgeniy, Autotest
    When Fill new user password 'Pass123456'
    When Fill new user confirmation password 'Pass123456'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and Pass123456
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
