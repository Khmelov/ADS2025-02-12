package by.it.group451004.struts.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph(scanner.nextLine());

        System.out.println(graph.topologicalSort());
    }
}
