package by.it.group451004.ivanov.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        Map<String, List<String>> graph = new HashMap<>();
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
            }
        }
        
        // Проверка на наличие циклов
        boolean hasCycle = hasCycle(graph, vertices);
        System.out.println(hasCycle ? "yes" : "no");
    }
    
    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> vertices) {
        Map<String, Integer> color = new HashMap<>();
        for (String vertex : vertices) {
            color.put(vertex, 0); // 0 - белый (не посещён), 1 - серый (в обработке), 2 - чёрный (обработан)
        }
        
        for (String vertex : vertices) {
            if (color.get(vertex) == 0) {
                if (dfs(graph, vertex, color)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private static boolean dfs(Map<String, List<String>> graph, String vertex, Map<String, Integer> color) {
        color.put(vertex, 1); // Помечаем как серый (в обработке)
        
        List<String> neighbors = graph.getOrDefault(vertex, new ArrayList<>());
        for (String neighbor : neighbors) {
            if (color.get(neighbor) == 1) {
                // Найден обратный рёбер - цикл обнаружен
                return true;
            }
            if (color.get(neighbor) == 0 && dfs(graph, neighbor, color)) {
                return true;
            }
        }
        
        color.put(vertex, 2); // Помечаем как чёрный (обработан)
        return false;
    }
}

