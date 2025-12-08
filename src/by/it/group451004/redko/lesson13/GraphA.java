package by.it.group451004.redko.lesson13;

import java.util.*;

public class GraphA {
    
    private static class Graph {
        private final Map<String, List<String>> adjacencyList;
        private final Set<String> allVertices;
        
        public Graph() {
            this.adjacencyList = new HashMap<>();
            this.allVertices = new TreeSet<>();
        }
        
        public void addEdge(String from, String to) {
            allVertices.add(from);
            allVertices.add(to);
            adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }
        
        public List<String> topologicalSort() {
            Map<String, Integer> inDegree = new HashMap<>();

            for (String vertex : allVertices) {
                inDegree.put(vertex, 0);
            }

            for (String vertex : adjacencyList.keySet()) {
                for (String neighbor : adjacencyList.get(vertex)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) + 1);
                }
            }

            PriorityQueue<String> queue = new PriorityQueue<>();

            for (String vertex : allVertices) {
                if (inDegree.get(vertex) == 0) {
                    queue.offer(vertex);
                }
            }
            
            List<String> result = new ArrayList<>();
            
            while (!queue.isEmpty()) {
                String current = queue.poll();
                result.add(current);

                if (adjacencyList.containsKey(current)) {
                    for (String neighbor : adjacencyList.get(current)) {
                        int newDegree = inDegree.get(neighbor) - 1;
                        inDegree.put(neighbor, newDegree);
                        
                        if (newDegree == 0) {
                            queue.offer(neighbor);
                        }
                    }
                }
            }
            
            return result;
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
        
        List<String> sorted = graph.topologicalSort();

        for (int i = 0; i < sorted.size(); i++) {
            System.out.print(sorted.get(i));
            if (i < sorted.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}

