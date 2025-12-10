package by.it.group451002.gorbach.lesson13;

import java.util.*;

public class GraphC {

    private Map<String, List<String>> graph;
    private Map<String, List<String>> reversedGraph;
    private Set<String> visited;
    private List<String> order;
    private List<List<String>> components;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        GraphC graphC = new GraphC();
        graphC.parseInput(input);
        List<List<String>> scc = graphC.findStronglyConnectedComponents();

        for (List<String> component : scc) {
            Collections.sort(component);
            System.out.println(String.join("", component));
        }
    }

    public void parseInput(String input) {
        graph = new HashMap<>();
        reversedGraph = new HashMap<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0];
            String to = parts[1];

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>());

            reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            reversedGraph.putIfAbsent(from, new ArrayList<>());
        }
    }

    public List<List<String>> findStronglyConnectedComponents() {
        visited = new HashSet<>();
        order = new ArrayList<>();

        List<String> allVertices = new ArrayList<>(graph.keySet());
        Collections.sort(allVertices);

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfs1(vertex);
            }
        }

        visited.clear();
        components = new ArrayList<>();

        Collections.reverse(order);
        for (String vertex : order) {
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs2(vertex, component);
                components.add(component);
            }
        }

        sortComponentsByTopologicalOrder();

        return components;
    }

    private void dfs1(String vertex) {
        visited.add(vertex);

        List<String> neighbors = new ArrayList<>(graph.get(vertex));
        Collections.sort(neighbors);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor);
            }
        }

        order.add(vertex);
    }

    private void dfs2(String vertex, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        List<String> neighbors = new ArrayList<>(reversedGraph.get(vertex));
        Collections.sort(neighbors);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, component);
            }
        }
    }

    private void sortComponentsByTopologicalOrder() {
        Map<List<String>, List<List<String>>> condensationGraph = new HashMap<>();
        Map<List<String>, Integer> componentIndex = new HashMap<>();

        Map<String, List<String>> vertexToComponent = new HashMap<>();
        for (List<String> component : components) {
            for (String vertex : component) {
                vertexToComponent.put(vertex, component);
            }
            componentIndex.put(component, components.indexOf(component));
            condensationGraph.put(component, new ArrayList<>());
        }

        for (List<String> component : components) {
            for (String vertex : component) {
                for (String neighbor : graph.get(vertex)) {
                    List<String> neighborComponent = vertexToComponent.get(neighbor);
                    if (component != neighborComponent &&
                            !condensationGraph.get(component).contains(neighborComponent)) {
                        condensationGraph.get(component).add(neighborComponent);
                    }
                }
            }
        }

        Map<List<String>, Integer> inDegree = new HashMap<>();
        for (List<String> component : components) {
            inDegree.put(component, 0);
        }

        for (List<String> component : components) {
            for (List<String> neighbor : condensationGraph.get(component)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        components.sort((comp1, comp2) -> {
            int inDegree1 = inDegree.get(comp1);
            int inDegree2 = inDegree.get(comp2);
            if (inDegree1 != inDegree2) {
                return Integer.compare(inDegree1, inDegree2);
            }

            Collections.sort(comp1);
            Collections.sort(comp2);
            return comp1.get(0).compareTo(comp2.get(0));
        });
    }
}