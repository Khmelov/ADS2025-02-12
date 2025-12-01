package by.it.group451004.redko.lesson13;

import java.util.*;

public class GraphC {
    
    private static class Graph {
        private final Map<String, List<String>> adjacencyList;
        private final Map<String, List<String>> reverseAdjacencyList;
        private final Set<String> allVertices;
        
        public Graph() {
            this.adjacencyList = new HashMap<>();
            this.reverseAdjacencyList = new HashMap<>();
            this.allVertices = new HashSet<>();
        }
        
        public void addEdge(String from, String to) {
            allVertices.add(from);
            allVertices.add(to);
            adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseAdjacencyList.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        }

        public List<Set<String>> findStronglyConnectedComponents() {
            Stack<String> finishStack = new Stack<>();
            Set<String> visited = new HashSet<>();

            List<String> sortedVertices = new ArrayList<>(allVertices);
            Collections.sort(sortedVertices);
            
            for (String vertex : sortedVertices) {
                if (!visited.contains(vertex)) {
                    dfs1(vertex, visited, finishStack);
                }
            }

            visited.clear();
            List<Set<String>> components = new ArrayList<>();
            
            while (!finishStack.isEmpty()) {
                String vertex = finishStack.pop();
                if (!visited.contains(vertex)) {
                    Set<String> component = new HashSet<>();
                    dfs2(vertex, visited, component);
                    components.add(component);
                }
            }
            
            return components;
        }
        
        private void dfs1(String vertex, Set<String> visited, Stack<String> finishStack) {
            visited.add(vertex);
            
            if (adjacencyList.containsKey(vertex)) {
                List<String> neighbors = new ArrayList<>(adjacencyList.get(vertex));
                Collections.sort(neighbors);
                
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        dfs1(neighbor, visited, finishStack);
                    }
                }
            }
            
            finishStack.push(vertex);
        }
        
        private void dfs2(String vertex, Set<String> visited, Set<String> component) {
            visited.add(vertex);
            component.add(vertex);
            
            if (reverseAdjacencyList.containsKey(vertex)) {
                List<String> neighbors = new ArrayList<>(reverseAdjacencyList.get(vertex));
                Collections.sort(neighbors);
                
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        dfs2(neighbor, visited, component);
                    }
                }
            }
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
        
        List<Set<String>> components = graph.findStronglyConnectedComponents();

        for (Set<String> component : components) {
            List<String> sortedComponent = new ArrayList<>(component);
            Collections.sort(sortedComponent);
            
            for (String vertex : sortedComponent) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }
}

