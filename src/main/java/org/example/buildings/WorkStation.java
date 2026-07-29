package org.example.buildings;

import org.example.PrimaryRecipeNode;

import java.util.Map;

public class WorkStation {
    private final double coef;
    private final Size size;
    private final String name;

    public WorkStation(double coef, Size size, String name) {
        this.coef = coef;
        this.size = size;
        this.name = name;
    }

    public String getName() {
        return name;
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
        System.out.println(workStation);
        return workStation;
    }
}

