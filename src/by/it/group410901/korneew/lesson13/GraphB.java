package by.it.group410901.korneew.lesson13;
import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки и построение графа
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(", ");
            for (String edge : edges) {
                String[] parts = edge.split(" -> ");
                String from = parts[0];
                String to = parts[1];

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Проверка на наличие циклов с помощью DFS
        boolean hasCycle = false;
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                if (hasCycleDFS(vertex, graph, visited, recursionStack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycleDFS(String vertex, Map<String, List<String>> graph,
                                       Set<String> visited, Set<String> recursionStack) {
        // Если вершина уже в стеке рекурсии - найден цикл
        if (recursionStack.contains(vertex)) {
            return true;
        }

        // Если вершина уже посещена и обработана - цикла нет
        if (visited.contains(vertex)) {
            return false;
        }

        // Помечаем вершину как посещенную и добавляем в стек рекурсии
        visited.add(vertex);
        recursionStack.add(vertex);

        // Рекурсивно проверяем всех соседей
        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (hasCycleDFS(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        // Убираем вершину из стека рекурсии (backtracking)
        recursionStack.remove(vertex);
        return false;
    }
}
