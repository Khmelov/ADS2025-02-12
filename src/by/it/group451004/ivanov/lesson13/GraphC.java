package by.it.group451004.ivanov.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> vertices = new HashSet<>();
        
        // Парсинг входной строки
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();
                vertices.add(from);
                vertices.add(to);
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            }
        }
        
        // Инициализация для всех вершин
        for (String vertex : vertices) {
            graph.putIfAbsent(vertex, new ArrayList<>());
            reverseGraph.putIfAbsent(vertex, new ArrayList<>());
        }
        
        // Алгоритм Косараю для поиска компонент сильной связности
        List<List<String>> components = kosaraju(graph, reverseGraph, vertices);
        
        // Вывод результата (компоненты уже в правильном порядке после Косараю)
        for (List<String> component : components) {
            Collections.sort(component); // Лексикографический порядок вершин в компоненте
            System.out.println(String.join("", component));
        }
    }
    
    private static List<List<String>> kosaraju(Map<String, List<String>> graph,
                                               Map<String, List<String>> reverseGraph,
                                               Set<String> vertices) {
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        
        // Первый проход DFS для получения порядка завершения
        List<String> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices); // Для лексикографического порядка
        
        for (String vertex : sortedVertices) {
            if (!visited.contains(vertex)) {
                dfs1(graph, vertex, visited, order);
            }
        }
        
        // Второй проход DFS на обратном графе
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        
        Collections.reverse(order);
        for (String vertex : order) {
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs2(reverseGraph, vertex, visited, component);
                components.add(component);
            }
        }
        
        return components;
    }
    
    private static void dfs1(Map<String, List<String>> graph, String vertex,
                            Set<String> visited, List<String> order) {
        visited.add(vertex);
        List<String> neighbors = graph.getOrDefault(vertex, new ArrayList<>());
        Collections.sort(neighbors); // Лексикографический порядок
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs1(graph, neighbor, visited, order);
            }
        }
        order.add(vertex);
    }
    
    private static void dfs2(Map<String, List<String>> graph, String vertex,
                            Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);
        List<String> neighbors = graph.getOrDefault(vertex, new ArrayList<>());
        Collections.sort(neighbors); // Лексикографический порядок
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs2(graph, neighbor, visited, component);
            }
        }
    }
}

