package io.skymind.pathmind.shared.utils;

import java.util.regex.Pattern;

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

}
