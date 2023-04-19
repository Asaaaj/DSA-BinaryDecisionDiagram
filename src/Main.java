public class Main {
    public static void main(String[] args) {
        System.out.println("DSA - Zadanie 2");

        BDD bdd = new BDD();
        String bf = "A+A*!B*C+!A*C+A+A*B+C*B+A*B";
        String bff = "A*B+A*C+B*C";
        String order = "ABC";
        if(isBooleanFunctionCorrect(bf)) {
            System.out.println("DNF: "  + bf + " is correct.");
            bdd.BDD_create(bf, order);
            BDD bdd1 = new BDD();
            bdd1.BDD_create_with_best_order(bf);
            BDD bdd2 = new BDD();
            System.out.println("BDD use: " + bdd2.BDD_use(bdd, "001"));
        }
        else System.out.println("DNF: "  + bf + " is incorrect.");

    }
    // CHECK IF BOOLEAN FUNCTION IS CORRECT
    public static boolean isBooleanFunctionCorrect(String bf) {
        return bf != null && !bf.isEmpty() && !bf.matches("[^A-Z&&^\\Q*\\E^\\Q+\\E^\\Q!\\E]");
    }
}