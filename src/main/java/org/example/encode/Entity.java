package org.example.encode;

import org.example.buildings.Size;

public class Entity {
    private static int currentNumber = 1;
    private final String entity_number;
    private final String name;
    // Size не как размер, а как координаты
    private final Size position;

    public Entity(String name, Size position) {
        this.entity_number = String.valueOf(currentNumber++);
        this.name = name;
        this.position = position;
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
