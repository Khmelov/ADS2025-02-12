package by.it.group451001.drzhevetskiy.lesson13;

import java.util.*;

public class GraphB {
    static Map<String, List<String>> graph = new HashMap<>();
    static Map<String, Integer> color = new HashMap<>();
    // 0 = white, 1 = gray, 2 = black
    static boolean hasCycle = false;

    static void dfs(String v) {
        color.put(v, 1); // gray
        for (String n : graph.get(v)) {
            if (color.get(n) == 1) {           // gray → cycle
                hasCycle = true;
                return;
            }
            if (color.get(n) == 0) dfs(n);     // white → dfs
        }
        color.put(v, 2); // black
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();
        String[] parts = line.split(",");

        // build graph
        for (String p : parts) {
            p = p.trim();
            String[] arr = p.split("->");
            String from = arr[0].trim();
            String to = arr[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
        }

        // init colors
        for (String v : graph.keySet()) color.put(v, 0);

        // dfs
        for (String v : graph.keySet()) {
            if (color.get(v) == 0) dfs(v);
        }

        System.out.println(hasCycle ? "yes" : "no");
    }
}
