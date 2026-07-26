package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File jsonFile = new File("recipes.json");

        Map<String, Object> root = objectMapper.readValue(jsonFile, Map.class);
        System.out.println(root.keySet());
        Object oil = root.get("basic-oil-processing");
        if (oil instanceof Map) {
            Map<String, String> map = (Map<String, String>) oil;
            System.out.println(map);
        }
    }
}