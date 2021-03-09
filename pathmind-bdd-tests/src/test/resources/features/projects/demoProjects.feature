@projectsPage
Feature: Demo projects

  Scenario: Check demo projects list elements
    Given Login to the pathmind
    When Open projects page
    When Check demo list elements

  Scenario: Check demo projects list elements for new user
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
    When Open projects page
    When Check demo list elements

  Scenario Outline: Check demo projects create demo project
    Given Login to the pathmind
    When Open projects page
    When Click demo list '<demo model>'
    When Check that experiment page is opened
    Then Check experiment page reward variables is <reward vars>
    When Check experiment page observations list <new experiment observations>
    Then Click project start run button
    When Wait a bit 5000 ms
    When Check experiment page observations list <experiment observations>
    Then Check experiment page simulation metrics <reward vars>
    Then Check experiment page reward function <reward function>
    Then Check that unexpected error alert is Not shown

    Examples:
      | demo model                      | reward function                                | reward vars                                                                                                                                                                       | new experiment observations                                                    | experiment observations                                             |
      | Automated Guided Vehicles       | DemoProjects/AutomatedGuidedVehiclesAGV.txt    | totalThroughput,fullConveyor,machineUtil,deliveryStage,trips,tripDuration,aveTripDuration,emptyOrigins,fullQueue,deliveryToFailedMachine,essentialDelivery,invalid,agvUtilization | Select All,AGVs_self,sourceLines,pl_inventory,pl_process,pl_failureStatus,time | AGVs_self,sourceLines,pl_inventory,pl_process,pl_failureStatus,time |
      | Product Delivery                | DemoProjects/ProductDeliveryRewardFunction.txt | avgWaitTime,avgDistanceKM                                                                                                                                                         | Select All,stocks,freeVehicles,orders                                          | stocks,freeVehicles,orders                                          |
      | Autonomous Moon Landing         | DemoProjects/AutonomousMoonLanding.txt         | fuelRemaining,distanceToX,distanceToY,distanceToZ,landed,crashed,gotAway,throttlingUp,thottlingDown,movingDown,movingUp,movingLeft,movingRight,speedX,speedY,speedZ               | Select All,distanceXYZ,powerXYZ,moduleXYZ                                      | distanceXYZ,powerXYZ,moduleXYZ                                      |
