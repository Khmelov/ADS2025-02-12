package by.it.group410902.kukhto.les13;
import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();


        Map<String, List<String>> graph = new HashMap<>();//вершина+список соседей
        Map<String, List<String>> reverseGraph = new HashMap<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);

            reverseGraph.putIfAbsent(from, new ArrayList<>());
            reverseGraph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.get(to).add(from);
        }

        // алгоритм Косарайа
        List<Set<String>> components = kosaraju(graph, reverseGraph);

        // графы компоненты
        Map<Set<String>, List<Set<String>>> condensation = buildCondensation(graph, components);

        // Топологическая сортировка компонент
        List<Set<String>> sortedComponents = topologicalSortComponents(condensation);


        for (Set<String> component : sortedComponents) {
            List<String> sortedVertices = new ArrayList<>(component);
            Collections.sort(sortedVertices);
            for (String vertex : sortedVertices) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static List<Set<String>> kosaraju(Map<String, List<String>> graph,
                                              Map<String, List<String>> reverseGraph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        // первый проход DFS
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        // второй проход DFS
        visited.clear();
        List<Set<String>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                Set<String> component = new HashSet<>();
                dfsSecondPass(vertex, reverseGraph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }
        stack.push(vertex);
    }

    private static void dfsSecondPass(String vertex, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, Set<String> component) {
        visited.add(vertex);
        component.add(vertex);
        for (String neighbor : reverseGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, reverseGraph, visited, component);
            }
        }
    }

    private static Map<Set<String>, List<Set<String>>> buildCondensation(
            Map<String, List<String>> graph, List<Set<String>> components) {

        Map<Set<String>, List<Set<String>>> condensation = new HashMap<>();
        Map<String, Set<String>> vertexToComponent = new HashMap<>();

        // сопоставляем каждой вершине её компоненту
        for (Set<String> component : components) {
            condensation.put(component, new ArrayList<>());
            for (String vertex : component) {
                vertexToComponent.put(vertex, component);
            }
        }

        // построение ребер
        for (String from : graph.keySet()) {
            Set<String> fromComponent = vertexToComponent.get(from);
            for (String to : graph.get(from)) {
                Set<String> toComponent = vertexToComponent.get(to);
                if (!fromComponent.equals(toComponent) &&
                        !condensation.get(fromComponent).contains(toComponent)) {
                    condensation.get(fromComponent).add(toComponent);
                }
            }
        }

        return condensation;
    }

    private static List<Set<String>> topologicalSortComponents(
            Map<Set<String>, List<Set<String>>> condensation) {

        Map<Set<String>, Integer> inDegree = new HashMap<>();
        for (Set<String> component : condensation.keySet()) {
            inDegree.put(component, 0);
        }


        for (Set<String> from : condensation.keySet()) {
            for (Set<String> to : condensation.get(from)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        // Топологическая сортировка
        List<Set<String>> result = new ArrayList<>();
        Queue<Set<String>> queue = new LinkedList<>();

        for (Set<String> component : condensation.keySet()) {
            if (inDegree.get(component) == 0) {
                queue.add(component);
            }
        }

        while (!queue.isEmpty()) {
            Set<String> current = queue.poll();
            result.add(current);

            for (Set<String> neighbor : condensation.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return result;
    }
}