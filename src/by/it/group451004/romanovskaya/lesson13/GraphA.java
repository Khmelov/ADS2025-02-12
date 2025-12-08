package by.it.group451004.romanovskaya.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();


        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> temp = new HashMap<>();


        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] strings = edge.split(" -> ");
            String u = strings[0];
            String v = strings[1];


            graph.putIfAbsent(u, new ArrayList<>());
            graph.get(u).add(v);

            temp.put(u, temp.getOrDefault(u, 0));
            temp.put(v, temp.getOrDefault(v, 0) + 1);
        }


        topologicalSort(graph, temp);
    }


    public static void topologicalSort(Map<String, List<String>> graph, Map<String, Integer> inDegree) {

        PriorityQueue<String> priorityQueue = new PriorityQueue<>();


        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                priorityQueue.offer(node);
            }
        }


        List<String> result = new ArrayList<>();

        while (!priorityQueue.isEmpty()) {

            String current = priorityQueue.poll();
            result.add(current);


            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);

                    if (inDegree.get(neighbor) == 0) {
                        priorityQueue.offer(neighbor);
                    }
                }
            }
        }


        for (String node : result) {
            System.out.print(node + " ");
        }
    }
}
