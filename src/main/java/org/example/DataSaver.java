package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.buildings.WorkStation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataSaver {
    ObjectMapper objectMapper = new ObjectMapper();

    static void saveData(List<String> rawIngredients, Map<String, WorkStation> workStations) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("raw_ingredients.json"), rawIngredients);
        objectMapper.writeValue(new File("work_stations.json"), workStations);
    }

    static List<String> readRawIngredients() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("raw_ingredients.json"), new TypeReference<List<String>>() {});
    }

    static Map<String, WorkStation> readWorkStations() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("work_stations.json"), new TypeReference<Map<String, WorkStation>>() {});
    }
}
