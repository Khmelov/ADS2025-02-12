package by.it.group451002.sidarchuk.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Читаем строку с описанием связей между вершинами
        // Пример: "0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1"
        String input = scanner.nextLine();

        // Граф: для каждой вершины храним список вершин, в которые идут стрелки
        Map<String, List<String>> graph = new HashMap<>();
        // Счетчик "входящих стрелок": сколько стрелок входит в каждую вершину
        Map<String, Integer> inDegree = new HashMap<>();

        // Разделяем входную строку на отдельные стрелки
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разделяем каждую стрелку на "откуда" и "куда"
            String[] vertices = edge.split(" -> ");
            String from = vertices[0];  // вершина, от которой идет стрелка
            String to = vertices[1];    // вершина, в которую идет стрелка

            // Добавляем связь в граф: from -> to
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Убеждаемся, что обе вершины есть в нашем счетчике
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);
        }

        // Подсчитываем для каждой вершины, сколько стрелок в нее входит
        for (List<String> neighbors : graph.values()) {
            for (String neighbor : neighbors) {
                // Увеличиваем счетчик входящих стрелок для вершины-соседа
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // Выполняем топологическую сортировку
        List<String> result = topologicalSort(graph, inDegree);

        // Выводим результат в нужном формате
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>(); // Здесь будем хранить итоговый порядок

        // Очередь с приоритетом: чтобы при равных условиях вершины шли по алфавиту
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Находим все вершины, в которые НЕ входят стрелки (можно начинать с них)
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        // Пока есть вершины, которые можно обработать
        while (!queue.isEmpty()) {
            // Берем вершину из очереди (с наименьшим номером/именем)
            String current = queue.poll();
            result.add(current); // Добавляем ее в результат

            // Если из текущей вершины есть исходящие стрелки
            if (graph.containsKey(current)) {
                // Берем всех соседей и сортируем их для сохранения порядка
                List<String> neighbors = new ArrayList<>(graph.get(current));
                Collections.sort(neighbors);

                // Для каждого соседа текущей вершины
                for (String neighbor : neighbors) {
                    // Уменьшаем счетчик входящих стрелок для соседа
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);

                    // Если у соседа больше нет входящих стрелок, добавляем в очередь
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return result;
    }
}