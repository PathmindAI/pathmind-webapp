@brokenModels
Feature: Broken Models tests

  Scenario: Upload broken model file(No helper)
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model problematic_models/ABrokenModel.zip
    Then Check that error message in model check panel is "You need to add PathmindHelper in your model."

  Scenario: Upload broken model file(Multiple Helpers)
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model problematic_models/5.simple_two_helpers.zip
    Then Check that error message in model check panel starts with "Only one PathmindHelper per model is currently supported."

  Scenario: Upload broken model file(Bonsai model)
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model problematic_models/9.simple_RLExperiment_exported_RLExperiment_bonsai.zip
    Then Check that error message in model check panel is "Invalid model. Please use the exported model for Pathmind."

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
      | Model File                               | Error Message                |
      | problematic_models/ProblemModel#1480.zip | Unable to analyze the model. |
      | problematic_models/NonTupleModel.zip     | Unable to analyze the model. |

  Scenario: Upload broken model few times
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project AutotestProject and click Create project button
    When Upload model problematic_models/NonTupleModel.zip
    Then Wait for text "Checking your model" to disappear
    And Check that error message in model check panel is "Unable to analyze the model."
    When Upload model problematic_models/ABrokenModel.zip
    Then Wait for text "Checking your model" to disappear
    And Check that error message in model check panel is "You need to add PathmindHelper in your model."
