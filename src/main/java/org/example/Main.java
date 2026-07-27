package org.example;

import org.example.buildings.Size;
import org.example.buildings.WorkStation;
import org.example.read.Recipe;
import org.example.read.RecipesReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        RecipesReader recipesReader = new RecipesReader(new File("recipes.json"));
        Map<String, WorkStation> workStationMap = new HashMap<>();
        workStationMap.put(null, new WorkStation(0.75, new Size(3,3)));
        workStationMap.put("crafting", new WorkStation(0.75, new Size(3,3)));
        workStationMap.put("smelting", new WorkStation(2, new Size(3,3)));

        SchemeBuilder schemeBuilder = new SchemeBuilder(recipesReader.getRecipes(), workStationMap);
        schemeBuilder.build("utility-science-pack", 2);
        }
    }
