package by.it.group451004.rak.lesson13;
import java.util.*;
public class GraphA {
    private Map<String, List<String>> adj = new HashMap<>(); //списк смежности, не все узлы это ключ
    private Set<String> nodes = new HashSet<>();

    public void addEdge(String from, String to) {
        adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        nodes.add(from);
        nodes.add(to);
    }

    public List<String> topologicalSort() {
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : nodes) {
            inDegree.put(node, 0);
        }
        for (String u : adj.keySet()) {//подсчет степени того, сколько указывают в данное ребро
            for (String v : adj.get(u)) {
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) queue.add(node); //не пусто
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String u = queue.poll();
            result.add(u);
            if (adj.containsKey(u)) {
                for (String v : adj.get(u)) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) queue.add(v);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        GraphA graph = new GraphA();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            graph.addEdge(parts[0], parts[1]);
        }

        List<String> sorted = graph.topologicalSort();
        for (String node : sorted) {
            System.out.print(node + " ");
        }
    }
}