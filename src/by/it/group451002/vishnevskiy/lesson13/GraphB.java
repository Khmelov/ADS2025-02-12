package by.it.group451002.vishnevskiy.lesson13;

import java.util.*;

public class GraphB {

    // Главный метод программы
    public static void main(String[] args) {
        // Создаем сканер для чтения ввода с консоли
        Scanner scanner = new Scanner(System.in);
        // Читаем всю строку, содержащую описание графа
        String input = scanner.nextLine();

        // Проверяем наличие циклов в графе
        boolean hasCycle = hasCycle(input);

        // Выводим результат: "yes" если есть циклы, "no" если нет
        System.out.println(hasCycle ? "yes" : "no");
    }

    // Метод для проверки наличия циклов в ориентированном графе
    public static boolean hasCycle(String input) {
        // Граф: вершина -> список её соседей (достижимых вершин)
        Map<String, List<String>> graph = new HashMap<>();
        // Множество всех вершин графа
        Set<String> allVertices = new HashSet<>();

        // Проверяем, не пустая ли входная строка
        if (!input.isEmpty()) {
            // Разбиваем строку по запятым, получая отдельные рёбра
            String[] edges = input.split(",\\s*");

            // Обрабатываем каждое ребро
            for (String edge : edges) {
                // Разделяем ребро по символу "->"
                String[] parts = edge.split("\\s*->\\s*");
                String from = parts[0].trim();  // Начальная вершина ребра
                String to = parts[1].trim();    // Конечная вершина ребра

                // Добавляем ребро в граф:
                // Если вершина 'from' еще не в графе, создаем для неё пустой список
                // Затем добавляем 'to' в список соседей 'from'
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

                // Добавляем обе вершины в множество всех вершин
                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Множество для отслеживания посещённых вершин
        Set<String> visited = new HashSet<>();
        // Множество для отслеживания вершин в текущем пути (стеке рекурсии)
        Set<String> recursionStack = new HashSet<>();

        // Проверяем все вершины графа на наличие циклов
        for (String vertex : allVertices) {
            // Если вершина еще не посещена, запускаем из неё DFS
            if (!visited.contains(vertex)) {
                // Если DFS находит цикл, возвращаем true
                if (dfsHasCycle(vertex, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        // Если ни из одной вершины не найден цикл, возвращаем false
        return false;
    }

    // Рекурсивный метод обхода в глубину (DFS) для поиска циклов
    private static boolean dfsHasCycle(String vertex, Map<String, List<String>> graph,
                                       Set<String> visited, Set<String> recursionStack) {
        // Помечаем текущую вершину как посещённую
        visited.add(vertex);
        // Добавляем вершину в стек текущего пути
        recursionStack.add(vertex);

        // Если у текущей вершины есть соседи (исходящие рёбра)
        if (graph.containsKey(vertex)) {
            // Проверяем всех соседей текущей вершины
            for (String neighbor : graph.get(vertex)) {
                // Если сосед еще не посещён
                if (!visited.contains(neighbor)) {
                    // Рекурсивно вызываем DFS для этого соседа
                    if (dfsHasCycle(neighbor, graph, visited, recursionStack)) {
                        return true;  // Найден цикл в подграфе
                    }
                }
                // Если сосед уже в стеке текущего пути - это цикл!
                else if (recursionStack.contains(neighbor)) {
                    return true;  // Обнаружен цикл
                }
            }
        }

        // Убираем вершину из стека текущего пути перед возвратом из рекурсии
        // Это означает, что мы закончили обработку этой вершины и её потомков
        recursionStack.remove(vertex);

        // Цикл не найден для этой вершины и её потомков
        return false;
    }
}