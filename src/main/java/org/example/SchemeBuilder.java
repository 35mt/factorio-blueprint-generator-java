package org.example;

import org.example.read.Item;
import org.example.read.Recipe;

import java.util.*;

public class SchemeBuilder {
    private final Map<String, Recipe> recipes;

    public SchemeBuilder(Map<String, Recipe> recipes) {
        this.recipes = recipes;
    }

    public void build(String resourceName, double countPerSecond) {
        List<Recipe> recipeList = getSuitableRecipes(resourceName);
        if (recipeList.isEmpty()) {
            throw new NullPointerException("Не найдено ни 1 рецепта с таким результатом");
        }
        PrimaryRecipeNode node = recipeTreeBuild(recipeList.get(0), resourceName, countPerSecond);
        printTree(node);

    }

    public void printTree(PrimaryRecipeNode mainNode) {
        Stack<PrimaryRecipeNode> stack = new Stack<>();
        stack.add(mainNode);
        while (!stack.isEmpty()) {
            PrimaryRecipeNode node = stack.pop();
            System.out.println(multString(" ", 5 * node.getLevel()) + "*" + node.getName() + "; " + node.getNeedPerSecond());

            stack.addAll(node.getChildren());
        }
    }

    private String multString(String string, int count){
        StringBuilder stringBuilder =new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    private PrimaryRecipeNode recipeTreeBuild(Recipe primaryRecipe, String rName,  double countPerSecond) {
        // Главный и первый узел дерева - от него расходятся ветки дерева
        PrimaryRecipeNode mainTreeNode = new PrimaryRecipeNode(false, rName, countPerSecond, primaryRecipe, null, 0);

        Stack<PrimaryRecipeNode> primaryRecipeNodes = new Stack<>();
        primaryRecipeNodes.add(mainTreeNode);

        while (!primaryRecipeNodes.isEmpty()) {
            PrimaryRecipeNode node = primaryRecipeNodes.pop(); // Низкоуровневый узел

            // Перебор ингредиентов узла для создания новых узлов или завершении ветки
            for (Item ingredient : node.getRecipe().getIngredients()) {
                // Создание высокоуровневого узла (1 ингредиент для низкоуровневого)
                double needsPerSecond = (node.getNeedPerSecond() * ingredient.getAmount()) / node.getResultResourceCount();

                // Получение доступных рецептов для этого узла
                List<Recipe> sRecipes = getSuitableRecipes(ingredient.getName());
                boolean isBranchEnd = sRecipes.isEmpty(); // Если в рецептах нет ни одного подходящего - это конец ветки, так как больше некуда разворачивать

                PrimaryRecipeNode hLNode = new PrimaryRecipeNode(
                        isBranchEnd,
                        ingredient.getName(),
                        needsPerSecond,
                        isBranchEnd ? null : sRecipes.get(0), // Пока что выбирается первый доступный рецепт
                        node,
                        node.getLevel() + 1);

                if (!isBranchEnd &&
                        !((hLNode.getRecipe().getCategory() != null && hLNode.getRecipe().getCategory().equals("oil-processing"))
                                || (hLNode.getRecipe().getSubgroup() != null && hLNode.getRecipe().getSubgroup().equals("empty-barrel")))) {
                    primaryRecipeNodes.add(hLNode);
                }

                node.getChildren().add(hLNode);
            }
        }
        return mainTreeNode;
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
