package by.it.group410901.volkov.lesson13;

import java.util.*;

/**
 * Класс для поиска компонент сильной связности в ориентированном графе
 * Компонента сильной связности - это максимальное множество вершин,
 * где каждая вершина достижима из любой другой вершины этого множества
 * Использует алгоритм Косарайю (Kosaraju's algorithm)
 */
public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        Map<String, List<String>> graph = parseGraph(input);
        List<List<String>> components = findStronglyConnectedComponents(graph);
        
        for (List<String> component : components) {
            Collections.sort(component);
            System.out.println(String.join("", component));
        }
    }
    
    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // Убедимся, что все вершины присутствуют в графе
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        
        return graph;
    }
    
    /**
     * Находит компоненты сильной связности алгоритмом Косарайю
     * Алгоритм состоит из двух проходов DFS:
     * 1. Первый проход: обход исходного графа, сохранение порядка завершения
     * 2. Второй проход: обход обращенного графа в обратном порядке завершения
     * Время выполнения: O(V + E)
     * @param graph граф в виде списка смежности
     * @return список компонент сильной связности
     */
    private static List<List<String>> findStronglyConnectedComponents(Map<String, List<String>> graph) {
        // Первый проход DFS: обход исходного графа
        List<String> finishOrder = new ArrayList<>(); // Порядок завершения вершин
        Set<String> visited = new HashSet<>();
        
        // Выполняем DFS для всех вершин, сохраняя порядок завершения
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfs1(vertex, graph, visited, finishOrder);
            }
        }
        
        // Создаем обращенный граф (все ребра развернуты)
        Map<String, List<String>> reversedGraph = reverseGraph(graph);
        // Обращаем порядок завершения для второго прохода
        Collections.reverse(finishOrder);
        
        // Второй проход DFS: обход обращенного графа
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        
        // Обходим вершины в обратном порядке завершения первого прохода
        for (String vertex : finishOrder) {
            if (!visited.contains(vertex)) {
                // Каждый новый DFS во втором проходе находит одну компоненту
                List<String> component = new ArrayList<>();
                dfs2(vertex, reversedGraph, visited, component);
                components.add(component);
            }
        }
        
        return components;
    }
    
    private static void dfs1(String vertex, Map<String, List<String>> graph, 
                           Set<String> visited, List<String> finishOrder) {
        visited.add(vertex);
        
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, graph, visited, finishOrder);
            }
        }
        
        finishOrder.add(vertex);
    }
    
    private static void dfs2(String vertex, Map<String, List<String>> reversedGraph, 
                           Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);
        
        for (String neighbor : reversedGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, reversedGraph, visited, component);
            }
        }
    }
    
    /**
     * Создает обращенный граф: все ребра развернуты в обратную сторону
     * Если в исходном графе было ребро (u -> v), в обращенном будет (v -> u)
     * @param graph исходный граф
     * @return обращенный граф
     */
    private static Map<String, List<String>> reverseGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> reversedGraph = new HashMap<>();
        
        // Собираем все вершины (включая те, которые только в списках смежности)
        Set<String> allVertices = new HashSet<>(graph.keySet());
        for (List<String> neighbors : graph.values()) {
            allVertices.addAll(neighbors);
        }
        
        // Инициализируем все вершины в обращенном графе пустыми списками
        for (String vertex : allVertices) {
            reversedGraph.put(vertex, new ArrayList<>());
        }
        
        // Обращаем ребра: для каждого ребра (from -> to) в исходном графе
        // создаем ребро (to -> from) в обращенном графе
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                reversedGraph.get(to).add(from);
            }
        }
        
        return reversedGraph;
    }
}
