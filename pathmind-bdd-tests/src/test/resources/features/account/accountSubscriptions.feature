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

  Scenario: Check Subscription Plans upgrade page cancel btn
    Given Login to the pathmind
    When Open user account page
    When Click in 'Upgrade' button
    When Click in 'Choose Pro' button
    When Click in 'Cancel' button
    Then Check subscription plans page

  Scenario: Check Subscription Plans upgrade page elements
    Given Login to the pathmind
    When Open user account page
    When Click in 'Upgrade' button
    When Click in 'Choose Pro' button
    Then Check subscription plans upgrade page
