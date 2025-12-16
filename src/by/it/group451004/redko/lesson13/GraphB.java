package by.it.group451004.redko.lesson13;

import java.util.*;

public class GraphB {
    
    private static class Graph {
        private final Map<String, List<String>> adjacencyList;
        private final Set<String> allVertices;
        
        public Graph() {
            this.adjacencyList = new HashMap<>();
            this.allVertices = new HashSet<>();
        }
        
        public void addEdge(String from, String to) {
            allVertices.add(from);
            allVertices.add(to);
            adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }
        
        public boolean hasCycle() {
            Set<String> visited = new HashSet<>();
            Set<String> recursionStack = new HashSet<>();
            
            for (String vertex : allVertices) {
                if (!visited.contains(vertex)) {
                    if (hasCycleDFS(vertex, visited, recursionStack)) {
                        return true;
                    }
                }
            }
            
            return false;
        }
        
        private boolean hasCycleDFS(String vertex, Set<String> visited, Set<String> recursionStack) {
            visited.add(vertex);
            recursionStack.add(vertex);
            
            if (adjacencyList.containsKey(vertex)) {
                for (String neighbor : adjacencyList.get(vertex)) {
                    if (!visited.contains(neighbor)) {
                        if (hasCycleDFS(neighbor, visited, recursionStack)) {
                            return true;
                        }
                    } else if (recursionStack.contains(neighbor)) {
                        return true;
                    }
                }
            }
            
            recursionStack.remove(vertex);
            return false;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        Graph graph = new Graph();

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();
                graph.addEdge(from, to);
            }
        }
        
        if (graph.hasCycle()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}


