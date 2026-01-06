package by.it.group451004.maximenko.lesson13;
import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();


        String[] parts = input.split(",");
        for (String part : parts) {
            String[] lr = part.trim().split("->");

            if (lr.length != 2) continue;

            String from = lr[0].trim();
            String to = lr[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);

            vertices.add(from);
            vertices.add(to);
        }


        Map<String, Integer> state = new HashMap<>();
        for (String v : vertices) state.put(v, 0);

        boolean[] hasCycle = {false};

        for (String v : vertices) {
            if (state.get(v) == 0) {
                dfs(v, graph, state, hasCycle);
            }
        }

        System.out.println(hasCycle[0] ? "yes" : "no");
    }

    private static void dfs(
            String v,
            Map<String, List<String>> graph,
            Map<String, Integer> state,
            boolean[] hasCycle
    ) {
        if (hasCycle[0]) return;

        state.put(v, 1);

        for (String nei : graph.get(v)) {
            if (state.get(nei) == 0) {
                dfs(nei, graph, state, hasCycle);
            } else if (state.get(nei) == 1) {
                hasCycle[0] = true;
                return;
            }
        }

        state.put(v, 2);
    }
}
