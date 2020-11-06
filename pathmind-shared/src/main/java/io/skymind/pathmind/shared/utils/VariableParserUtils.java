package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.constants.ObservationDataType;

import java.util.regex.Pattern;

import static io.skymind.pathmind.shared.constants.ObservationDataType.*;

public class VariableParserUtils {
    
    private VariableParserUtils()  {
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

        if (type.equals(INTEGER.toString())){
            return isArray ? INTEGER_ARRAY : INTEGER;
        } else if (type.equals(NUMBER.toString()) || type.equals("double")) {
            return isArray ? NUMBER_ARRAY : NUMBER;
        } else if (type.equals(BOOLEAN.toString())) {
            return BOOLEAN;
        } else {
            throw new IllegalStateException(String.format("Not supported observation type: %s for %s", type, name));
        }

    }

}
