package org.example;

import org.example.read.Recipe;

import java.util.ArrayList;
import java.util.List;

public class PrimaryRecipeNode {
    final private String name;
    final private double needPerSecond;
    final private Recipe recipe;
    final private PrimaryRecipeNode parent;
    final private List<PrimaryRecipeNode> children = new ArrayList<>();

    public PrimaryRecipeNode(String name, double needPerSecond, Recipe recipe, PrimaryRecipeNode parent) {
        this.name = name;
        this.needPerSecond = needPerSecond;
        this.recipe = recipe;
        this.parent = parent;
    }

    public PrimaryRecipeNode getParent() {
        return parent;
    }

    public List<PrimaryRecipeNode> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public double getNeedPerSecond() {
        return needPerSecond;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
