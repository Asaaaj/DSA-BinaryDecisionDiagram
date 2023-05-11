import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("DSA - Zadanie 2");

        Scanner sc = new Scanner(System.in);
        int scanner = 0;
        while (scanner != 4) {
            System.out.println("Choose operation: \n(1) - BDD_create\n(2) - BDD_create_with_best_order\n(3) - BDD_use\n\n (4) - EXIT");
            scanner = sc.nextInt();
            switch (scanner) {
                case 1 -> {
                    System.out.println("Number of variables: ");
                    int numberOfVariables = sc.nextInt();
                    System.out.println("Number of tests: ");
                    int numberOfTests = sc.nextInt();
                    long times = 0;
                    double reductionRate = 0;
                    for (int number = 0; number < numberOfTests; number++) {
                        BDD bdd = new BDD();
                        String booleanFunction = generateBooleanFunction(numberOfVariables);
                        Instant startTime = Instant.now();
                        bdd.BDD_create(booleanFunction, getOrderOfBooleanFunction(booleanFunction));
                        Instant endTime = Instant.now();
                        times += Duration.between(startTime, endTime).toMillis();
                        reductionRate += ((1 - ((double)bdd.numberOfNodesAfterReduction / bdd.numberOfNodesBeforeReduction)) * 100);
                        System.out.println("TEST #" + (number + 1) + " of " + numberOfTests);
                        System.out.println("DNF: " + booleanFunction + "\nOrder: " + bdd.order + "\nNumber of variables: " + bdd.numberOfVariables + "\nNumber of Nodes Before Reduction: " + bdd.numberOfNodesBeforeReduction + "\nNumber of Nodes After Reduction: " + bdd.numberOfNodesAfterReduction);
                        System.out.println("Reduction rate: " + String.format("%.4f", ((1 - ((double)bdd.numberOfNodesAfterReduction / bdd.numberOfNodesBeforeReduction)) * 100)) + "%\tDuration: " + Duration.between(startTime, endTime).toMillis() + " ms\n\n");
                    }
                    System.out.println("Average time: " + ((double) times / numberOfTests) + "ms");
                    System.out.println("Average reduction rate: " + (reductionRate / numberOfTests) + "%\n\n");
                }
                case 2 -> {
                    double reductionRate = 0;
                    System.out.println("Number of variables: ");
                    int numberOfVariables = sc.nextInt();
                    System.out.println("Number of tests: ");
                    int numberOfTests = sc.nextInt();
                    for (int number = 0; number < numberOfTests; number++) {
                        System.out.println("TEST #" + (number + 1) + " of " + numberOfTests);
                        BDD bdd = new BDD();
                        String booleanFunction = generateBooleanFunction(numberOfVariables);
                        Instant startTime = Instant.now();
                        bdd = bdd.BDD_create_with_best_order(booleanFunction);
                        Instant endTime = Instant.now();
                        reductionRate += ((1 - ((double)bdd.numberOfNodesAfterReduction / bdd.numberOfNodesBeforeReduction)) * 100);
                        System.out.println("DNF: " + booleanFunction + "\nBest Order: " + bdd.order + "\nNumber of variables: " + bdd.numberOfVariables + "\nNumber of Nodes Before Reduction: " + bdd.numberOfNodesBeforeReduction + "\nNumber of Nodes After Reduction: " + bdd.numberOfNodesAfterReduction);
                        System.out.println("Reduction rate: " + String.format("%.4f", ((1 - ((double)bdd.numberOfNodesAfterReduction / bdd.numberOfNodesBeforeReduction)) * 100)) + "%\tDuration: " + Duration.between(startTime, endTime).toMillis() + " ms\n\n");
                    }
                    System.out.println("Average reduction rate: " + (reductionRate / numberOfTests) + "%\n\n");
                }
                case 3 -> {
                    long times = 0;
                    System.out.println("Number of variables: ");
                    int numberOfVariables = sc.nextInt();
                    System.out.println("Number of tests: ");
                    int numberOfTests = sc.nextInt();
                    for (int number = 0; number < numberOfTests; number++) {
                        BDD bdd = new BDD();
                        String booleanFunction = generateBooleanFunction(numberOfVariables);
                        String useIpnut = generateUseFunction(booleanFunction);
                        bdd.BDD_create(booleanFunction, getOrderOfBooleanFunction(booleanFunction));
                        Instant startTime = Instant.now();
                        String output = bdd.BDD_use(bdd, useIpnut);
                        Instant endTime = Instant.now();
                        times += Duration.between(startTime, endTime).toMillis();
                        System.out.println("TEST #" + (number + 1) + " of " + numberOfTests);
                        System.out.println("DNF: " + booleanFunction + "\nOrder: " + bdd.order + "\nNumber of variables: " + bdd.numberOfVariables + "\nNumber of Nodes Before Reduction: " + bdd.numberOfNodesBeforeReduction + "\nNumber of Nodes After Reduction: " + bdd.numberOfNodesAfterReduction);
                        System.out.println("Reduction rate: " + String.format("%.4f", ((1 - ((double)bdd.numberOfNodesAfterReduction / bdd.numberOfNodesBeforeReduction)) * 100)) + "%\tDuration: " + Duration.between(startTime, endTime).toMillis() + " ms");
                        System.out.println("Use input: " + useIpnut + "\nOutput: " + output + "\n\n");
                    }
                    System.out.println("Average time: " + ((double) times / numberOfTests) + "ms\n\n");
                }
            }
        }

    }

    // BOOLEAN FUNCTION GENERATOR
    public static String generateBooleanFunction(int numberOfLetters) {
        Random random = new Random();

        StringBuilder letterBuilder = new StringBuilder();
        for (int i = 0; i < numberOfLetters; i++) {
            char letter = (char) (random.nextInt('Z' - 'A') + 'A');
            if(letterBuilder.toString().contains(String.valueOf(letter))) {
                i--;
            }
            else {
                letterBuilder.append(letter);
            }
        }

        String letters = letterBuilder.toString();

        StringBuilder booleanFunction = new StringBuilder();
        int numberOfExpressions = random.nextInt(20 - 1) + 1;
        for (int countOfExpression = 0; countOfExpression < numberOfExpressions; countOfExpression++) {
            StringBuilder expression = new StringBuilder();
            int lengthOfExpression = random.nextInt(numberOfLetters - 1) + 1;
            for (int length = 0; length < lengthOfExpression; length++) {
                int indexOfLetter =  random.nextInt(numberOfLetters);
                if (String.valueOf(expression).contains(String.valueOf(letters.charAt(indexOfLetter)))) {
                    length--;
                    continue;
                }
                if (random.nextBoolean()) expression.append("!");
                expression.append(letters.charAt(indexOfLetter));
                if (length < lengthOfExpression - 1) expression.append("*");
            }
            booleanFunction.append(expression);
            if (countOfExpression < numberOfExpressions - 1) booleanFunction.append("+");
        }
        return booleanFunction.toString();
    }

    //GETTING ALPHABETICAL ORDER OF BOOLEAN FUNCTION
    public static String getOrderOfBooleanFunction(String booleanFunction) {
        String order = "";

        for (int charValue = 65; charValue <= 90; charValue++) {
            String charToString = Character.toString((char) charValue);
            if(booleanFunction.contains(charToString)) order += charToString;
        }
        return order;
    }

    public static String generateUseFunction(String booleanFunction) {
        Random random = new Random();
        String order = getOrderOfBooleanFunction(booleanFunction);
        StringBuilder useFunctionBuilder = new StringBuilder();

        for (int index = 0; index < order.length(); index++) {
            useFunctionBuilder.append(random.nextInt(2));
        }
        return useFunctionBuilder.toString();
    }
}