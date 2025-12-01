package by.bsuir.dsa.csv2025.gr410901.Квитченко;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution  {

    private static class Edge {
        String to;
        int capacity;
        Edge reverseEdge;

        Edge(String to, int capacity) {
            this.to = to;
            this.capacity = capacity;
        }
    }

    private Map<String, List<Edge>> graph = new HashMap<>();
    private Map<String, String> parentNodeMap;
    private Map<String, Edge> parentEdgeMap;

    private void addEdge(String u, String v, int capacity) {
        graph.putIfAbsent(u, new ArrayList<>());
        graph.putIfAbsent(v, new ArrayList<>());

        Edge forward = new Edge(v, capacity);
        Edge backward = new Edge(u, 0);

        forward.reverseEdge = backward;
        backward.reverseEdge = forward;

        graph.get(u).add(forward);
        graph.get(v).add(backward);
    }

    private int bfs(String start, String end) {
        parentNodeMap = new HashMap<>();
        parentEdgeMap = new HashMap<>();

        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        parentNodeMap.put(start, null);

        while (!queue.isEmpty()) {
            String u = queue.poll();
            if (u.equals(end)) break;

            for (Edge edge : graph.getOrDefault(u, Collections.emptyList())) {
                if (!parentNodeMap.containsKey(edge.to) && edge.capacity > 0) {
                    parentNodeMap.put(edge.to, u);
                    parentEdgeMap.put(edge.to, edge);
                    queue.add(edge.to);
                }
            }
        }

        if (!parentNodeMap.containsKey(end)) {
            return 0;
        }

        int pathFlow = Integer.MAX_VALUE;
        String v = end;
        while (parentNodeMap.get(v) != null) {
            Edge edge = parentEdgeMap.get(v);
            pathFlow = Math.min(pathFlow, edge.capacity);
            v = parentNodeMap.get(v);
        }

        v = end;
        while (parentNodeMap.get(v) != null) {
            Edge forwardEdge = parentEdgeMap.get(v);

            forwardEdge.capacity -= pathFlow;

            forwardEdge.reverseEdge.capacity += pathFlow;

            v = parentNodeMap.get(v);
        }

        return pathFlow;
    }

    public int maxFlow(String start, String end) {
        int totalFlow = 0;
        int pathFlow;

        while ((pathFlow = bfs(start, end)) > 0) {
            totalFlow += pathFlow;
        }

        return totalFlow;
    }

    public static void main(String[] args) {
        Solution flow = new Solution();
        String graphInput;
        String startEnd;
        int resultFlow = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            if (scanner.hasNextLine()) {
                graphInput = scanner.nextLine().trim();
            } else {
                System.out.print(0); return;
            }

            if (scanner.hasNextLine()) {
                startEnd = scanner.nextLine().trim();
            } else {
                System.out.print(0); return;
            }

            String[] parts = graphInput.split("\\s*,\\s*");

            for (String part : parts) {
                if (part.isEmpty()) continue;

                String[] wParts = part.split(":");
                if (wParts.length != 2) continue;

                try {
                    int capacity = Integer.parseInt(wParts[1].trim());
                    String[] vParts = wParts[0].split("-");

                    if (vParts.length != 2) continue;

                    String u = vParts[0].trim();
                    String v = vParts[1].trim();

                    if (capacity > 0) {
                        flow.addEdge(u, v, capacity);
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            String[] se = startEnd.split("\\s+");

            if (se.length == 2) {
                String startNode = se[0].trim();
                String endNode = se[1].trim();
                resultFlow = flow.maxFlow(startNode, endNode);
            }

        } catch (Exception e) {
            resultFlow = 0;
        }

        System.out.print(resultFlow);
    }

    @Test
    public void Test1() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 10);
        flow.addEdge("A", "T", 5);
        assertEquals(5, flow.maxFlow("S", "T"));
    }

    @Test
    public void Test2() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 10);
        flow.addEdge("A", "T", 5);
        flow.addEdge("S", "B", 10);
        flow.addEdge("B", "T", 10);
        assertEquals(15, flow.maxFlow("S", "T"));
    }

    @Test
    public void Test3() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 10);
        flow.addEdge("A", "B", 8);
        flow.addEdge("B", "T", 12);
        assertEquals(8, flow.maxFlow("S", "T"));
    }

    @Test
    public void Test4() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 10);
        flow.addEdge("S", "B", 10);
        flow.addEdge("A", "B", 1);
        flow.addEdge("B", "T", 10);
        flow.addEdge("A", "T", 10);
        assertEquals(20, flow.maxFlow("S", "T"));
    }

    @Test
    public void Test5() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 0);
        flow.addEdge("A", "T", 10);
        assertEquals(0, flow.maxFlow("S", "T"));
    }

    @Test
    public void Test6() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 10);
        flow.addEdge("A", "T", 5);
        flow.addEdge("B", "C", 1);
        assertEquals(5, flow.maxFlow("S", "T"));
    }

    @Test
    public void Test7() {
        Solution flow = new Solution();
        flow.addEdge("1", "2", 10);
        flow.addEdge("2", "3", 5);
        assertEquals(5, flow.maxFlow("1", "3"));
    }

    @Test
    public void Test8() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 10);
        flow.addEdge("B", "C", 10);
        flow.addEdge("D", "E", 10);
        assertEquals(0, flow.maxFlow("S", "T"));
    }

    @Test
    public void Test9() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 500000);
        flow.addEdge("A", "T", 500000);
        assertEquals(500000, flow.maxFlow("S", "T"));
    }

    @Test
    public void Test10() {
        Solution flow = new Solution();
        flow.addEdge("S", "A", 10);
        flow.addEdge("S", "C", 5);
        flow.addEdge("A", "C", 5);
        flow.addEdge("C", "T", 15);
        flow.addEdge("A", "T", 5);
        assertEquals(15, flow.maxFlow("S", "T"));
    }
}