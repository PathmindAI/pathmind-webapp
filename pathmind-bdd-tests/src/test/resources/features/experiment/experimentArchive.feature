@experiment
Feature: Experiment archive

  Scenario: Check running experiment page archive btn, move model to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    Then Click side nav archive button for current experiment
    When In confirmation dialog click in 'Archive' button
    When Check that model/experiment name '1' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check running experiment page archive btn, move second running experiment to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    Then Click in 'New Experiment' button
    Then Click project start run button
    Then Click side nav archive button for 'Experiment #1'
    When In confirmation dialog click in 'Archive' button
    When Click in 'Model #1 (coffeeshop_v1)' button
    When Check that model/experiment name '1' NOT exist in archived/not archived tab
    Then Check that model/experiment name '2' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab
    When Check that model/experiment name '2' NOT exist in archived/not archived tab
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click in 'Model #1 (coffeeshop_v1)' button
    Then Click the experiment name 2
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click in 'Model #1 (coffeeshop_v1)' button

  Scenario: Check running experiment page archive btn, move second draft experiment to archived
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    Then Click in 'New Experiment' button
    Then Click side nav archive button for 'Experiment #1'
    When In confirmation dialog click in 'Archive' button
    When Click in 'Model #1 (coffeeshop_v1)' button
    When Check that model/experiment name '1' NOT exist in archived/not archived tab
    Then Check that model/experiment name '2 Draft' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Check that model/experiment name '1' exist in archived/not archived tab
    When Check that model/experiment name '2 Draft' NOT exist in archived/not archived tab
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check unarchive archived running experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click project start run button
    Then Click in 'New Experiment' button
    Then Click side nav archive button for 'Experiment #1'
    When In confirmation dialog click in 'Archive' button
    When Click in 'Model #1 (coffeeshop_v1)' button
    When Open projects/model/experiment archived tab
    Then Click the experiment name 1
    When Click in 'Unarchive' button
    When In confirmation dialog click in 'Unarchive' button
    When Click in 'Model #1 (coffeeshop_v1)' button
    Then Check that model/experiment name '1' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment name '1' NOT exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    Then Click the experiment name 1
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button

  Scenario: Check unarchive archived experiment from experiment page
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    Then Click in 'New Experiment' button
    Then Click side nav archive button for 'Experiment #1'
    When In confirmation dialog click in 'Archive' button
    When Click in 'Model #1 (coffeeshop_v1)' button
    When Open projects/model/experiment archived tab
    Then Click the experiment name 1
    When Click in 'Unarchive' button
    When In confirmation dialog click in 'Unarchive' button
    When Click in 'Model #1 (coffeeshop_v1)' button
    Then Check that model/experiment name '1 Draft' exist in archived/not archived tab
    When Open projects/model/experiment archived tab
    When Check that model/experiment name '1 Draft' NOT exist in archived/not archived tab
