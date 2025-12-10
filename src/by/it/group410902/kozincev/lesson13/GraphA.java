package by.it.group410902.kozincev.lesson13;

import java.util.*;

public class GraphA {

    // Метод для парсинга входной строки и построения графа
    private static Map<String, Set<String>> parseGraph(String input, Map<String, Integer> inDegree, Set<String> allNodes) {
        Map<String, Set<String>> adj = new HashMap<>();

        // Формат: "A -> B, A -> C, B -> D, C -> D"
        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length == 2) {
                String u = parts[0]; // Начальная вершина (источник)
                String v = parts[1]; // Конечная вершина (цель)

                // Добавляем вершины во множество всех вершин
                allNodes.add(u);
                allNodes.add(v);

                // Инициализируем списки смежности для новых вершин
                adj.putIfAbsent(u, new HashSet<>());
                adj.putIfAbsent(v, new HashSet<>());

                // Проверяем, чтобы не добавлять дубликаты ребер (на случай, если в строке есть A->B, A->B)
                if (adj.get(u).add(v)) {
                    // Увеличиваем входящую степень для v
                    inDegree.put(v, inDegree.getOrDefault(v, 0) + 1);
                }
            }
        }

        // Убеждаемся, что все вершины есть в inDegree (даже те, у которых 0)
        for (String node : allNodes) {
            inDegree.putIfAbsent(node, 0);
        }

        return adj;
    }

    // Метод для выполнения лексикографической топологической сортировки
    public static String topologicalSort(String input) {
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> allNodes = new TreeSet<>(); // TreeSet для лексикографического порядка вершин

        Map<String, Set<String>> adj = parseGraph(input, inDegree, allNodes);

        // Используем PriorityQueue для хранения вершин с входящей степенью 0
        // Приоритетная очередь гарантирует лексикографический порядок (по возрастанию)
        PriorityQueue<String> pq = new PriorityQueue<>();

        // Инициализация очереди: добавляем все вершины с входящей степенью 0
        for (String node : allNodes) {
            if (inDegree.get(node) == 0) {
                pq.add(node);
            }
        }

        List<String> result = new ArrayList<>();

        // Алгоритм Кана
        while (!pq.isEmpty()) {
            String u = pq.poll(); // Извлекаем лексикографически наименьшую вершину
            result.add(u);

            // Обрабатываем всех соседей u
            if (adj.containsKey(u)) {
                for (String v : adj.get(u)) {
                    int newDegree = inDegree.get(v) - 1;
                    inDegree.put(v, newDegree);

                    // Если входящая степень соседа стала 0, добавляем его в очередь
                    if (newDegree == 0) {
                        pq.add(v);
                    }
                }
            }
        }

        // Проверка на цикл
        if (result.size() != allNodes.size()) {
            return "Graph has a cycle (Topological Sort not possible)";
        }

        // Форматирование результата
        return String.join(" ", result);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        scanner.close();

        String sortedOutput = topologicalSort(input);

        System.out.println(sortedOutput);
    }
}