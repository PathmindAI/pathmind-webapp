package io.skymind.pathmind.ui.views.project.binders;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.converter.DoubleToLongConverter;

public class ProjectBinders
{
	public static void bindNumberOfObservations(Binder<Project> binder, NumberField numberOfObservationsNumberField)
	{
		binder.forField(numberOfObservationsNumberField)
				.withConverter(new DoubleToLongConverter())
				.asRequired("Number of Observations is required")
				.withValidator(numberOfObservations ->
								numberOfObservations >= Project.MIN_NUMBER_OF_OBSERVATIONS && numberOfObservations <= Project.MAX_NUMBER_OF_OBSERVATIONS,
						"Must be between: " + Project.MIN_NUMBER_OF_OBSERVATIONS + " and " + Project.MAX_NUMBER_OF_OBSERVATIONS)
				.bind(Project::getNumberOfObservations, Project::setNumberOfObservations);
	}

	public static void bindNumberOfPossibleActions(Binder<Project> binder, NumberField numberOfPossibleActionsNumberField)
	{
		binder.forField(numberOfPossibleActionsNumberField)
				.withConverter(new DoubleToLongConverter())
				.asRequired("Number of Possible Actions is required")
				.withValidator(numberOfPossibleActions ->
								numberOfPossibleActions >= Project.MIN_NUMBER_OF_POSSIBLE_ACTIONS && numberOfPossibleActions <= Project.MAX_NUMBER_OF_POSSIBLE_ACTIONS,
						"Number of observations must be between: " + Project.MIN_NUMBER_OF_OBSERVATIONS + " and " + Project.MAX_NUMBER_OF_OBSERVATIONS)
				.bind(Project::getNumberOfPossibleActions, Project::setNumberOfPossibleActions);
	}

	public static void bindGetObservationForRewardFunction(Binder<Project> binder, TextArea getObservationForRewardFunctionTextArea)
	{
		binder.forField(getObservationForRewardFunctionTextArea)
				.asRequired("Field is required")
				.bind(Project::getGetObservationForRewardFunction, Project::setGetObservationForRewardFunction);
	}

	public static void bindProjectName(Binder<Project> binder, TextField projectNameTextField)
	{
		binder.forField(projectNameTextField)
				.asRequired("Project must have a name")
				.bind(Project::getName, Project::setName);
	}
}
