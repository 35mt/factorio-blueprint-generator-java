package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.buildings.Size;
import org.example.buildings.WorkStation;
import org.example.read.Recipe;
import org.example.read.RecipesReader;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws IOException {
        RecipesReader recipesReader = new RecipesReader(new File("data-raw-dump.json"));
        baseShell(recipesReader.getRecipes());

        // Пример использования
//        // Рабочие станции
//        Map<String, WorkStation> workStationMap = new HashMap<>();
//        workStationMap.put(null, new WorkStation(0.75, new Size(3, 3), "assembling-machine-2"));
//        workStationMap.put("crafting", new WorkStation(0.75, new Size(3, 3), "assembling-machine-2"));
//        workStationMap.put("advanced-crafting", new WorkStation(0.75, new Size(3, 3), "assembling-machine-2"));
//        workStationMap.put("crafting-with-fluid", new WorkStation(0.75, new Size(3, 3), "assembling-machine-2"));
//        workStationMap.put("smelting", new WorkStation(2, new Size(3, 3), "electric-furnace"));
//        workStationMap.put("chemistry", new WorkStation(1, new Size(3, 3), "chemical-plant"));
//
//        // Сырьевые компоненты
//        List<String> rawComponents = new ArrayList<>();
//        //rawComponents.add("iron-plate");
//        //rawComponents.add("copper-plate");
//        rawComponents.add("steel-plate");
//
//        SchemeBuilder schemeBuilder = new SchemeBuilder(recipesReader.getRecipes(), workStationMap, rawComponents);
//        schemeBuilder.build("utility-science-pack", 1);
//        //System.out.println(schemeBuilder.build("artillery-shell", 0.1));
//        //System.out.println(schemeBuilder.build("utility-science-pack", 1));
//        //System.out.println(schemeBuilder.build("engine-unit", 1));
    }

    static void baseShell(Map<String, Recipe> recipes) {
        Scanner scanner = new Scanner(System.in);

        Map<String, WorkStation> workStationMap = new HashMap<>();
        List<String> rawComponents = new ArrayList<>();

        String input = "";
        while (!input.equals("0")) {
            System.out.println("0: Выход");
            System.out.println("1: Добавить рабочую станцию");
            System.out.println("2: Добавить сырьевой ресурс");
            System.out.println("3: Собрать схему");
            System.out.println("Выберете действие: ");
            input = scanner.nextLine();
            if (input.equals("1")) {
                System.out.println("Введите категорию (строчный код) рецепта для рабочей станции: ");
                String category = scanner.nextLine();
                System.out.println("Введите название (строчный код) рабочей станции: ");
                String name = scanner.nextLine();
                System.out.println("Введите коэффициент скорости изготовления: ");
                double coef;
                try {
                    coef = Float.parseFloat(scanner.nextLine().replace(",", "."));
                } catch (Exception e) {
                    System.out.println("Ошибка при парсинге коэффициента: " + e.getMessage());
                    continue;
                }

                workStationMap.put(!category.equals("null") ? category : null, new WorkStation(coef, new Size(3, 3), name));
            } else if (input.equals("2")) {
                System.out.println("Введите название (строчный код) сырьевого ресурса: ");
                rawComponents.add(scanner.nextLine());
            } else if (input.equals("3")) {
                System.out.println("Введите название итогового ресурса (строчный код) для составления схемы: ");
                String name = scanner.nextLine();
                double count;
                System.out.println("Введите необходимое количество ресурса в секунду: ");
                try {
                    count = Float.parseFloat(scanner.nextLine().replace(",", "."));
                } catch (Exception e) {
                    System.out.println("Ошибка при парсинге количества: " + e.getMessage());
                    continue;
                }
                SchemeBuilder schemeBuilder = new SchemeBuilder(recipes, workStationMap, rawComponents);
                try {
                    System.out.println(schemeBuilder.build(name, count));
                } catch (JsonProcessingException e) {
                    System.out.println("JsonProcessingException: " + e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    System.out.println("UnsupportedEncodingException: " + e.getMessage());
                }
            }
        }
    }
}
