package org.example;

import org.example.read.Item;
import org.example.read.Recipe;

import java.util.ArrayList;
import java.util.List;

public class PrimaryRecipeNode {
    final private boolean isBranchEnd;
    final private String name;
    final private double needPerSecond;
    final private Recipe recipe;
    final private PrimaryRecipeNode parent;
    final private List<PrimaryRecipeNode> children = new ArrayList<>();
    final private int level;

    public PrimaryRecipeNode(boolean isBranchEnd, String name, double needPerSecond, Recipe recipe, PrimaryRecipeNode parent, int level) {
        this.isBranchEnd = isBranchEnd;
        this.name = name;
        this.needPerSecond = needPerSecond;
        this.recipe = recipe;
        this.parent = parent;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isBranchEnd() {
        return isBranchEnd;
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

    public double getResultResourceCount() {
        for (Item result : recipe.getResults()) {
            if (result.getName().equals(name)) {
                return result.getAmount();
            }
        }
        throw new NullPointerException("В результатах рецепта узла не найдено основного производимого ресурса");
    }
}
