package by.it.group410901.borisdubinin.lesson13;

import java.util.*;

public class GraphC {
    private static Map<String, List<String>> graph;
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        graph = parseGraph(input);
        stack = new ArrayDeque<>();
        visited = new HashSet<>();

        List<List<String>> components = kosaraju();
        for(List<String> component : components){
            System.out.println(String.join("", component));
        }
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

    private static List<List<String>> kosaraju(){
        for(String vertex : graph.keySet()){
            if(!visited.contains(vertex)){
                dfsFirst(vertex);
            }
        }

        transposeGraph();

        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while(!stack.isEmpty()){
            String top = stack.pop();
            if(!visited.contains(top)){
                List<String> component = new ArrayList<>();
                dfsSecond(top, component);
                Collections.sort(component);
                components.add(component);
            }
        }
        return components;
    }

    private static Set<String> visited;
    private static Deque<String> stack;
    private static void dfsFirst(String vertex) {
        visited.add(vertex);

        for(String neighbor : graph.get(vertex)){
            if(!visited.contains(neighbor))
                dfsFirst(neighbor);
        }

        stack.push(vertex);
    }

    private static void transposeGraph(){
        Map<String, List<String>> transposed = new HashMap<>();

        for(String vertex : graph.keySet()){
            transposed.computeIfAbsent(vertex, k -> new ArrayList<>());
            for(String neighbor : graph.get(vertex)){
                transposed.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(vertex);
            }
        }

        graph = transposed;
    }

    private static void dfsSecond(String vertex, List<String> component){
        visited.add(vertex);
        component.add(vertex);

        for(String neighbor : graph.get(vertex)){
            if(!visited.contains(neighbor))
                dfsSecond(neighbor, component);
        }
    }
}
