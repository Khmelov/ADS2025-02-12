package by.it.group410901.danilova.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*"); //разделяем строки на отдельные ребра
            for (String edge : edges) {
                String[] parts = edge.split("\\s*->\\s*");// разделяем строки на начальную и конечную вершины
                String from = parts[0].trim();//начальная
                String to = parts[1].trim();// конечная
                // Добавление ребра в структуру графа
                // Если from отсутствует, создается новый список для него
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Проверка на наличие циклов
        boolean hasCycle = hasCycle(graph, allVertices);

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> allVertices) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String vertex : allVertices) { // Проходим по всем вершинам
            if (!visited.contains(vertex)) { // Если вершина еще не посещена, начинаем новый DFS
                if (dfs(vertex, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(String vertex, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> recursionStack) {
        // Если вершина уже в текущем стеке рекурсии - найден цикл
        if (recursionStack.contains(vertex)) {
            return true;
        }

        // Если вершина уже полностью обработана - пропускаем
        if (visited.contains(vertex)) {
            return false;
        }

        // Добавляем вершину в посещенные и в стек рекурсии
        visited.add(vertex);
        recursionStack.add(vertex);

        // Рекурсивно проверяем всех соседей
        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (dfs(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        // Убираем вершину из стека рекурсии (завершаем обработку)
        recursionStack.remove(vertex);
        return false;
    }
}