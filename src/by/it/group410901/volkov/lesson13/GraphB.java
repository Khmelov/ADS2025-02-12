package by.it.group410901.volkov.lesson13;

import java.util.*;

/**
 * Класс для проверки наличия циклов в ориентированном графе
 * Использует алгоритм DFS с отслеживанием стека рекурсии
 * для обнаружения обратных ребер, которые указывают на циклы
 */
public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        Map<String, List<String>> graph = parseGraph(input);
        boolean hasCycle = hasCycle(graph);
        
        System.out.println(hasCycle ? "yes" : "no");
    }
    
    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();
            
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            if (!graph.containsKey(to)) {
                graph.put(to, new ArrayList<>());
            }
        }
        
        return graph;
    }
    
    /**
     * Проверяет наличие циклов в ориентированном графе
     * Использует DFS с отслеживанием стека рекурсии
     * Цикл обнаруживается, если при обходе встречается вершина,
     * которая уже находится в стеке рекурсии (обратное ребро)
     * @param graph граф в виде списка смежности
     * @return true если граф содержит цикл, false иначе
     */
    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();        // Посещенные вершины
        Set<String> recursionStack = new HashSet<>(); // Вершины в текущем пути DFS
        
        // Проверяем все компоненты связности
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (hasCycleDFS(vertex, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Рекурсивный DFS для обнаружения циклов
     * @param vertex текущая вершина
     * @param graph граф
     * @param visited множество посещенных вершин
     * @param recursionStack множество вершин в текущем пути рекурсии
     * @return true если обнаружен цикл
     */
    private static boolean hasCycleDFS(String vertex, Map<String, List<String>> graph, 
                                     Set<String> visited, Set<String> recursionStack) {
        visited.add(vertex);
        recursionStack.add(vertex); // Добавляем в стек рекурсии
        
        // Проверяем всех соседей
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                // Рекурсивно проверяем непосещенного соседа
                if (hasCycleDFS(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                // Нашли обратное ребро: сосед уже в стеке рекурсии - это цикл!
                return true;
            }
        }
        
        // Убираем вершину из стека рекурсии перед возвратом
        recursionStack.remove(vertex);
        return false;
    }
}
