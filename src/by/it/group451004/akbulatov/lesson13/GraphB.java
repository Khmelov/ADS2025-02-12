package by.it.group451004.akbulatov.lesson13;

import java.util.*;

public class GraphB
{
    enum State {ST_UNPROCESSED, ST_PROCESSING, ST_PROCESSED};

    private static final Map<Integer, List<Integer>> graph = new HashMap<>();
    private static final Map<Integer, State> state = new HashMap<>();

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        String[] edges = input.split(", ");
        for (String edge : edges)
        {
            String[] parts = edge.split(" -> ");
            int from = Integer.parseInt(parts[0]);
            int to = Integer.parseInt(parts[1]);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);

            state.put(from, State.ST_UNPROCESSED);
            state.put(to, State.ST_UNPROCESSED);
        }

        boolean hasCycle = false;
        for (Integer node : graph.keySet())
        {
            if (state.get(node) == State.ST_UNPROCESSED)
            {
                if (dfs(node))
                {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(int node)
    {
        state.put(node, State.ST_PROCESSING);

        if (graph.containsKey(node))
        {
            for (int neighbor : graph.get(node))
            {
                if (state.get(neighbor) == State.ST_UNPROCESSED)
                {
                    if (dfs(neighbor)) return true;
                }
                else if (state.get(neighbor) == State.ST_PROCESSING)
                    return true;
            }
        }

        state.put(node, State.ST_PROCESSED);
        return false;
    }
}