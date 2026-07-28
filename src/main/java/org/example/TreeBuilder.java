package org.example;

import org.example.buildings.WorkStation;
import org.example.read.Item;
import org.example.read.Recipe;

import java.util.*;

public class TreeBuilder {
    private final Map<String, Recipe> recipes;
    private final Map<String, WorkStation> workStations;
    private final List<String> rawComponents;

    public TreeBuilder(Map<String, Recipe> recipes, Map<String, WorkStation> workStations, List<String> rawComponents) {
        this.recipes = recipes;
        this.workStations = workStations;
        this.rawComponents = rawComponents;
    }

    public PrimaryRecipeNode recipeTreeBuild(Recipe primaryRecipe, String rName, double countPerSecond) {
       if (primaryRecipe.getCategory() != null && workStations.get(primaryRecipe.getCategory()) == null) {
           throw new NullPointerException("Нельзя составлять чертёж, когда конечный продукт требует рабочей станции неизвестной категории. Категория рецепта: " + primaryRecipe.getCategory());
       }

        // Главный и первый узел дерева - от него расходятся ветки дерева
        PrimaryRecipeNode mainTreeNode = new PrimaryRecipeNode(false, rName, countPerSecond, primaryRecipe, null, 0);

        if (rawComponents.contains(mainTreeNode.getName())) {
            throw new RuntimeException("Конечный продукт помечен как сырьё");
        }

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

                // Условия выхода из ветки
                boolean isEmptyRecipes = sRecipes.isEmpty(); // Если в рецептах нет ни одного подходящего - это конец ветки, так как больше некуда разворачивать
                if (isEmptyRecipes) {
                    continue;
                }

                PrimaryRecipeNode hLNode = new PrimaryRecipeNode(
                        false,
                        ingredient.getName(),
                        needsPerSecond,
                        sRecipes.get(0), // Пока что выбирается первый доступный рецепт
                        node,
                        node.getLevel() + 1);

                // Условия пропуска ингредиента
                boolean isRawComponent = rawComponents.contains(hLNode.getName());
                boolean isNoWorkStation = WorkStation.getWorkStationInMap(workStations, hLNode) == null;
                boolean isBarrelCycle = "empty-barrel".equals(hLNode.getRecipe().getSubgroup()); // Костыль - при выгрузке жидкостей из бочек происходит зацикливание
                if (isBarrelCycle || isRawComponent || isNoWorkStation) {
                    continue;
                }
                primaryRecipeNodes.add(hLNode);
                node.getChildren().add(hLNode);

            }
        }
        return mainTreeNode;
    }

    public void printTree(PrimaryRecipeNode mainNode, boolean isSoftPrint) {
        Stack<PrimaryRecipeNode> stack = new Stack<>();
        stack.add(mainNode);
        while (!stack.isEmpty()) {
            PrimaryRecipeNode node = stack.pop();
            WorkStation workStation = WorkStation.getWorkStationInMap(workStations, node);
            if (workStation == null && isSoftPrint) {
                continue;
            }
            if (workStation == null) {
                System.out.print("\u001B[31m");
            } else {
                System.out.print("\u001B[0m");
            }
            System.out.println(multString(" ", 5 * node.getLevel()) + "*" + node.getName() + "; "
                    + node.getNeedPerSecond() + "; " + node.getMachinesCount(workStation == null ? 0 : workStation.getCoef()) + "; "
                    + (node.isBranchEnd() ? "" : (node.getRecipe().getCategory() + "; " + node.getRecipe().getSubgroup())));

            stack.addAll(node.getChildren());
        }
    }

    private String multString(String string, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public List<Recipe> getSuitableRecipes(String resourceName) {
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
