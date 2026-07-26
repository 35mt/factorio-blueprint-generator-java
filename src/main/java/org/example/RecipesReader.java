package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class RecipesReader {
    private Map<String, Recipe> recipes;
    public RecipesReader(File jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        recipes = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Recipe>>() {});
    }

    public Map<String, Recipe> getRecipes() {
        return recipes;
    }
}
