package by.it.group410901.bandarzheuskaya.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        // Считываем входную строку с описанием орграфа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Инициализируем структуры данных:

        Map<String, List<String>> graph = new HashMap<>();// список смежности, хранит для каждой вершины список её соседей
        Map<String, Integer> inDegree = new HashMap<>(); // хранит количество входящих рёбер для каждой вершины

        // Разбиваем входную строку на части (рёбра или изолированные вершины)
        String[] parts = input.split(", ");
        for (String part : parts) {
            // Проверяем, является ли часть ребром
            if (part.contains("->")) {
                // Разбиваем ребро на источник и приёмник
                String[] edge = part.split("->");
                String from = edge[0].trim();
                String to = edge[1].trim();
                // Добавляем ребро в граф
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                // Инициализируем степени захода для обеих вершин
                inDegree.putIfAbsent(from, 0);
                inDegree.putIfAbsent(to, 0);
            } else {
                // Если часть - изолированная вершина, добавляем её в граф и inDegree
                String vertex = part.trim();
                graph.putIfAbsent(vertex, new ArrayList<>());
                inDegree.putIfAbsent(vertex, 0);
            }
        }

        // Вычисляем степени захода для всех вершин
        for (List<String> neighbors : graph.values()) {
            // Для каждого соседа увеличиваем inDegree на 1
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // Выполняем топологическую сортировку алгоритмом Кана
        List<String> result = topologicalSort(graph, inDegree);

        // Выводим результат: вершины через пробел
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }


    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем в очередь все вершины с нулевой степенью захода
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        // Основной цикл алгоритма Кана
        while (!queue.isEmpty()) {
            // Извлекаем вершину с наименьшим лексикографическим значением
            String current = queue.poll();
            // Добавляем её в результат
            result.add(current);

            // Если у вершины есть исходящие рёбра
            if (graph.containsKey(current)) {
                // Получаем и сортируем соседей для обработки в лексикографическом порядке
                List<String> neighbors = graph.get(current);
                Collections.sort(neighbors);

                // Для каждого соседа уменьшаем степень захода
                for (String neighbor : neighbors) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    // Если степень захода стала 0, добавляем соседа в очередь
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return result;
    }
}