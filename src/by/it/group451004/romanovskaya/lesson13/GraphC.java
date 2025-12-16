package by.it.group451004.romanovskaya.lesson13;

import java.util.*;

public class GraphC {
    private Map<Character, List<Character>> graph;
    private Map<Character, List<Character>> reversedGraph;
    private Set<Character> visited;
    private List<Character> order;
    private List<List<Character>> components;

    public GraphC() {
        graph = new HashMap<>();
        reversedGraph = new HashMap<>();
        visited = new HashSet<>();
        order = new ArrayList<>();
        components = new ArrayList<>();
    }

    public void addEdge(char from, char to) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
    }

    public void buildGraph(String input) {
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] nodes = edge.split("->");
            char from = nodes[0].charAt(0);
            char to = nodes[1].charAt(0);
            addEdge(from, to);
        }
    }

    public void dfs(char node) {
        if (!visited.contains(node)) {
            visited.add(node);
            for (char neighbor : graph.getOrDefault(node, Collections.emptyList())) {
                dfs(neighbor);
            }
            order.add(node);
        }
    }

    public void dfsReversed(char node, List<Character> component) {
        if (!visited.contains(node)) {
            visited.add(node);
            component.add(node);
            for (char neighbor : reversedGraph.getOrDefault(node, Collections.emptyList())) {
                dfsReversed(neighbor, component);
            }
        }
    }

    public void findStronglyConnectedComponents() {
        visited.clear();
        order.clear();
        components.clear();

        for (char node : graph.keySet()) {
            dfs(node);
        }

        visited.clear();
        Collections.reverse(order);

        for (char node : order) {
            if (!visited.contains(node)) {
                List<Character> component = new ArrayList<>();
                dfsReversed(node, component);
                Collections.sort(component);
                components.add(component);
            }
        }
    }

    public void printComponents() {
        for (List<Character> component : components) {
            for (char node : component) {
                System.out.print(node);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        GraphC graphC = new GraphC();
        graphC.buildGraph(input);
        graphC.findStronglyConnectedComponents();
        graphC.printComponents();
    }

}
