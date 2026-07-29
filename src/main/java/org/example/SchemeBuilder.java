package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.buildings.Size;
import org.example.buildings.WorkStation;
import org.example.encode.Encoder;
import org.example.encode.Entity;
import org.example.read.Recipe;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class SchemeBuilder {
    private final Map<String, Recipe> recipes;
    private final Map<String, WorkStation> workStations;
    private final List<String> rawComponents;
    private final TreeBuilder treeBuilder;

    public SchemeBuilder(Map<String, Recipe> recipes, Map<String, WorkStation> workStations, List<String> rawComponents) {
        this.recipes = recipes;
        this.workStations = workStations;
        this.rawComponents = rawComponents;
        this.treeBuilder = new TreeBuilder(recipes, workStations, rawComponents);
    }

    public String build(String resourceName, double countPerSecond) throws UnsupportedEncodingException, JsonProcessingException {
        List<Recipe> recipeList = treeBuilder.getSuitableRecipes(resourceName);
        if (recipeList.isEmpty()) {
            throw new NullPointerException("Не найдено ни 1 рецепта с таким результатом");
        }
        PrimaryRecipeNode node = treeBuilder.recipeTreeBuild(recipeList.get(0), resourceName, countPerSecond);

        return Encoder.encode(primarySchemeBuild(node), "bl");

    }

    public List<Entity> primarySchemeBuild(PrimaryRecipeNode mainNode) {
        List<Entity> entities = new ArrayList<>();
        // Список точек обрыва конвейера - для слепого соединения конвейеров неизвестной длины на одном уровне
        List<Size> breakPoints = new ArrayList<>();
        int currentXLevel = 0;

        Stack<PrimaryRecipeNode> stack = new Stack<>();
        stack.add(mainNode);
        while (!stack.isEmpty()) {
            PrimaryRecipeNode node = stack.pop();
            WorkStation workStation = WorkStation.getWorkStationInMap(workStations, node);

            int startXLevel = currentXLevel;

            int yShift = getYShift(node);
            boolean isTwoLine = getTransportBeltCount(node.getRecipe().getIngredients().size()) >= 2;
            for (int i = 0; i < node.getMachinesCount(workStation.getCoef()); i++) {
                currentXLevel -= 3;
                // Конвейеры
                addTopTransportBelts(entities, currentXLevel, yShift, isTwoLine, i);
                entities.add(new Entity("transport-belt", new Size(currentXLevel - 1, yShift + 3), null, "4"));
                entities.add(new Entity("transport-belt", new Size(currentXLevel + 1, yShift + 3), null, "4"));
                entities.add(new Entity("transport-belt", new Size(currentXLevel - 0, yShift + 3), null, "4"));

                // Манипуляторы
                if (isTwoLine)
                    entities.add(new Entity("long-handed-inserter", new Size(currentXLevel + 1, yShift - 2), null, null));
                entities.add(new Entity("fast-inserter", new Size(currentXLevel, yShift + 2), null, null));
                entities.add(new Entity("fast-inserter", new Size(currentXLevel, yShift - 2), null, null));

                // Рабочая станция
                entities.add(new Entity(workStation.getName(), new Size(currentXLevel, yShift), node.getRecipe().getName(), null));
            }
            // Добавляем точки разрыва конвейеров к верхним принимающим линиям
            if (isTwoLine) breakPoints.add(new Size(currentXLevel - 1, yShift - 4));
            breakPoints.add(new Size(currentXLevel - 1, yShift - 3));

            // sffsef
            addIngredientReceiver(entities, breakPoints, node, startXLevel,  currentXLevel, yShift);

            // Добавляем дочерние узлы в обратном порядке, для правильного смещения по индексу при 2 линиях конвейеров
            List<PrimaryRecipeNode> reversedChildren = new ArrayList<>(node.getChildren());
            Collections.reverse(reversedChildren);
            stack.addAll(reversedChildren);

            currentXLevel -= 3;
        }
        return entities;
    }

    private void addIngredientReceiver(List<Entity> entities, List<Size> breakPoints, PrimaryRecipeNode node, int startXLevel, int currentXLevel, int yShift) {
        if (node.getParent() == null) {
            return;
        }
        // Номер ингредиента в рецепте родителя
        List<PrimaryRecipeNode> parentIngredintsList = node.getParent().getChildren();
        int nodeIndex = parentIngredintsList.indexOf(node);
        if ((parentIngredintsList.size() >= 2 && nodeIndex == 0) || (parentIngredintsList.size() >= 4 && nodeIndex == 2)) {
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 2, yShift + 2), null, "8"));
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 2, yShift + 3), null, "4"));
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 3, yShift + 3), null, "4"));
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 3, yShift + 2), null, "4"));
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 4, yShift + 2), null, "4"));
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 4, yShift + 3), null, "0"));
            breakPoints.add(new Size(currentXLevel - 4, yShift + 3));
        }

        // Соединение линий конвейеров на неизвестном расстоянии по breakPoints
        int i = 0;
        while (true) {
            Size currentSize = new Size(startXLevel - 1, yShift + 3).xShift(i);
            if (breakPoints.contains(currentSize)) {
                break;
            }
            entities.add(new Entity("transport-belt", currentSize, null, "4"));
            i++;
        }

    }

    private void addTopTransportBelts(List<Entity> entities, int currentXLevel, int yShift, boolean isTwoLine, int i) {
        if (i == 0) {
            if (isTwoLine) {
                entities.add(new Entity("transport-belt", new Size(currentXLevel + 1, yShift - 4), null, "8"));
            }
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 0, yShift - 3), null, "8"));
        } else {
            if (isTwoLine) {
                entities.add(new Entity("transport-belt", new Size(currentXLevel + 1, yShift - 4), null, "4"));
            }

            entities.add(new Entity("transport-belt", new Size(currentXLevel - 0, yShift - 3), null, "4"));
            entities.add(new Entity("transport-belt", new Size(currentXLevel + 1, yShift - 3), null, "4"));
        }
        // Конвейеры
        if (isTwoLine) {
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 1, yShift - 4), null, "4"));
            entities.add(new Entity("transport-belt", new Size(currentXLevel - 0, yShift - 4), null, "4"));
        }
        entities.add(new Entity("transport-belt", new Size(currentXLevel - 1, yShift - 3), null, "4"));
    }

    private int getYShift(PrimaryRecipeNode topNode) {
        if (getTransportBeltCount(topNode.getRecipe().getIngredients().size()) > 2) {
            throw new RuntimeException("В рецепте " + topNode.getName() + " Необходимо задействовать больше 2 полос конвейеров, пока что скрипт так не умеет - добавьте этот компонент в сырьевые");
        }
        // Если передан главный узел - сразу возврат
        if (topNode.getLevel() == 0) {
            return 0;
        }

        // Индивидуальный сдвиг в зависимости от индекса в списке ингредиентов у родительского ресурса
        int yShift = 0;

        // Рекурсивный проход по всем уровням для общего сдвига
        PrimaryRecipeNode node = topNode;

        while (node.getParent() != null) {
            yShift -= getTransportBeltCount(node.getParent().getRecipe().getIngredients().size()) + 5;
            yShift += node.getParent().getChildren().indexOf(node) / 2;

            node = node.getParent();
        }
        return yShift;
    }

    private int getTransportBeltCount(int ingredientCount) {
        return (int) Math.ceil((double) ingredientCount / 2);
    }

}
