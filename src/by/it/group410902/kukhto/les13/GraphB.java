package by.it.group410902.kukhto.les13;
import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();


        Map<String, List<String>> graph = new HashMap<>();//вершина+список соседей
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }


        System.out.println(hasCycle(graph) ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();//проработанные вершины
        Set<String> stack = new HashSet<>();//текущие вершины

        for (String node : graph.keySet()) {
            if (!visited.contains(node) && dfs(node, graph, visited, stack)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dfs(String node, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> stack) {
        if (stack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        stack.add(node);

        for (String neighbor : graph.get(node)) {
            if (dfs(neighbor, graph, visited, stack)) {
                return true;
            }
        }

        stack.remove(node);
        return false;
    }
}
