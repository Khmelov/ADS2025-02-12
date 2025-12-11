package by.it.group410901.danilova.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> allVertices = new TreeSet<>(); // TreeSet для автоматической сортировки

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");
            for (String edge : edges) {
                String[] parts = edge.split("\\s*->\\s*");
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Инициализация степеней входа
        for (String vertex : allVertices) {
            inDegree.put(vertex, 0); // Изначально у всех 0 входящих стрелок
        }

        // Вычисление степеней входа
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.get(to) + 1); // Увеличиваем счетчик для конечной вершины
            }
        }

        // Топологическая сортировка алгоритмом Кана
        List<String> result = new ArrayList<>();
        PriorityQueue<String> queue = new PriorityQueue<>(); // Min-heap для лексикографического порядка

        // Добавляем вершины с нулевой степенью входа
        for (String vertex : allVertices) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex); // Добавляем вершины без входящих стрелок
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll(); // Берем вершину из очереди
            result.add(current);  // Добавляем в результат

            if (graph.containsKey(current)) {
                // Сортируем соседей для детерминированного порядка
                List<String> neighbors = new ArrayList<>(graph.get(current));
                Collections.sort(neighbors);

                for (String neighbor : neighbors) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1); // Убираем текущую стрелку

                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor); // Если больше нет входящих - добавляем в очередь
                    }
                }
            }
        }

        // Вывод результата
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(result.get(i));
        }
    }
}