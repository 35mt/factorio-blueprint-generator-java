package org.example.encode;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.buildings.Size;

public class Entity {
    private static int currentNumber = 1;
    private final String entity_number;
    private final String name;
    // Size не как размер, а как координаты
    private final Size position;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String recipe;

    public Entity(String name, Size position, String recipe) {
        this.entity_number = String.valueOf(currentNumber++);
        this.recipe = recipe;
        this.name = name;
        this.position = position;
    }

    public String getRecipe() {
        return recipe;
    }

    public String getEntity_number() {
        return entity_number;
    }

    public String getName() {
        return name;
    }

    public Size getPosition() {
        return position;
    }
}
