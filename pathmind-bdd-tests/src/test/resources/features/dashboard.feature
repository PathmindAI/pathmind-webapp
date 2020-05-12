@dashboard
Feature: Dashboard page
  Scenario: Open project breadcrumb from dashboard
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    When Click project AutotestProject from dashboard
    Then Check that project AutotestProject page is opened

  Scenario: Open model breadcrumb from dashboard
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    When Click model breadcrumb AutotestProject from dashboard
    Then Check that models page opened

  Scenario: Open experiment from dashboard
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    When Click experiment breadcrumb AutotestProject from dashboard
    Then Check that newExperiment page opened

  Scenario: Check stage status for empty project
    Given Login to the pathmind
    When Create new empty project
    When Open dashboard page
    Then Check AutotestProject stage Set up simulation is stage-active
    Then Check AutotestProject stage Write reward function is stage-next
    Then Check AutotestProject stage Train policy is stage-next
    Then Check AutotestProject stage Export is stage-next

  Scenario: Check stage status for project with model
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    Then Check AutotestProject stage Set up simulation is stage-done
    Then Check AutotestProject stage Write reward function is stage-active
    Then Check AutotestProject stage Train policy is stage-next
    Then Check AutotestProject stage Export is stage-next

  Scenario: Check stage status for experiment run
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Click project start run button
    When Open dashboard page
    Then Check AutotestProject stage Set up simulation is stage-done
    Then Check AutotestProject stage Write reward function is stage-done
    Then Check AutotestProject stage Training... is stage-active
    Then Check AutotestProject stage Export is stage-next
    When Open projects page
    When Open project AutotestProject on projects page
    Then Click the model name 1
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check stage status for project export
    Given Login to the pathmind
    Then To do

  Scenario: Check experiment notes for dashboard item without experiment
    Given Login to the pathmind
    When Create new empty project
    When Open dashboard page
    Then Check that AutotestProject experiment notes does not exist

  Scenario: Check experiment notes for dashboard item with experiment that has empty notes
    Given Login to the pathmind
    When Create new CoffeeShop project
    When Open dashboard page
    Then Check dashboard AutotestProject experiment notes is —

  Scenario: Check experiment notes for dashboard item with experiment that has some notes
    Given Login to the pathmind
    When Create new CoffeeShop project with experiment notes
    When Open dashboard page
    Then Check dashboard AutotestProject experiment notes is This is the experiment notes for this Coffee Shop project fast speed model.

  Scenario: Check dashboard new project btn
    Given Login to the pathmind
    When Open dashboard page
    When Click create new project button
    Then Check Create A New Project page

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
