package io.skymind.pathmind.shared.utils;

import java.util.regex.Pattern;

import io.skymind.pathmind.shared.constants.ObservationDataType;

import static io.skymind.pathmind.shared.constants.ObservationDataType.BOOLEAN;
import static io.skymind.pathmind.shared.constants.ObservationDataType.BOOLEAN_ARRAY;
import static io.skymind.pathmind.shared.constants.ObservationDataType.FLOAT;
import static io.skymind.pathmind.shared.constants.ObservationDataType.FLOAT_ARRAY;
import static io.skymind.pathmind.shared.constants.ObservationDataType.INTEGER;
import static io.skymind.pathmind.shared.constants.ObservationDataType.INTEGER_ARRAY;
import static io.skymind.pathmind.shared.constants.ObservationDataType.LONG;
import static io.skymind.pathmind.shared.constants.ObservationDataType.LONG_ARRAY;
import static io.skymind.pathmind.shared.constants.ObservationDataType.NUMBER;
import static io.skymind.pathmind.shared.constants.ObservationDataType.NUMBER_ARRAY;

public class VariableParserUtils {

    private VariableParserUtils() {
        throw new IllegalAccessError("Static usage only");
    }

    public static boolean isArray(String name) {
        return Pattern.matches("\\w*\\[[0-9]*\\]", name);
    }

    public static String removeArrayIndexFromVariableName(String name) {
        return name.replaceAll("\\[[0-9]*\\]", "");
    }

    public static ObservationDataType observationType(String name, String type) {
        boolean isArray = isArray(name);

        if (type.equals(INTEGER.toString())) {
            return isArray ? INTEGER_ARRAY : INTEGER;
        } else if (type.equals(BOOLEAN.toString())) {
            return isArray ? BOOLEAN_ARRAY : BOOLEAN;
        } else if (type.equals(LONG.toString())) {
            return isArray ? LONG_ARRAY : LONG;
        } else if (type.equals(FLOAT.toString())) {
            return isArray ? FLOAT_ARRAY : FLOAT;
        } else if (type.equals(NUMBER.toString()) || type.equals("double")) {
            return isArray ? NUMBER_ARRAY : NUMBER;
        } else {
            throw new IllegalStateException(String.format("Not supported observation type: %s for %s", type, name));
        }

    }

}
