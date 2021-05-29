package com.example.coffeemachine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CoffeeMachine {

    int outlets;
    ConcurrentHashMap<String,Integer> ingredients;
    ConcurrentHashMap<String,Beverage> beverages;

    /**
     * Constructor to initialise the coffee machine with N outlets
     * @param outlets
     * @param ingredients
     * @param beverages
     */
    public CoffeeMachine(int N, ConcurrentHashMap<String, Integer> ingredients, ConcurrentHashMap<String, Beverage> beverages) {
        this.outlets = N;
        this.ingredients = ingredients;
        this.beverages = beverages;
    }

    /**
     * Method to refill the ingredients
     * @param refill
     */
    public void setIngredients(ConcurrentHashMap<String, Integer> refill) {
        if(this.ingredients==null || this.ingredients.size()==0){
            return;
        }
        else {
            for (Map.Entry<String, Integer> ingredient : refill.entrySet()) {
                /**
                 * Refill only those ingredients that were initially present in the machine.
                 * Any new ingredient will be discarded as machine wasn't initialised with that.
                  */
                if(this.ingredients.containsKey(ingredient.getKey())) {
                    int quantity = this.ingredients.get(ingredient.getKey());
                    this.ingredients.put(ingredient.getKey(), quantity + ingredient.getValue());
                }
            }
        }
    }

    /**
     * Method to show current status of ingredients quantities
     */
    public void displayIngredients(){
        for (Map.Entry<String, Integer> ingredient : ingredients.entrySet()) {
            System.out.println("Available " + ingredient.getKey() + ": "+ ingredient.getValue());
        }
    }
    
    /**
     * Method to show menu of the coffee machine, what beverages can be prepared
     */
    public void displayMenu() {
	System.out.println("\n ------------------ ");
	System.out.println("|       Menu       |\n");
        System.out.println("|   Select Type:   |\n");
        for (Map.Entry<String, Beverage> beverage : beverages.entrySet()) {
            System.out.println(beverage.getKey());
        }
        System.out.println(" ------------------ \n");
    }
}
