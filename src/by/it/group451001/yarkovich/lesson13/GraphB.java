package by.it.group451001.yarkovich.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String result = hasCycles(input) ? "yes" : "no";
        System.out.println(result);
        scanner.close();
    }

    // Алгоритм обнаружения циклов с помощью DFS:
    // 1. Используем два множества: visited (все посещенные вершины) и recursionStack (вершины в текущем пути обхода)
    // 2. Если при обходе встречаем вершину, которая уже в recursionStack - найден цикл
    // 3. После обработки всех потомков вершины убираем ее из recursionStack
    public static boolean hasCycles(String input) {
        Map<String, List<String>> graph = buildGraph(input);
        return hasCycle(graph);
    }

    private static Map<String, List<String>> buildGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",");

        for (String edge : edges) {
            edge = edge.trim();
            String[] nodes = edge.split("->");
            String from = nodes[0].trim();
            String to = nodes[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
            graph.putIfAbsent(to, new ArrayList<>());
        }

        return graph;
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfsCycleDetection(node, visited, recursionStack, graph)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Рекурсивный DFS для обнаружения циклов
    private static boolean dfsCycleDetection(String node, Set<String> visited, Set<String> recursionStack,
                                             Map<String, List<String>> graph) {
        if (recursionStack.contains(node)) {
            return true;
        }

        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recursionStack.add(node);

        for (String neighbor : graph.get(node)) {
            if (dfsCycleDetection(neighbor, visited, recursionStack, graph)) {
                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }
}