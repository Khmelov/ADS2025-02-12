package by.it.group410902.kovalchuck.lesson01.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки для построения графа
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(", ");

        // Обработка каждого ребра графа
        for (String edge : edges) {
            // Разделяем ребро на начальную и конечную вершины
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            // Добавляем вершины в граф, если их еще нет
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            // Добавляем направленное ребро от начальной к конечной вершине
            graph.get(from).add(to);
        }

        // Проверка на наличие циклов в графе
        boolean hasCycle = hasCycle(graph);

        // Вывод результата: "yes" если есть цикл, "no" если нет циклов
        System.out.println(hasCycle ? "yes" : "no");
    }

    //Проверяет наличие циклов в ориентированном графе
    private static boolean hasCycle(Map<String, List<String>> graph) {
        // Словарь для отслеживания состояния вершин:
        // 0 - не посещена (WHITE)
        // 1 - в процессе обработки (GRAY)
        // 2 - обработка завершена (BLACK)
        Map<String, Integer> visited = new HashMap<>();

        // Инициализация всех вершин как непосещенных
        for (String node : graph.keySet()) {
            visited.put(node, 0);
        }

        // Запуск DFS для каждой непосещенной вершины
        for (String node : graph.keySet()) {
            if (visited.get(node) == 0) {
                if (dfs(node, graph, visited)) {
                    return true; // Найден цикл
                }
            }
        }

        return false; // Циклов не обнаружено
    }

    //Рекурсивная функция обхода в глубину (DFS) для обнаружения циклов
    private static boolean dfs(String node, Map<String, List<String>> graph, Map<String, Integer> visited) {
        visited.put(node, 1); // Помечаем вершину как "в процессе обработки" (GRAY)

        // Рекурсивно обрабатываем всех соседей текущей вершины
        for (String neighbor : graph.get(node)) {
            if (visited.get(neighbor) == 0) {
                // Если сосед не посещен, запускаем DFS для него
                if (dfs(neighbor, graph, visited)) {
                    return true; // Цикл обнаружен в поддереве
                }
            } else if (visited.get(neighbor) == 1) {
                // Если сосед находится "в процессе обработки" - это обратное ребро, означающее цикл
                return true; // Найден цикл!
            }
            // Если сосед уже полностью обработан (состояние 2), просто пропускаем
        }

        visited.put(node, 2); // Помечаем вершину как "обработка завершена" (BLACK)
        return false; // В этой ветке циклов не обнаружено
    }
}