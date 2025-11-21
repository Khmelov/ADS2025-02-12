package by.it.group410902.plekhova.lesson13;

import java.util.*;
// проверить наличие циклов
public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) {
            System.out.println("no");
            sc.close();
            return;
        }
        String line = sc.nextLine().trim();
        sc.close();

        // Пустая строка — нет рёбер, значит цикла нет
        if (line.isEmpty()) {
            System.out.println("no");
            return;
        }

        // Разбор рёбер. Ожидаем формат: "A -> B, C -> D, ..."
        Map<String, List<String>> graph = new HashMap<>();

        String[] parts = line.split(",");
        for (String p : parts) {
            String edge = p.trim();
            if (edge.isEmpty()) continue;
            // Разделяем по "->" с любыми пробелами вокруг
            String[] nodes = edge.split("\\s*->\\s*");
            if (nodes.length != 2) {
                // некорректная часть — пропускаем
                continue;
            }
            String from = nodes[0].trim();
            String to = nodes[1].trim();
            if (from.isEmpty() || to.isEmpty()) continue;

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // добавим целевую вершину в map, чтобы учесть изолированные вершины как ключи
            graph.putIfAbsent(to, new ArrayList<>());
        }

        boolean hasCycle = hasCycle(graph);
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        // 0 = непосещена, 1 = посещена (находится в стеке), 2 = обработана
        Map<String, Integer> color = new HashMap<>();
        for (String node : graph.keySet()) {
            // Если вершина ещё не посещалась — запускаем DFS
            if (color.getOrDefault(node, 0) == 0) {
                if (dfs(node, graph, color)) return true; // если вершина в стеке, и мы снова к ней пришли - цикл
            }
        }
        return false; // циклов нет
    }

    private static boolean dfs(String u, Map<String, List<String>> graph, Map<String, Integer> color) {
        color.put(u, 1); // помещаем вершину в стек
        for (String v : graph.getOrDefault(u, Collections.emptyList())) { // проверяем всех соседей
            int c = color.getOrDefault(v, 0);
            if (c == 1) return true; // найдено ребро в стеке — цикл
            if (c == 0) {
                if (dfs(v, graph, color)) return true;
            }
        }
        color.put(u, 2);
        return false;
    }
}