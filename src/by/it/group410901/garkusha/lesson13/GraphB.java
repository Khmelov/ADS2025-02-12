package by.it.group410901.garkusha.lesson13;
import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Инициализируем списки смежности
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
        }

        boolean hasCycle = hasCycle(graph);

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>(); // Вершины, находящиеся в текущем пути обхода

        // Проверяем каждую вершину на наличие циклов
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (dfs(vertex, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(String current, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> recursionStack) {
        // Если вершина уже в текущем стеке рекурсии - найден цикл
        if (recursionStack.contains(current)) {
            return true;
        }

        // Если вершина уже полностью обработана - цикла нет
        if (visited.contains(current)) {
            return false;
        }

        visited.add(current);
        recursionStack.add(current);

        // Рекурсивно проверяем всех соседей
        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (dfs(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        recursionStack.remove(current);

        return false;
    }
}
