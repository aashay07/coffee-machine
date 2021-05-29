package com.example.coffeemachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestRunner {
    
    public static void main(String[] args) {
        try {
            /**
             * Reading input data from input.json and test.json files
             */
            Path path = Paths.get("src/main/resources/input.json");
            final FileReader inputfr = new FileReader(path.toString());
            path = Paths.get("src/main/resources/test.json");
            final FileReader testfr = new FileReader(path.toString());

            final BufferedReader inputbr = new BufferedReader(inputfr);
            final BufferedReader testbr = new BufferedReader(testfr);
            
            String inputJSON = "";
            String testJson = "";
            String line ="";
            while ((line = inputbr.readLine()) != null) {
                inputJSON += line;
            }
            line ="";
            while ((line = testbr.readLine()) != null) {
        	testJson += line;
            }
            JSONObject input = new JSONObject(inputJSON);
            JSONArray requestList = new JSONArray(testJson);
            JSONObject machineInput = input.getJSONObject("machine");
            Integer numberOFOutlets = machineInput.getJSONObject("outlets").getInt("count_n");
            JSONObject totalItems = machineInput.getJSONObject("total_items_quantity");
            ConcurrentHashMap<String, Integer> machineIngredients = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Integer> refill = new ConcurrentHashMap<>();

            Iterator<String> keys = totalItems.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                if (totalItems.get(key) instanceof Integer) {
                    machineIngredients.put(key,(Integer)totalItems.get(key));
                    refill.put(key,(Integer)totalItems.get(key));
                }
            }

            /**
             * Initialising Beverage Ingredients and Machine Beverage HashMaps
             */
            JSONObject beverages = machineInput.getJSONObject("beverages");
            ConcurrentHashMap<String, Beverage> machineBeverages = new ConcurrentHashMap<>();
            keys = beverages.keys();
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
             * Instance of StartMachine which will initialise CoffeeMachine Instance
             */
            StartMachine startMachine = new StartMachine(numberOFOutlets,machineIngredients, machineBeverages,refill);
            CoffeeMachine coffeeMachine = startMachine.coffeeMachine;
            MakeBeverage makeBeverage = startMachine.makeBeverage;

            /**
             * Running prepare() method for beverage request list which was fetched from test.json
             */
            for(int i=0; i<requestList.length(); i++) {
                makeBeverage.prepare(requestList.getString(i));
            }
        } catch (final Exception e) {
            System.out.println("Machine not working correctly :: "+e);
        }
    }

}
