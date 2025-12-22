package by.it.group410902.kozincev.lesson13;

import java.util.*;

public class GraphB {

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

                // Проверяем, чтобы не добавлять дубликаты ребер и корректно считать inDegree
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

    // Метод для проверки наличия циклов
    public static String hasCycle(String input) {
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> allNodes = new HashSet<>(); // HashSet достаточно, лексикографический порядок тут не нужен

        Map<String, Set<String>> adj = parseGraph(input, inDegree, allNodes);
        int totalNodes = allNodes.size();

        // Используем обычную очередь для Алгоритма Кана
        Queue<String> queue = new LinkedList<>();

        // Инициализация очереди: добавляем все вершины с входящей степенью 0
        for (String node : allNodes) {
            if (inDegree.get(node) == 0) {
                queue.add(node);
            }
        }

        int count = 0; // Счетчик вершин, добавленных в топологический порядок

        // Алгоритм Кана
        while (!queue.isEmpty()) {
            String u = queue.poll();
            count++; // Успешно добавлена в порядок

            // Обрабатываем всех соседей u
            if (adj.containsKey(u)) {
                for (String v : adj.get(u)) {
                    int newDegree = inDegree.get(v) - 1;
                    inDegree.put(v, newDegree);

                    // Если входящая степень соседа стала 0, добавляем его в очередь
                    if (newDegree == 0) {
                        queue.add(v);
                    }
                }
            }
        }

        // Проверка на цикл
        // Если количество обработанных вершин равно общему числу вершин, цикла нет.
        if (count == totalNodes) {
            return "no";
        } else {
            // Если count < totalNodes, оставшиеся вершины образуют цикл(ы).
            return "yes";
        }
    }

    public static void main(String[] args) {
        // Необходимо считывать строку структуры графа из стандартного ввода (System.in),
        // чтобы тесты работали корректно.
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            // Выводим результат в консоль, который будет перехвачен тестом
            String result = hasCycle(input);
            System.out.println(result);
        }

        scanner.close();
    }
}