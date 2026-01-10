package by.it.group451004.akbulatov.lesson13;

import java.util.*;

public class GraphA
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(", ");

        for (String edge : edges)
        {
            String[] vertices = edge.split(" -> ");
            String from = vertices[0];
            String to = vertices[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            graph.putIfAbsent(to, new ArrayList<>());

            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        List<String> result = new ArrayList<>();
        PriorityQueue<String> queue = new PriorityQueue<>();

        for (String vertex : inDegree.keySet())
            if (inDegree.get(vertex) == 0)
                queue.add(vertex);

        while (!queue.isEmpty())
        {
            String current = queue.poll();
            result.add(current);

            for (String neighbor : graph.get(current))
            {
                int newDegree = inDegree.get(neighbor) - 1;
                if (newDegree == 0) queue.add(neighbor);
                inDegree.put(neighbor, newDegree);
            }
        }

        for (int i = 0; i < result.size(); i++)
        {
            System.out.print(result.get(i));

            if (i < result.size() - 1)
                System.out.print(" ");
        }
    }
}