package org.example;

import org.example.buildings.Size;
import org.example.buildings.WorkStation;
import org.example.encode.Encoder;
import org.example.encode.Entity;
import org.example.read.Recipe;
import org.example.read.RecipesReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    // Пример использования
    public static void main(String[] args) throws IOException {
        RecipesReader recipesReader = new RecipesReader(new File("recipes.json"));

        // Рабочие станции
        Map<String, WorkStation> workStationMap = new HashMap<>();
        workStationMap.put(null, new WorkStation(0.75, new Size(3, 3), "assembling-machine-2"));
        workStationMap.put("crafting", new WorkStation(0.75, new Size(3, 3), "assembling-machine-2"));
        workStationMap.put("advanced-crafting", new WorkStation(0.75, new Size(3, 3), "assembling-machine-2"));
        workStationMap.put("crafting-with-fluid", new WorkStation(0.75, new Size(3, 3), "assembling-machine-2"));
        workStationMap.put("smelting", new WorkStation(2, new Size(3, 3), "electric-furnace"));
        workStationMap.put("chemistry", new WorkStation(1, new Size(3, 3), "chemical-plant"));

        // Сырьевые компоненты
        List<String> rawComponents = new ArrayList<>();
        //rawComponents.add("iron-plate");
        //rawComponents.add("copper-plate");
        rawComponents.add("steel-plate");

        SchemeBuilder schemeBuilder = new SchemeBuilder(recipesReader.getRecipes(), workStationMap, rawComponents);
        schemeBuilder.build("utility-science-pack", 1);
        //schemeBuilder.build("artillery-shell", 0.1);
        //schemeBuilder.build("utility-science-pack", 1);
        //schemeBuilder.build("engine-unit", 1);
    }

}
