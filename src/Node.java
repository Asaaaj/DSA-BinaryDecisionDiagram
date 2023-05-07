public class Node {
    int level = 0;
    String variable;
    Node zero = null;
    Node one = null;

    public Node(String variable) {
        this.variable = variable;
    }

    public Node(String variable, int level) {
        this.variable = variable;
        this.level = level;
    }

    public Node(String variable, Node zero, Node one, int level) {
        this.variable = variable;
        this.zero = zero;
        this.one = one;
        this.level = level;
    }
}
