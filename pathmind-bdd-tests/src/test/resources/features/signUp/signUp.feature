@signup
Feature: Sign Up

  Scenario: Check default page for first-time visitors
    Given Delete all cookies
    Given Open pathmind page
    When Check create new user page elements

  @tempEmail
  Scenario Outline: Create new user
    Given Open page sign-up
    When Fill new user form with name <First Name>, <Last Name>
    When Fill new user password '<Password>'
    When Fill new user confirmation password '<Password>'
    When Create new user click sign in button
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <Password>
    Then Check that user <First Name> <Last Name> successfully logged in
    Then Delete all cookies

    Examples:
      | First Name | Last Name | Password   |
      | Evgeniy    | Autotest  | Pass123456 |

  @tempEmail
  Scenario Outline: Create new user and login without email approve start project and login again
    Given Open page sign-up
    When Fill new user form with name <First Name>, <Last Name>
    When Fill new user password '<Password>'
    When Fill new user confirmation password '<Password>'
    When Create new user click sign in button
    When Open pathmind page
    Then Login with new user email and <Password>
    And Wait for sign-in page anti-flicker script
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Logout from pathmind
    Then Login with new user email and <Password>
    Then Check that Create new user error Email is not verified shown
    Then Check that Create new user Resend btn is shown

    Examples:
      | First Name | Last Name | Password   |
      | Evgeniy    | Autotest  | Pass123456 |

  @tempEmail
  Scenario: Create new user using email alias
    Given Open page sign-up
    When Fill new user form with first name Evgeniy
    When Fill new user form with last name Autotest
    When Fill temporary email with alias to the new user form
    When Fill new user password 'Pass123456'
    When Fill new user confirmation password 'Pass123456'
    When Create new user click sign in button
    When Open pathmind page
    Then Login with new user email and Pass123456
    And Wait for sign-in page anti-flicker script
    Then Check that Create new user error Email is not verified shown
    Then Click in 'Resend' button
    Then Check that Email verification was sent to your email. popup is shown
    Then Delete all cookies

  Scenario: Check What We Offer btn
    Given Open page sign-up
    When Click sign-up what we offer button
    When Open tab 1
    Then Check page url is https://pathmind.com/

  Scenario: Check About Us btn
    Given Open page sign-up
    When Click sign-up about us button
    When Open tab 1
    Then Check page url is https://pathmind.com/about/
