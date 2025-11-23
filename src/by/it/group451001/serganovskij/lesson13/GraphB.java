package by.it.group451001.serganovskij.lesson13;
import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // читаем строку с рёбрами, например: A->B, B->C, C->A

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // Разбираем ввод и строим граф
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length < 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Добавляем вершины без исходящих рёбер
        for (String vertex : vertices) {
            graph.putIfAbsent(vertex, new ArrayList<>());
        }

        // Проверяем наличие цикла
        boolean hasCycle = hasCycle(graph, vertices);
        System.out.println(hasCycle ? "yes" : "no");
    }

    // Проверка, есть ли цикл в графе
    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> vertices) {
        Map<String, Integer> visited = new HashMap<>();
        // 0 — не посещена, 1 — в стеке вызовов, 2 — обработана
        for (String vertex : vertices) {
            visited.put(vertex, 0);
        }

        // Запускаем DFS для всех непосещённых вершин
        for (String vertex : vertices) {
            if (visited.get(vertex) == 0) {
                if (dfsHasCycle(vertex, graph, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Рекурсивный обход DFS с проверкой циклов
    private static boolean dfsHasCycle(String vertex, Map<String, List<String>> graph, Map<String, Integer> visited) {
        visited.put(vertex, 1); // помечаем как "в процессе"

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (visited.get(neighbor) == 0) {
                    // рекурсивный вызов
                    if (dfsHasCycle(neighbor, graph, visited)) {
                        return true;
                    }
                } else if (visited.get(neighbor) == 1) {
                    // найден цикл (возврат к вершине в стеке)
                    return true;
                }
            }
        }

        visited.put(vertex, 2); // завершена обработка вершины
        return false;
    }
}
