package org.example;

import org.example.read.Item;
import org.example.read.Recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SchemeBuilder {
    private Map<String, Recipe> recipes;
    public SchemeBuilder(Map<String, Recipe> recipes) {
        this.recipes = recipes;
    }

    public void build(String resourceName, double countPerSecond) {
        List<Recipe> recipeList = getSuitableRecipes(resourceName);
        System.out.println( recipeList.get(0).getName());
        //System.out.println( recipeList.get(1).getName());

    }

    private List<Recipe> getSuitableRecipes(String resourceName) {
        List<Recipe> suitableRecipes = new ArrayList<>();
        for (Iterator<Recipe> it = recipes.values().iterator(); it.hasNext(); ) {
            Recipe recipe = it.next();
            if (recipe.getResults() == null) continue;
            for (Item result : recipe.getResults()) {
                if (result != null && result.getName().equals(resourceName)) {
                    suitableRecipes.add(recipe);
                }
            }
        }
        return suitableRecipes;
    }
}
