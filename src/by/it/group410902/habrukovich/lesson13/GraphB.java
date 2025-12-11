package by.it.group410902.habrukovich.lesson13;
import java.util.*;
public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = parseGraph(input);

        boolean hasCycle = hasCycle(graph);
        System.out.println(hasCycle ? "yes" : "no");
        scanner.close();
    }
    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] vertices = edge.split("\\s*->\\s*"); // Разделяем по стрелке
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        return graph;
    }
    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();     // Все посещённые вершины
        Set<String> recStack = new HashSet<>();    // Вершины в текущем пути обхода (рекурсивном стеке)

        // Запускаем проверку для каждой вершины графа
        for (String vertex : graph.keySet()) {
            if (hasCycleUtil(vertex, graph, visited, recStack)) {
                return true; // Найден цикл
            }
        }
        return false; // Циклов не найдено
    }

    private static boolean hasCycleUtil(String vertex, Map<String, List<String>> graph,
                                        Set<String> visited, Set<String> recStack) {
        // Если вершина уже находится в текущем пути обхода - найден цикл!
        if (recStack.contains(vertex)) {
            return true;
        }
        if (visited.contains(vertex)) {
            return false;
        }
        // Помечаем вершину как посещённую и добавляем в текущий путь
        visited.add(vertex);
        recStack.add(vertex);
        // Рекурсивно проверяем всех соседей текущей вершины
        List<String> neighbors = graph.getOrDefault(vertex, new ArrayList<>());
        for (String neighbor : neighbors) {
            // Если в поддереве соседа найден цикл - возвращаем true
            if (hasCycleUtil(neighbor, graph, visited, recStack)) {
                return true;
            }
        }

        // Убираем вершину из текущего пути (backtracking)
        recStack.remove(vertex);
        return false;
    }
}
