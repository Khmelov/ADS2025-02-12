package by.it.group410902.shahov.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки в граф
        Map<Integer, List<Integer>> graph = parseGraph(input);

        // Проверка наличия циклов в графе
        boolean hasCycle = hasCycle(graph);

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    /**
     * Парсит строку с описанием графа и возвращает представление в виде списка смежности
     * Формат входной строки: "1->2, 2->3, 3->1" (ребра разделены запятыми)
     */
    private static Map<Integer, List<Integer>> parseGraph(String input) {
        Map<Integer, List<Integer>> graph = new HashMap<>();

        // Проверка на пустую входную строку
        if (input == null || input.trim().isEmpty()) {
            return graph;
        }

        // Разделение строки на отдельные ребра по запятым
        String[] edges = input.split(",");

        for (String edge : edges) {
            // Очистка пробелов и разбор ребра
            String cleanedEdge = edge.trim();
            String[] parts = cleanedEdge.split("->");

            // Проверка корректности формата ребра
            if (parts.length == 2) {
                try {
                    // Парсинг вершин из строк в числа
                    int from = Integer.parseInt(parts[0].trim());
                    int to = Integer.parseInt(parts[1].trim());

                    // Добавление ребра в граф
                    // computeIfAbsent: если ключа нет, создает новый ArrayList
                    graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

                    // Гарантируем, что конечная вершина тоже есть в графе
                    // (даже если у нее нет исходящих ребер)
                    graph.putIfAbsent(to, new ArrayList<>());
                } catch (NumberFormatException e) {
                    // Игнорируем некорректные записи (не числа)
                }
            }
        }

        return graph;
    }

    /**
     * Проверяет, содержит ли ориентированный граф циклы
     * Использует алгоритм DFS с отслеживанием стека рекурсии
     */
    private static boolean hasCycle(Map<Integer, List<Integer>> graph) {
        Set<Integer> visited = new HashSet<>();      // Множество полностью обработанных вершин
        Set<Integer> recursionStack = new HashSet<>(); // Множество вершин в текущем пути DFS

        // Запускаем DFS из каждой непосещенной вершины
        for (Integer node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfsHasCycle(node, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Рекурсивная функция DFS для обнаружения циклов
     * @param node текущая вершина
     * @param graph граф
     * @param visited множество посещенных вершин
     * @param recursionStack стек рекурсии (вершины в текущем пути обхода)
     * @return true если найден цикл, иначе false
     */
    private static boolean dfsHasCycle(Integer node, Map<Integer, List<Integer>> graph,
                                       Set<Integer> visited, Set<Integer> recursionStack) {
        // Помечаем вершину как посещенную в текущем обходе
        visited.add(node);
        recursionStack.add(node);

        // Проверяем всех соседей текущей вершины
        for (Integer neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                // Если сосед не посещен, рекурсивно проверяем его
                if (dfsHasCycle(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                // Если сосед уже в стеке рекурсии - найден цикл!
                return true;
            }
        }

        // Убираем вершину из стека рекурсии при backtracking
        recursionStack.remove(node);
        return false;
    }
}
