# coffee-machine
Coffee Machine

Steps to run:
1. Update the project with maven dependency by running **"mvn clean install"** from
  terminal or **update project** in any IDE.
2. All classes are present under **/src/main/java/com/example/coffeemachine/** and resources
  file such as json files are under **/src/main/resources/**. There are 2 main classes
  that can be run, **StartMachine.java** and **TestRunner.java**
3. **StartMachine.java** can be used to run to have user interaction with application
  where user can command the machine to perform different functions. **input.json**
  is the input file that is used to inbitialise the coffee machine with beverage list
  and ingredients stock.
4. **TestRunner.java** can be run to test the multithreading functionality of coffee machine
  to prepare beverage.
5. To test, **/src/main/resources/** path has 2 json files, **input.json** to define the
  coffee machine and **test.json** contains list of orders that need to prepared by
  the coffee machine. We can update these files to test the code with different
  testcase scenarios.
