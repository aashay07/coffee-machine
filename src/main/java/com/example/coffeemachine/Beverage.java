package com.example.coffeemachine;

import java.util.Map;

/**
 * Beverage entity class to have instance of different beverages and details of ingredients required for the beverage
 * 
 * @author aashaymedatwal
 *
 */
public class Beverage {
    String name;
    Map<String,Integer> ingredients;

    public Beverage(String name, Map<String, Integer> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(Map<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }
}
