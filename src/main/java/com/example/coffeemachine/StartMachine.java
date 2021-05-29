package com.example.coffeemachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

public class StartMachine {

    CoffeeMachine coffeeMachine;
    static Scanner scan = new Scanner(System.in);
    MakeBeverage makeBeverage;
    ConcurrentHashMap<String, Integer> refill;

    public StartMachine(int numberOFOutlets, ConcurrentHashMap<String, Integer> machineIngredients, 
	    ConcurrentHashMap<String, Beverage> machineBeverages, ConcurrentHashMap<String, Integer> refill){
	this.refill = refill;
	this.coffeeMachine = new CoffeeMachine(numberOFOutlets,machineIngredients, machineBeverages);
        this.makeBeverage = new MakeBeverage(this.coffeeMachine);
    }

    /**
     * Method to start coffee machine where user can interact and command the machine 
     * to perform different tasks.
     * This method communicate with other classes, CoffeeMachine & MakeBeverage, to perform
     * different tasks such as show current quantites of ingredients, refill ingredients and prepare beverage.
     */
    public void start(){
        System.out.println(" xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx ");
        System.out.println("|                   Coffee Machine                    |");
        System.out.println(" xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx ");

        System.out.println("\nCurrent Status: ");
        coffeeMachine.displayIngredients();
        boolean running = true;
        while(running){
            System.out.println("\n -------------------------------- ");
            System.out.println("|1:     Status of Ingredient     |"
                    + "\n -------------------------------- \n"
                    + "|2:      Fill Ingredients        |\n"
                    + " -------------------------------- \n"
                    + "|3:        Make Coffee           |\n"
                    + " -------------------------------- \n"
                    + "|4:        Shut Down             |");
            System.out.println(" -------------------------------- \n\n");
            char c = scan.next().charAt(0);
            
            /**
             *  User can give input to perform different functions by machine such as refilling
             *  or place beverage order.
             */
            switch(c){
                case '1':
                    System.out.println("------------- Status ------------");
                    coffeeMachine.displayIngredients();
                    System.out.println("---------------------------------");
                    break;
                case '2':
                    coffeeMachine.setIngredients(refill);
                    break;
                case '3':
                    coffeeMachine.displayMenu();
                    makeBeverage.prepare(scan.next());
                    break;
                default:
                    System.out.println("\nExiting...\n");
                    running = false;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            /**
             * Reading input data from input.json file
             */
            Path path = Paths.get("src/main/resources/input.json");
            final FileReader fr = new FileReader(path.toString());

            final BufferedReader br = new BufferedReader(fr);
            String inputJSON = "";
            String line ="";
            while ((line = br.readLine()) != null) {
                inputJSON+=line;
            }
            JSONObject input = new JSONObject(inputJSON);

            JSONObject machineInput = input.getJSONObject("machine");

            Integer numberOFOutlets = machineInput.getJSONObject("outlets").getInt("count_n");
            JSONObject totalItems = machineInput.getJSONObject("total_items_quantity");
            ConcurrentHashMap<String, Integer> machineIngredients = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Integer> refill = new ConcurrentHashMap<>();

            Iterator<String> keys = totalItems.keys();

            /**
             * Initialising Machine Ingredient stock HashMaps
             */
            while(keys.hasNext()) {
                String key = keys.next();
                if (totalItems.get(key) instanceof Integer) {
                    machineIngredients.put(key,(Integer)totalItems.get(key));
                    refill.put(key,(Integer)totalItems.get(key));
                }
            }

            JSONObject beverages = machineInput.getJSONObject("beverages");
            ConcurrentHashMap<String, Beverage> machineBeverages = new ConcurrentHashMap<>();
            keys = beverages.keys();
            /**
             * Initialising Machine Beverage and Beverage Ingredients HashMap
             */
            while(keys.hasNext()) {
                HashMap<String, Integer> beverageIngredients = new HashMap<>();
                String beverageName = keys.next();
                if (beverages.get(beverageName) instanceof JSONObject) {
                    JSONObject bvIngredient = (JSONObject)beverages.get(beverageName);
                    Iterator<String> bvKeys = bvIngredient.keys();
                    while(bvKeys.hasNext()) {
                        String key = bvKeys.next();
                        if (bvIngredient.get(key) instanceof Integer) {
                            beverageIngredients.put(key,(Integer)bvIngredient.get(key));
                        }
                    }
                }
                machineBeverages.put(beverageName, new Beverage(beverageName,beverageIngredients));
            }

            /**
             * Instances For StartMachine which initialise CoffeeMachine instance
             */
            StartMachine startMachine = new StartMachine(numberOFOutlets,machineIngredients, machineBeverages,refill);

            /**
             * Calling start() method to start taking input from user
             */
            startMachine.start();
            System.out.println("Shutting Down...\n");
        } catch (final Exception e) {
            System.out.println("Machine not working correctly :: "+e);

        }
    }

}
