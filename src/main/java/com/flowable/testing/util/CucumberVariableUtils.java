package com.flowable.testing.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;

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


    public static Map<String, Object> getMapFromDataTable(DataTable variables) {
        Map<String, Object> variableMap = new HashMap<>();
        for (List<String> row : variables.asLists()) {
            Object value = CucumberVariableUtils.parseDataTableVariable(row.get(1), row.get(2));
            variableMap.put(row.get(0), value);
        }
        return variableMap;
    }



}
