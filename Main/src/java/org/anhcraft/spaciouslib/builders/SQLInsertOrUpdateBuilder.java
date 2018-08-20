package org.anhcraft.spaciouslib.builders;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

public class SQLInsertOrUpdateBuilder {
    private HashMap<String, String> data = new HashMap<>();
    private String table;

    public SQLInsertOrUpdateBuilder(String table){
        this.table = table;
    }

    public SQLInsertOrUpdateBuilder add(String name, String value){
        data.put(name, value);
        return this;
    }

    public SQLInsertOrUpdateBuilder add(String name, double value){
        data.put(name, Double.toString(value));
        return this;
    }

    public SQLInsertOrUpdateBuilder add(String name, float value){
        data.put(name, Float.toString(value));
        return this;
    }

    public SQLInsertOrUpdateBuilder add(String name, int value){
        data.put(name, Integer.toString(value));
        return this;
    }

    public SQLInsertOrUpdateBuilder add(String name, boolean value){
        data.put(name, Boolean.toString(value));
        return this;
    }

    public SQLInsertOrUpdateBuilder add(String name, Enum<?> value) {
        data.put(name, value.toString());
        return this;
    }

    public String build(){
        StringBuilder x = new StringBuilder("INSERT INTO `" + table + "`(" + String.join(",", data.keySet()) + ") VALUES(");
        int i = 0;
        for(String n : data.values()){
            x.append("\"").append(StringEscapeUtils.escapeJava(n)).append("\"");
            if(i < data.size() - 1){
                x.append(",");
            }
            i++;
        }
        x.append(") ON DUPLICATE KEY UPDATE ");
        i = 0;
        for(Map.Entry<String, String> entry : data.entrySet()){
            x.append(entry.getKey()).append("=\"").append(StringEscapeUtils.escapeJava(entry.getValue())).append("\"");
            if(i < data.size() - 1){
                x.append(", ");
            }
            i++;
        }
        return x.toString();
    }
}
