package by.it.group451004.struts.lesson13;

import java.util.ArrayList;

public class Node {
    public Node(String name) {
        this.name = name;
    }

    String name;
    ArrayList<Node> ways = new ArrayList<>();
    public ArrayList<Node> reversedWays = new ArrayList<>();
    public boolean visited = false;
}
