package org.example.read;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe implements Serializable {
    private String name;
    private String category;
    private String subgroup;
    private double energy_required;
    @JsonDeserialize(using = IngredientsDeserializer.class)
    private List<Item> ingredients;
    @JsonDeserialize(using = IngredientsDeserializer.class)
    private List<Item> results;

    public Recipe() {
    }

    public List<Item> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Item> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Item> getResults() {
        return results;
    }

    public void setResults(List<Item> results) {
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        if (category == null) return "null";
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public double getEnergy_required() {
        return energy_required == 0.0 ? 0.5 : energy_required;
    }

    public void setEnergy_required(double energy_required) {
        this.energy_required = energy_required;
    }
}

