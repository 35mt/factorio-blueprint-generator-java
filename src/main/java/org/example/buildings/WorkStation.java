package org.example.buildings;

import org.example.PrimaryRecipeNode;

import java.util.Map;

public class WorkStation {
    private final double coef;
    private final Size size;

    public WorkStation(double coef, Size size) {
        this.coef = coef;
        this.size = size;
    }

    public double getCoef() {
        return coef;
    }

    public Size getSize() {
        return size;
    }

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

