@experiment
Feature: Experiment page alerts in multiple views

  @otherView
  Scenario: Check experiment side bar when an experiment is created and started in other tab
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project save draft btn
    When Duplicate current tab
    When Click project start run button
    When Check that confirmation dialog is shown false
    When Open tab 0
    When Check that confirmation dialog is shown true
    When In confirmation dialog click in 'Ok' button
    Then Check that 'Experiment #1' exist on the experiment page
    Then Check that 'Experiment #1' status icon is 'icon-loading-spinner'
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Open tab 1
    Then Check that 'Experiment #1' exist on the experiment page
    Then Check that 'Experiment #1' status icon is 'icon-stopped'

  @otherView
  Scenario: Check experiment side bar when an experiment is archived in other tab
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project save draft btn
    When Duplicate current tab
    When Click side nav archive button for 'Experiment #1'
    When In confirmation dialog click in 'Archive' button
    When Check that confirmation dialog is shown false
    When Open tab 0
    When Check that confirmation dialog is shown true
    When In confirmation dialog click in 'Ok' button
    Then Check that models page opened
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    When Open tab 1
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    When Click in 'Unarchive' button
    When In confirmation dialog click in 'Unarchive' button
    When Check that confirmation dialog is shown false
    When Open tab 0
    When Check that confirmation dialog is shown true
    When In confirmation dialog click in 'Ok' button
    Then Check that 'Experiment #1' exist on the experiment page

  @otherView
  Scenario: Check experiment side bar when an running experiment is archived in other tab
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project save draft btn
    When Click project start run button
    When Check that confirmation dialog is shown false
    When Duplicate current tab
    When Click side nav archive button for 'Experiment #1'
    When In confirmation dialog click in 'Archive' button
    When Check that confirmation dialog is shown false
    When Open tab 0
    When Check that confirmation dialog is shown true
    When In confirmation dialog click in 'Ok' button
    Then Check that models page opened
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    When Open tab 1
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    When Click in 'Unarchive' button
    When In confirmation dialog click in 'Unarchive' button
    When Check that confirmation dialog is shown false
    When Open tab 0
    When Check that confirmation dialog is shown true
    When In confirmation dialog click in 'Ok' button
    Then Check that 'Experiment #1' exist on the experiment page

  @otherView
  Scenario: Check experiment side bar when an experiment is created in other tab (few experiments)
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Duplicate current tab
    When Click in 'New Experiment' button
    When Open tab 0
    Then Check that 'Experiment #2' exist on the experiment page
    When Click in 'New Experiment' button
    Then Click project start run button
    When Open tab 1
    Then Check that 'Experiment #3' exist on the experiment page
    When Open tab 0
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click in 'Experiment #1' button
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

#  @otherView
#  Scenario: Check experiment side bar when an experiment is created and started in other tab (few experiments)
#    Given Login to the pathmind
#    When Create new CoffeeShop project with single reward function
#    When Click project save draft btn
#    When Duplicate current tab
#    When Click in 'New Experiment' button
#    When Click project start run button
#    When Open tab 0
#    Then Check that 'Experiment #2' exist on the experiment page
#    Then Check that 'Experiment #2' status icon is 'icon-loading-spinner'
#    When Click in 'Experiment #2' button
#    When Click in 'Stop Training' button
#    Then Check that the 'Stop Training' confirmation dialog is shown
#    When In confirmation dialog click in 'Stop Training' button

  @otherView
  Scenario: Check experiment side bar when an experiment is created and stopped in other tab (few experiments)
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project save draft btn
    When Duplicate current tab
    When Click in 'New Experiment' button
    When Click project start run button
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Open tab 0
    Then Check that 'Experiment #2' exist on the experiment page
    Then Check that 'Experiment #2' status icon is 'icon-stopped'

  @otherView
  Scenario: Check experiment side bar when an experiment is archived in other tab (few experiments)
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project save draft btn
    When Duplicate current tab
    When Click in 'New Experiment' button
    When Click side nav archive button for 'Experiment #2'
    When In confirmation dialog click in 'Archive' button
    When Open tab 0
    Then Check that 'Experiment #2' NOT exist on the experiment page
    When Click model breadcrumb btn
    When Open projects/model/experiment archived tab
    When Click experiment unarchive button
    When In confirmation dialog click in 'Unarchive' button
    When Open tab 1
    Then Check that 'Experiment #2' exist on the experiment page
