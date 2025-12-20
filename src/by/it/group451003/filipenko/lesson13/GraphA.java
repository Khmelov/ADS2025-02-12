package by.it.group451003.filipenko.lesson13;

import java.util.*;

public class GraphA {
    protected HashMap<String, ArrayList<String>> graph;

    GraphA(Scanner scanner) {
        this.graph = createGraph(scanner);
    }

    protected HashMap<String, ArrayList<String>> createGraph(Scanner scanner) {
        HashMap<String, ArrayList<String>> graph = new HashMap<>();
        String key, value;

        for (String nodes : scanner.nextLine().split(", ")) {
            String[] twoNodes = nodes.split(" -> ");
            key = twoNodes[0];
            value = twoNodes[1];

            if (graph.get(key) != null && graph.get(key).contains(value)) continue;

            if (!graph.containsKey(key))
                graph.put(key, new ArrayList<>());
            graph.get(key).add(value);
        }

        scanner.close();
        return graph;
    }

    protected void DFS(ArrayList<String> order, ArrayList<String> visited, String key) {
        visited.add(key);

        if (graph.get(key) != null) {
            Collections.sort(graph.get(key));
            Collections.reverse(graph.get(key));

            for (String value : graph.get(key))
                if (!visited.contains(value))
                    DFS(order, visited, value);
        }

        order.add(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> visited = new ArrayList<>();
        ArrayList<String> order = new ArrayList<>();
        for (String key : graph.keySet())
            if (!visited.contains(key))
                DFS(order, visited, key);
        Collections.reverse(order);

        sb.append("[");
        for (String element : order)
            sb.append(element).append(" ");

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");

        return sb.toString();
    }

    public static void main(String[] args){
        GraphA myGraphA  = new GraphA(new Scanner(System.in));
        System.out.println(myGraphA);
    }
}
