package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.buildings.Size;
import org.example.buildings.WorkStation;
import org.example.encode.Encoder;
import org.example.encode.Entity;
import org.example.read.Item;
import org.example.read.Recipe;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class SchemeBuilder {
    private final Map<String, Recipe> recipes;
    private final Map<String, WorkStation> workStations;

    public SchemeBuilder(Map<String, Recipe> recipes, Map<String, WorkStation> workStations) {
        this.recipes = recipes;
        this.workStations = workStations;
    }

    public void build(String resourceName, double countPerSecond) throws UnsupportedEncodingException, JsonProcessingException {
        List<Recipe> recipeList = getSuitableRecipes(resourceName);
        if (recipeList.isEmpty()) {
            throw new NullPointerException("Не найдено ни 1 рецепта с таким результатом");
        }
        PrimaryRecipeNode node = recipeTreeBuild(recipeList.get(0), resourceName, countPerSecond);
//        printTree(node, true);
//        System.out.println(multString("-", 100));
//        printTree(node, false);
//        System.out.println(multString("-", 100));
//        printLine(node);
        System.out.println(Encoder.encode(primarySchemeBuild(node), "bl"));

    }

    public List<Entity> primarySchemeBuild(PrimaryRecipeNode mainNode) {
        List<Entity> entities = new ArrayList<>();
        int currentXLevel = 0;

        Stack<PrimaryRecipeNode> stack = new Stack<>();
        stack.add(mainNode);
        while (!stack.isEmpty()) {
            PrimaryRecipeNode node = stack.pop();
            WorkStation workStation = WorkStation.getWorkStationInMap(workStations, node);
            if (workStation == null || node.getRecipe() == null) {
                continue;
            }

            for (int i = 0; i < node.getMachinesCount(workStation.getCoef()); i++) {
                // Конвееры
                entities.add(new Entity("transport-belt", new Size(currentXLevel - 1, node.getLevel() * -6 + 3), null, "4"));
                entities.add(new Entity("transport-belt", new Size(currentXLevel - 0, node.getLevel() * -6 + 3), null, "4"));
                entities.add(new Entity("transport-belt", new Size(currentXLevel + 1, node.getLevel() * -6 + 3), null, "4"));
                entities.add(new Entity("transport-belt", new Size(currentXLevel - 1, node.getLevel() * -6 - 3), null, "4"));
                entities.add(new Entity("transport-belt", new Size(currentXLevel - 0, node.getLevel() * -6 - 3), null, "4"));
                entities.add(new Entity("transport-belt", new Size(currentXLevel + 1, node.getLevel() * -6 - 3), null, "4"));

                // Манипуляторы
                entities.add(new Entity("fast-inserter", new Size(currentXLevel, node.getLevel() * -6 + 2), null, null));
                entities.add(new Entity("fast-inserter", new Size(currentXLevel, node.getLevel() * -6 - 2), null, null));

                // Рабочая станция
                entities.add(new Entity(workStation.getName(), new Size(currentXLevel, node.getLevel() * -6), node.getRecipe().getName(), null));
                currentXLevel -= 3;
            }

            stack.addAll(node.getChildren());
            currentXLevel -= 3;
        }
        return entities;
    }

    public void printLine(PrimaryRecipeNode mainNode) {
        Stack<PrimaryRecipeNode> stack = new Stack<>();
        stack.add(mainNode);
        while (!stack.isEmpty()) {
            PrimaryRecipeNode node = stack.pop();
            WorkStation workStation = WorkStation.getWorkStationInMap(workStations, node);
            if (workStation == null) {
                continue;
            }
            System.out.println(multString(" ", 2 * node.getLevel()) + "\\");
            System.out.println(multString(" ", 2 * node.getLevel() + 1) + node.getName());
            System.out.print("\u001B[34m");
            for (int i = 0; i < node.getMachinesCount(workStation.getCoef()); i++) {
                System.out.println(multString(" ", 2 * node.getLevel() + 1) + "*");
            }
            System.out.print("\u001B[0m");
            stack.addAll(node.getChildren());
        }
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
