package by.it.group451004.ivanov.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> vertices = new HashSet<>();
        
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
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }
        
        for (String vertex : vertices) {
            inDegree.putIfAbsent(vertex, 0);
        }
        
        List<String> result = topologicalSort(graph, inDegree, vertices);
        
        System.out.println(String.join(" ", result));
    }
    
    private static List<String> topologicalSort(Map<String, List<String>> graph, 
                                                 Map<String, Integer> inDegree, 
                                                 Set<String> vertices) {
        List<String> result = new ArrayList<>();
        PriorityQueue<String> queue = new PriorityQueue<>();
        
        for (String vertex : vertices) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);
            
            List<String> neighbors = graph.getOrDefault(current, new ArrayList<>());
            for (String neighbor : neighbors) {
                int newInDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newInDegree);
                if (newInDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return result;
    }
}

