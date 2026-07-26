package org.example;

import org.example.read.Recipe;
import org.example.read.RecipesReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        RecipesReader recipesReader = new RecipesReader(new File("recipes.json"));
        SchemeBuilder schemeBuilder = new SchemeBuilder(recipesReader.getRecipes());
        schemeBuilder.build("utility-science-pack", 5);
        }
    }
