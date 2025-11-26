package by.it.group410901.getmanchuk.lesson13;

import java.util.*;

// Топологическая сортировка графа (алгоритм Кана с приоритетной очередью)

public class GraphA {

    public static void main(String[] args) {

        // Чтение входной строки с рёбрами графа (например: "A -> B, B -> C")
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Список смежности для хранения графа
        Map<String, List<String>> graph = new HashMap<>();

        // Множество всех вершин (включая те, у которых нет исходящих рёбер)
        Set<String> vertices = new HashSet<>();

        // Разделение входной строки на отдельные рёбра
        String[] edges = input.split(", ");

        // Построение графа по рёбрам формата "A -> B"
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Выполнение топологической сортировки
        List<String> result = topologicalSort(graph, vertices);

        // Вывод результата через пробел
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }

    // Метод выполняет топологическую сортировку по алгоритму Кана
    private static List<String> topologicalSort(Map<String, List<String>> graph, Set<String> vertices) {
        // Список для хранения результата сортировки
        List<String> sorted = new ArrayList<>();

        // Подсчёт входящих рёбер (in-degree) для каждой вершины
        Map<String, Integer> inDegree = new HashMap<>();
        for (String vertex : vertices)
            inDegree.put(vertex, 0);

        // Увеличение входящей степени для всех вершин, в которые входят рёбра
        for (List<String> neighbors : graph.values()) {
            for (String neighbor : neighbors)
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
        }

        // Приоритетная очередь для вершин с нулевой входящей степенью
        // Вершины автоматически сортируются по алфавиту
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавление в очередь всех вершин без входящих рёбер
        for (String vertex : vertices) {
            if (inDegree.get(vertex) == 0)
                queue.offer(vertex);
        }

        // Основной цикл алгоритма Кана
        while (!queue.isEmpty()) {
            // Извлечение вершины с минимальным именем
            String current = queue.poll();
            sorted.add(current);

            // Уменьшение входящей степени всех соседей текущей вершины
            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);

                    // Если входящих рёбер больше нет — добавить вершину в очередь
                    if (inDegree.get(neighbor) == 0)
                        queue.offer(neighbor);
                }
            }
        }

        // Возврат результата сортировки
        return sorted;
    }
}