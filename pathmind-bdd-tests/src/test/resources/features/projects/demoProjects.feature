@projectsPage
Feature: Demo projects

  Scenario: Check demo projects popup elements
    Given Login to the pathmind
    When Open projects page
    When Click in 'Example Projects' button
    When Check demo popup elements

  Scenario: Check demo projects close btn
    Given Login to the pathmind
    When Open projects page
    When Click in 'Example Projects' button
    When Check that demo projects popup is shown 'true'
    When Close demo projects pop-up
    When Check that demo projects popup is shown 'false'

  Scenario Outline: Check demo projects create demo project
    Given Login to the pathmind
    When Open projects page
    When Click in 'Example Projects' button
    When Click demo popup '<demo model>' get started btn
    When Check that experiment page is opened
    Then Check experiment page reward variables is <reward vars>

    Examples:
      | demo model                      | reward vars                                                                                                                                                                       |
      | Automated Guided Vehicles (AGV) | totalThroughput,fullConveyor,machineUtil,deliveryStage,trips,tripDuration,aveTripDuration,emptyOrigins,fullQueue,deliveryToFailedMachine,essentialDelivery,invalid,agvUtilization |
      | Product Delivery                | avgWaitTime,avgDistanceKM                                                                                                                                                         |
      | Autonomous Moon Landing         | fuelRemaining,distanceToX,distanceToY,distanceToZ,landed,crashed,gotAway,throttlingUp,thottlingDown,movingDown,movingUp,movingLeft,movingRight,speedX,speedY,speedZ               |
