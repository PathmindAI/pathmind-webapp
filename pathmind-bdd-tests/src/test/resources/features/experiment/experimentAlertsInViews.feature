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
    When In confirmation dialog click in 'OK' button
    Then Check that 'Experiment #1' exist on the experiment page
    Then Check that 'Experiment #1' status icon is 'loading-spinner'
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
    When Click archive button for current draft experiment
    When In confirmation dialog click in 'Archive' button
    When Check that confirmation dialog is shown false
    When Open tab 0
    When Check that confirmation dialog is shown true
    When Check that the 'Experiment Archived' confirmation dialog is shown
    When Experiment page check number of the experiments is '0' in the left sidebar
    When In confirmation dialog click in 'OK' button
    Then Check that project AutotestProject page is opened
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    When Open tab 1
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    When Click in 'Unarchive' button
    When In confirmation dialog click in 'Unarchive' button
    When Check that confirmation dialog is shown false
    When Check that unexpected error alert is Not shown
    When Open tab 0
    When Check that unexpected error alert is Not shown
    When Check that confirmation dialog is shown true
    When Check that the 'Experiment Unarchived' confirmation dialog is shown
    When In confirmation dialog click in 'OK' button
    When Experiment page check number of the experiments is '1' in the left sidebar
    Then Check that 'Experiment #1' exist on the experiment page

  @otherView
  Scenario: Check experiment side bar when a running experiment is archived in other tab
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project save draft btn
    When Click project start run button
    When Check that confirmation dialog is shown false
    When Duplicate current tab
    When Click archive button for current experiment
    When In confirmation dialog click in 'Archive' button
    When Check that unexpected error alert is Not shown
    When Check that confirmation dialog is shown false
    When Open tab 0
    When Check that confirmation dialog is shown true
    When In confirmation dialog click in 'OK' button
    Then Check that project page is opened
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    When Open tab 1
    When Open projects/model/experiment archived tab
    When Click the experiment name 1
    When Click experiment page actions 'Unarchive' btn
    When In confirmation dialog click in 'Unarchive' button
    When Check that confirmation dialog is shown false
    When Open tab 0
    When Check that confirmation dialog is shown true
    When In confirmation dialog click in 'OK' button
    Then Check that 'Experiment #1' exist on the experiment page
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

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
    When Click side bar experiment Experiment #1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  @otherView
  Scenario: Check experiment side bar when an experiment is created and started in other tab (few experiments)
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project save draft btn
    When Duplicate current tab
    When Click in 'New Experiment' button
    When Click project start run button
    When Open tab 0
    Then Check that 'Experiment #2' exist on the experiment page
    Then Check that 'Experiment #2' status icon is 'loading-spinner'
    When Click side bar experiment Experiment #2
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    Then Check that 'Experiment #2' status icon is 'icon-stopped'

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
    When Wait for loading bar disappear
    When Click archive button for current draft experiment
    When In confirmation dialog click in 'Archive' button
    When Open tab 0
    Then Check that 'Experiment #2' NOT exist on the experiment page
    When Click model breadcrumb btn
    When Open projects/model/experiment archived tab
    When Click experiment unarchive button
    When In confirmation dialog click in 'Unarchive' button
    When Open tab 1
    Then Check that 'Experiment #2' exist on the experiment page
