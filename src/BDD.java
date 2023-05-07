import java.util.*;
import java.util.StringJoiner;

public class BDD {
    int numberOfVariables = 0;
    int numberOfNodesBeforeReduction = 0;
    int numberOfNodesAfterReduction = 0;
    public Node root = null;
    HashMap<String, Node> allNodes = new HashMap<>();

    public Node BDD_create(String booleanFunction, String order) {
        if(root == null) {
            root = new Node(booleanFunction, 0);
            allNodes.put(booleanFunction, root);
            numberOfNodesAfterReduction++;
        }
        else return root;

        root = BDD_recursive(root, booleanFunction, 0, order);
        numberOfVariables = order.length();
        numberOfNodesBeforeReduction = (int) Math.pow(2, numberOfVariables + 1) - 1;
        numberOfNodesAfterReduction = allNodes.size();
        return root;
    }

    private Node BDD_recursive(Node currentNode, String booleanFunction, int level, String order) {
        Node zeroNode = new Node("0");
        Node oneNode = new Node("1");
        System.out.println("Boolean function: " + booleanFunction);
        if (booleanFunction.equals("1")) {
            allNodes.putIfAbsent("1", oneNode);
            return allNodes.get("1");
        }
        else if (booleanFunction.equals("0")) {
            allNodes.putIfAbsent("0", zeroNode);
            return allNodes.get("0");
        }

        if (!(booleanFunction.contains(String.valueOf(order.charAt(level))) || booleanFunction.contains("!" + order.charAt(level))))  level++;
        String negativeLetter = "!" + order.charAt(level);
        System.out.println(negativeLetter);
        booleanFunction = booleanFunction.replaceAll(negativeLetter, "0");
        booleanFunction = booleanFunction.replaceAll(String.valueOf(order.charAt(level)), "1");
        System.out.println(booleanFunction);

        String[] expressions = booleanFunction.split("\\+");

        StringJoiner oneExpressionsJoiner = new StringJoiner("+");
        StringJoiner zeroExpressionsJoiner = new StringJoiner("+");
        for (String expression : expressions) {
            if(expression.contains("1"))oneExpressionsJoiner.add(expression);
            else if(expression.contains("0")) zeroExpressionsJoiner.add(expression);
            else {
                oneExpressionsJoiner.add(expression);
                zeroExpressionsJoiner.add(expression);
            }
        }
        String oneExpressions = oneExpressionsJoiner.toString();
        String zeroExpressions = zeroExpressionsJoiner.toString();

        oneExpressions = oneExpressions.replaceAll("[1][*]|[*][1]|[+][1]$|[1][+]", "");
        zeroExpressions = zeroExpressions.replaceAll("[0][*]|[*][0]|[+][0]$|[0][+]", "");

        List<String> oneArrayList = Arrays.asList(oneExpressions.split("\\+"));
        ArrayList<String> oneNoDuplicatesList = new ArrayList<>();
        for(String expression : oneArrayList) {
            if(!oneNoDuplicatesList.contains(expression)) oneNoDuplicatesList.add(expression);
        }
        List<String> zeroArrayList = Arrays.asList(zeroExpressions.split("\\+"));
        ArrayList<String> zeroNoDuplicatesList = new ArrayList<>();
        for(String expression : zeroArrayList) {
            if(!zeroNoDuplicatesList.contains(expression)) zeroNoDuplicatesList.add(expression);
        }

        oneExpressionsJoiner = new StringJoiner("+");
        zeroExpressionsJoiner = new StringJoiner("+");

        for (String expression : oneNoDuplicatesList) {
            oneExpressionsJoiner.add(expression);
        }
        for (String expression : zeroNoDuplicatesList) {
            zeroExpressionsJoiner.add(expression);
        }

        oneExpressions = oneExpressionsJoiner.toString();
        zeroExpressions = zeroExpressionsJoiner.toString();

        if (oneExpressions.equals("")) oneExpressions = "0";
        if (zeroExpressions.equals("")) zeroExpressions = "0";


        if (allNodes.get(oneExpressions) == null) {
            allNodes.put(oneExpressions, new Node(oneExpressions, level + 1));
            currentNode.one = allNodes.get(oneExpressions);
            currentNode.one = BDD_recursive(currentNode.one, oneExpressions, level + 1, order);
        } else if (allNodes.get(oneExpressions) != null) {
            currentNode.one = allNodes.get(oneExpressions);
        }

        if (allNodes.get(zeroExpressions) == null) {
            allNodes.put(zeroExpressions, new Node(zeroExpressions, level + 1));
            currentNode.zero = allNodes.get(zeroExpressions);
            currentNode.zero = BDD_recursive(currentNode.zero, zeroExpressions, level + 1, order);
        } else if (allNodes.get(zeroExpressions) != null) {
            currentNode.zero = allNodes.get(zeroExpressions);
        }



        if ((currentNode.one == currentNode.zero) && (currentNode.one != allNodes.get("0") || currentNode.one != allNodes.get("1"))) {
            allNodes.remove(currentNode.one.variable);
            currentNode.one = currentNode.one.one;
            currentNode.zero = currentNode.zero.zero;
        }

        return currentNode;
    }

    public Node BDD_create_with_best_order(String booleanFunction) {
        HashMap<Integer, BDD> allBDDs = new HashMap<>();
        List<Integer> keys = new ArrayList<>();
        String order = "";

        for (int charValue = 65; charValue <= 90; charValue++) {
            String charToString = Character.toString((char) charValue);
            if(booleanFunction.contains(charToString)) order += charToString;
        }

        for (int numberOfVariables = 0; numberOfVariables < order.length(); numberOfVariables++) {
            System.out.println("Order: " + order);
            BDD newBDD = new BDD();
            newBDD.BDD_create(booleanFunction, order);
            allBDDs.put(newBDD.numberOfNodesAfterReduction, newBDD);
            keys.add(newBDD.numberOfNodesAfterReduction);

            char firstLetter = order.charAt(0);
            order = order.substring(1) + firstLetter;
        }
        return allBDDs.get(Collections.min(keys)).root;
    }

    public String BDD_use(BDD bdd, String input) {
        Node currentNode = bdd.root;
        int currentInputPosition = 0;

        for (char number : input.toCharArray()) {
            if (currentNode.variable.equals("1")) return "1";
            else if (currentNode.variable.equals("0")) return "0";
            if (currentNode.level > currentInputPosition) continue;
            if (number == '1') currentNode = currentNode.one;
            else if (number == '0') currentNode = currentNode.zero;
            currentInputPosition++;
        }
        return "-1";
    }

    private void resetBDD() {
        root = null;
        numberOfNodesAfterReduction = 0;
        numberOfNodesBeforeReduction = 0;
        allNodes = new HashMap<>();
    }
}
