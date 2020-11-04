@brokenModels
Feature: Broken Models tests

#  Scenario: Upload broken model file
#    Given Login to the pathmind
#    When Open projects page
#    When Click create new project button
#    When Input name of the new project AutotestProject and click Create project button
#    When Upload model problematic_models/ABrokenModel.zip
#    Then Check that error message in model check panel is "The uploaded file is invalid, check it and upload again."

  Scenario: Upload broken model file 220Mb
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Generate big model with name 215mbFile
    When Upload model problematic_models/215mbFile
    Then Check that error message in model check panel is "The file is too big. Please contact support@pathmind.com."

#TO DO, need more problematic models
  Scenario Outline: Upload model file without the required info
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model <Model File>
    Then Wait for text "Checking your model" to disappear
    And Check that error message in model check panel is "<Error Message>"

    Examples:
      | Model File                                      | Error Message                                                                            |
#      | problematic_models/AModelWithNoActions.zip      | Number of actions found to be zero.                                                      |
#      | problematic_models/AModelWithNoObservations.zip | Number of observations found to be zero.                                                 |
      | problematic_models/ProblemModel#1480.zip        | Model needs to be updated. You can take a look at this article for upgrade instructions. |
      | problematic_models/NonTupleModel.zip            | Model needs to be updated. You can take a look at this article for upgrade instructions. |
