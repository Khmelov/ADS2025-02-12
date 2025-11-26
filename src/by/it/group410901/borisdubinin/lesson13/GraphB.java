package by.it.group410901.borisdubinin.lesson13;

import java.util.*;

public class GraphB {
    private static Map<String, List<String>> graph;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        graph = parseGraph(input);
        visited = new HashSet<>();
        processed = new HashSet<>();
        boolean hasCycle = hasCycle();

        System.out.println(hasCycle ? "yes" : "no");
        scanner.close();
    }

    private static Map<String, List<String>> parseGraph(String input){
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] vertices = edge.split("\\s*->\\s*");
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        return graph;
    }

    private static Set<String> visited;
    private static Set<String> processed;
    private static boolean hasCycle(){
        boolean hasCycle = false;
        for(String vertex : graph.keySet()){
            if(!processed.contains(vertex))
                hasCycle |= dfs(vertex);
        }
        return hasCycle;
    }

    private static boolean dfs(String currentVertex){
        if(visited.contains(currentVertex)){   //если текущая вершина посещена
            return true;           //есть цикл
        }
        if(processed.contains(currentVertex)){
            return false;
        }
        visited.add(currentVertex);

        for(String neighbor : graph.get(currentVertex)){
            if(dfs(neighbor))
                return true;
        }

        visited.remove(currentVertex);
        processed.add(currentVertex);
        return false;
    }
}
