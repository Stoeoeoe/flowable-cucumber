package com.flowable.testing.util;

import java.util.Date;

public class CucumberVariableUtils {

    public static Object parseDataTableVariable(String value, String type) {
        switch (type) {
            case "string": return value;
            case "integer": return Integer.parseInt(value);
            case "double": return Double.parseDouble(value);
            case "float": return Float.parseFloat(value);
            case "date": return javax.xml.bind.DatatypeConverter.parseDateTime(value).getTime();
            default: return value;
        }
    }

}
