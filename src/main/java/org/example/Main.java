package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        RecipesReader recipesReader = new RecipesReader(new File("recipes.json"));
        List<Recipe> recipes =  recipesReader.getRecipes().values().stream().toList();
        System.out.println( recipes.get(100).getName());
        }
    }
