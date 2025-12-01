package by.it.group451001.sobol.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphB {
    static Map<String, List<String>> g = new HashMap<>();
    static Map<String, Integer> color = new HashMap<>();
    static boolean hasCycle = false;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        String[] parts = line.split(",");
        for (String raw : parts) {
            String token = raw.trim();
            if (token.contains("->")) {
                String[] arrow = token.split("->", 2);
                String from = arrow[0].trim();
                String to = arrow[1].trim();

                g.putIfAbsent(from, new ArrayList<>());
                g.putIfAbsent(to, new ArrayList<>());

                g.get(from).add(to);
            } else {
                String v = token.trim();
                g.putIfAbsent(v, new ArrayList<>());
            }
        }

        List<String> vertices = new ArrayList<>(g.keySet());

        for (String v : vertices) color.put(v, 0);
        for (String v : vertices)
            if (color.get(v) == 0) dfs(v);

        System.out.println(hasCycle ? "yes" : "no");
    }

    static void dfs(String v) {
        color.put(v, 1);
        for (String u : g.get(v)) {
            if (color.get(u) == 1) {
                hasCycle = true;
                return;
            }
            if (color.get(u) == 0) dfs(u);
            if (hasCycle) return;
        }
        color.put(v, 2);
    }
}