package io.skymind.pathmind.ui.binders;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.ui.converter.DoubleToIntegerConverter;
import io.skymind.pathmind.ui.converter.DoubleToLongConverter;

public class ModelBinders
{
	public static void bindNumberOfObservations(Binder<Model> binder, NumberField numberOfObservationsNumberField)
	{
		binder.forField(numberOfObservationsNumberField)
				.withConverter(new DoubleToIntegerConverter())
				.asRequired("Number of Observations is required")
				.withValidator(numberOfObservations ->
								numberOfObservations >= Model.MIN_NUMBER_OF_OBSERVATIONS && numberOfObservations <= Model.MAX_NUMBER_OF_OBSERVATIONS,
						"Must be between: " + Model.MIN_NUMBER_OF_OBSERVATIONS + " and " + Model.MAX_NUMBER_OF_OBSERVATIONS)
				.bind(Model::getNumberOfObservations, Model::setNumberOfObservations);
	}

	public static void bindNumberOfPossibleActions(Binder<Model> binder, NumberField numberOfPossibleActionsNumberField)
	{
		binder.forField(numberOfPossibleActionsNumberField)
				.withConverter(new DoubleToIntegerConverter())
				.asRequired("Number of Possible Actions is required")
				.withValidator(numberOfPossibleActions ->
								numberOfPossibleActions >= Model.MIN_NUMBER_OF_POSSIBLE_ACTIONS && numberOfPossibleActions <= Model.MAX_NUMBER_OF_POSSIBLE_ACTIONS,
						"Number of observations must be between: " + Model.MIN_NUMBER_OF_OBSERVATIONS + " and " + Model.MAX_NUMBER_OF_OBSERVATIONS)
				.bind(Model::getNumberOfPossibleActions, Model::setNumberOfPossibleActions);
	}

	public static void bindGetObservationForRewardFunction(Binder<Model> binder, TextArea getObservationForRewardFunctionTextArea)
	{
		binder.forField(getObservationForRewardFunctionTextArea)
				.asRequired("Field is required")
				.bind(Model::getGetObservationForRewardFunction, Model::setGetObservationForRewardFunction);
	}
}
