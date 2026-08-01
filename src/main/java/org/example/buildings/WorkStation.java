package org.example.buildings;

import org.example.PrimaryRecipeNode;

import java.util.Map;

public record WorkStation (double coef, Size size, String name) {
    public static WorkStation getWorkStationInMap(Map<String, WorkStation> workStations, PrimaryRecipeNode node) {
        WorkStation workStation;
        try {
            workStation = workStations.get(node.getRecipe().getCategory());
        } catch (NullPointerException e) {
            return null;
        }
        return workStation;
    }
}

