package com.example.coffeemachine;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MakeBeverage {

    private CoffeeMachine coffeeMachine;

    public MakeBeverage(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    /**
     * Beverage preparation method - will prepare the requested beverage if 
     * requested a valid order and ingredients are sufficient.
     * 
     * @param order 
     */
    public void prepare(String order){
        /**
         * Check if requested order is present in beverage menu or not
         */
        if (coffeeMachine.beverages.containsKey(order.toLowerCase())) {
            try {
                final ExecutorService executorService = Executors.newFixedThreadPool(coffeeMachine.outlets);

                final MachineExecutor executor = new MachineExecutor(executorService, coffeeMachine.outlets);
                executor.submitTask(new Thread() {
                    @Override public void run() {
                        Beverage beverage = coffeeMachine.beverages.get(order);
                        
                        /**
                         * Check Availability of ingredients if ingredients are present or not and 
                         * check if running low in quantity to prepare the requested beverage
                         */
                        boolean flag = true;
                        for (Map.Entry<String, Integer> ingredient : beverage.getIngredients().entrySet()) {
                            if(coffeeMachine.ingredients.containsKey(ingredient.getKey())) {
                                if (coffeeMachine.ingredients.get(ingredient.getKey())<ingredient.getValue()) {
                                    System.out.println(order + " cannot be prepared because item " + ingredient.getKey() + " is not sufficient");
                                    flag = false;
                                    break;
                                }
                            } 
                            else {
                                System.out.println(order + " cannot be prepared because item " + ingredient.getKey() + " is not available");
                                flag= false;
                                break;
                            }
                        }

                        /**
                         * Start preparing order if all ingredients are present in sufficient quantity
                         */
                        if(flag) {
                            for (Map.Entry<String, Integer> ingredient : beverage.getIngredients().entrySet()) {
                                coffeeMachine.ingredients.put(ingredient.getKey(), coffeeMachine.ingredients.get(ingredient.getKey())-ingredient.getValue());
                            }
                            System.out.println("Your order: "+ order +" is ready.");
                        }

                    }
                });
            } catch (InterruptedException e) {
                System.out.println("Machine malfunctioned. Please restart machine.");
            }
        }
        /**
         * If requested beverage is not present in the coffee machine list
         */
        else {
           System.out.println("Order can't be processed as request is not present in the menu");
        }

    }
}
