package by.it.group451002.mishchenko.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки с ребрами графа
        Map<String, List<String>> graph = new HashMap<>(); // Граф как список смежности
        String[] edges = input.split(", ");

        // Обрабатываем каждое ребро формата "вершина1 -> вершина2"
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0]; // Начальная вершина
            String to = parts[1];   // Конечная вершина

            // Добавляем вершины в граф (если их еще нет)
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            // Добавляем ребро from -> to
            graph.get(from).add(to);
        }

        // Проверка на наличие циклов в графе
        boolean hasCycle = hasCycle(graph);

        // Вывод результата: "yes" если есть цикл, "no" если нет
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();     // Посещенные вершины
        Set<String> recursionStack = new HashSet<>(); // Вершины в текущем пути DFS

        // Получаем все вершины графа
        List<String> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices); // Сортируем для детерминированного поведения

        // Проверяем каждую вершину на наличие циклов
        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                // Запускаем DFS из непосещенной вершины
                if (dfsHasCycle(vertex, graph, visited, recursionStack)) {
                    return true; // Найден цикл
                }
            }
        }

        return false; // Циклов не найдено
    }

    private static boolean dfsHasCycle(String vertex, Map<String, List<String>> graph,
                                       Set<String> visited, Set<String> recursionStack) {
        // Если вершина уже в текущем пути - найден цикл
        if (recursionStack.contains(vertex)) return true;
        // Если вершина уже полностью обработана - циклов нет
        if (visited.contains(vertex)) return false;

        // Помечаем вершину как посещенную и добавляем в текущий путь
        visited.add(vertex);
        recursionStack.add(vertex);

        // Рекурсивно проверяем всех соседей
        List<String> neighbors = graph.getOrDefault(vertex, new ArrayList<>());
        Collections.sort(neighbors); // Сортируем для детерминированного поведения

        for (String neighbor : neighbors) {
            if (dfsHasCycle(neighbor, graph, visited, recursionStack)) {
                return true; // Найден цикл в поддереве
            }
        }

        // Все соседи обработаны, вершина больше не в текущем пути
        recursionStack.remove(vertex);
        return false; // В этом поддереве циклов не найдено
    }
}