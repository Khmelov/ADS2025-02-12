package by.it.group451003.avrusevich.lesson13;


import java.util.*;

public class GraphA {

    static void topologicalSortUnit(String node, Map<String, ArrayList<String>> graph, Set<String> visited, Stack<String> stack) {
        visited.add(node);

        if (graph.get(node)!=null){
            for (String nextNode: graph.get(node)
            ) {
                if (!visited.contains(nextNode)){
                    topologicalSortUnit(nextNode, graph, visited, stack);
                }
            }
        }
        stack.push(node);
    }

    static void topologicalSort(Map<String, ArrayList<String>> graph) {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        for (ArrayList<String> array : graph.values()) {
            array.sort(Comparator.reverseOrder());
        }

        for (String node : graph.keySet()
        ) {
            if (!visited.contains(node)){
                topologicalSortUnit(node, graph, visited, stack);
            }
        }

        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
    }

    private static void getGraph(Map<String, ArrayList<String>> graph) {
        Scanner in = new Scanner(System.in);

        boolean isEnd = false;
        while (!isEnd) {
            String vertexOut = in.next();
            if (!graph.containsKey(vertexOut)) {
                graph.put(vertexOut, new ArrayList<>());
            }
            String edge = in.next();
            String vertexIn = in.next();
            if (vertexIn.charAt(vertexIn.length() - 1) == ',') {
                vertexIn = vertexIn.substring(0, vertexIn.length() - 1);
            } else {
                isEnd = true;
            }
            graph.get(vertexOut).add(vertexIn);
        }
    }

    public static void main(String[] args) {
        Map<String, ArrayList<String>> graph = new HashMap<>();
        getGraph(graph);
        topologicalSort(graph);
    }
}