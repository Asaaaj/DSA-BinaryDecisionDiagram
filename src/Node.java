public class Node {
    String variable;
    Node zero = null;
    Node one = null;

    public Node(String variable) {
        this.variable = variable;
    }

    public Node(String variable, Node zero, Node one) {
        this.variable = variable;
        this.zero = zero;
        this.one = one;
    }
}
