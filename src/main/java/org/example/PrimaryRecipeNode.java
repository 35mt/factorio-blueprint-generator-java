package org.example;

import org.example.read.Recipe;

public class PrimaryRecipeNode {
    final private String name;
    final private String parent;
    final private double needPerSecond;
    final private Recipe recipe;

    public PrimaryRecipeNode(String name, String parent, double needPerSecond, Recipe recipe) {
        this.name = name;
        this.parent = parent;
        this.needPerSecond = needPerSecond;
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public double getNeedPerSecond() {
        return needPerSecond;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
