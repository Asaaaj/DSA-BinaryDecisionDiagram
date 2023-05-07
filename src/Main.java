import java.time.Duration;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("DSA - Zadanie 2");

        BDD bdd = new BDD();
        String bf = "A+A*!B*C+!A*C+A+A*B+C*B+A*B";
        String bff = "A*B+A*C+B*C";
        // !W+C+A+Y*!H+!V+!B*H+B*V+I*H+X*D+B*S+P+!Y+!X+D*B+J+!L*!E+R*!H+!K+!R*B+A*D+!D+!H+X*I+!R+J+!O+K+!P*G+!H+K+!U*!J+!E+K*E
        String order = "ABC";
        if(isBooleanFunctionCorrect(bf)) {
            System.out.println("DNF: "  + bf + " is correct.");
            bdd.BDD_create("A+A*!B*C+!A*C+A+A*B+C*B+A*B", "ABC");
            System.out.println("Before reduction: " + bdd.numberOfNodesBeforeReduction + " After reduction: " + bdd.numberOfNodesAfterReduction);
//            BDD bdd1 = new BDD();
//            bdd1.BDD_create_with_best_order(bf);
            BDD bdd2 = new BDD();
            System.out.println("BDD use: " + bdd2.BDD_use(bdd, "001"));

            for (int i = 0; i < 10; i++) {
                System.out.println("i: " + i + " : " + generateBooleanFunction(3));
            }
        }
        else System.out.println("DNF: "  + bf + " is incorrect.");

    }
    // CHECK IF BOOLEAN FUNCTION IS CORRECT
    public static boolean isBooleanFunctionCorrect(String bf) {
        return bf != null && !bf.isEmpty() && !bf.matches("[^A-Z&&^\\Q*\\E^\\Q+\\E^\\Q!\\E]");
    }

    public static String generateBooleanFunction(int numberOfLetters) {
        Random random = new Random();
        StringBuilder booleanFunction = new StringBuilder();
        int numberOfExpressions = random.nextInt(50 - 1) + 1;
        for (int countOfExpression = 0; countOfExpression < numberOfExpressions; countOfExpression++) {
            StringBuilder expression = new StringBuilder();
            int lengthOfExpression = random.nextInt(numberOfLetters - 1) + 1;
            for (int length = 0; length < lengthOfExpression; length++) {
                char letter = (char) (random.nextInt('Z' - 'A') + 'A');
                if (random.nextBoolean()) expression.append("!");
                expression.append(letter);
                if (length < lengthOfExpression - 1) expression.append("*");
            }
            booleanFunction.append(expression);
            if (countOfExpression < numberOfExpressions - 1) booleanFunction.append("+");
        }
        return booleanFunction.toString();
    }

    public static String getOrderOfBooleanFunction(String booleanFunction) {
        String order = "";

        for (int charValue = 65; charValue <= 90; charValue++) {
            String charToString = Character.toString((char) charValue);
            if(booleanFunction.contains(charToString)) order += charToString;
        }
        return order;
    }
}