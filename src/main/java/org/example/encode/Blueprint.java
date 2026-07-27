package org.example.encode;

import java.util.List;
import java.util.Map;

public class Blueprint {
    private final String item;
    private final String label;
    private final String version;
    private final List<Entity> entities;

    public Blueprint(String item, String label, String version, List<Entity> entities) {
        this.item = item;
        this.label = label;
        this.version = version;
        this.entities = entities;
    }

    public String getItem() {
        return item;
    }

    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}

