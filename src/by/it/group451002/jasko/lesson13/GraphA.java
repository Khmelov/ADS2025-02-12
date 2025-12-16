package by.it.group451002.jasko.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки с ребрами графа
        Map<String, List<String>> graph = new HashMap<>(); // Граф как список смежности
        Map<String, Integer> inDegree = new HashMap<>();  // Входящие степени вершин
        Set<String> allVertices = new HashSet<>();        // Все вершины графа

        // Разбиваем входную строку на отдельные ребра
        String[] edges = input.split(",\\s*");

        // Обрабатываем каждое ребро формата "вершина1 -> вершина2"
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim(); // Начальная вершина
            String to = parts[1].trim();   // Конечная вершина

            // Добавляем вершины в множество
            allVertices.add(from);
            allVertices.add(to);

            // Строим граф - добавляем ребро from -> to
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            // Обновляем входящие степени: to получает +1 входящее ребро
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            // Убеждаемся, что from есть в карте входящих степеней
            inDegree.putIfAbsent(from, 0);
        }

        // Добавляем вершины без исходящих ребер в граф
        for (String vertex : allVertices) {
            graph.putIfAbsent(vertex, new ArrayList<>());
            inDegree.putIfAbsent(vertex, 0);
        }

        // Топологическая сортировка алгоритмом Кана
        List<String> result = new ArrayList<>();
        PriorityQueue<String> queue = new PriorityQueue<>(); // Для лексикографического порядка

        // Начинаем с вершин, у которых нет входящих ребер
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        // Основной цикл алгоритма
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            // Уменьшаем входящие степени всех соседей
            for (String neighbor : graph.get(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // Если степень стала 0 - добавляем в очередь
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Вывод результата
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
        System.out.println();
    }
}