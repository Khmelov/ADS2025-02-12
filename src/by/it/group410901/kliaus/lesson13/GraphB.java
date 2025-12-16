package by.it.group410901.kliaus.lesson13;

import java.util.*;

public class GraphB {

    private static Map<String, List<String>> graph;
    private static Map<String, Integer> color; // 0 - белый, 1 - серый, 2 - черный
    private static boolean hasCycle;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        graph = new HashMap<>();
        color = new HashMap<>();
        hasCycle = false;

        Set<String> allNodes = new HashSet<>();

        // Парсинг входных данных
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            allNodes.add(from);
            allNodes.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
        }

        // Инициализация цветов (все вершины белые)
        for (String node : allNodes) {
            color.put(node, 0);
        }

        // DFS для поиска цикла
        for (String node : allNodes) {
            if (color.get(node) == 0) {
                dfs(node);
                if (hasCycle) {
                    break;
                }
            }
        }

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static void dfs(String node) {
        if (hasCycle) {
            return;
        }

        color.put(node, 1); // Помечаем серым (в процессе обработки)

        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (color.get(neighbor) == 1) {
                    // Нашли обратное ребро - есть цикл
                    hasCycle = true;
                    return;
                } else if (color.get(neighbor) == 0) {
                    dfs(neighbor);
                }
            }
        }

        color.put(node, 2); // Помечаем черным (обработан)
    }
}