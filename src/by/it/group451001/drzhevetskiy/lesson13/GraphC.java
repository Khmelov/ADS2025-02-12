package by.it.group451001.drzhevetskiy.lesson13;

import java.util.*;

public class GraphC {

    static Map<String, List<String>> g = new HashMap<>();
    static Map<String, List<String>> gr = new HashMap<>();
    static Set<String> used = new HashSet<>();
    static List<String> order = new ArrayList<>();
    static List<String> component = new ArrayList<>();

    static void dfs1(String v) {
        used.add(v);
        for (String n : g.get(v)) {
            if (!used.contains(n)) dfs1(n);
        }
        order.add(v);
    }

    static void dfs2(String v) {
        used.add(v);
        component.add(v);
        for (String n : gr.get(v)) {
            if (!used.contains(n)) dfs2(n);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();
        String[] edges = line.split(",");

        // build graphs
        for (String e : edges) {
            e = e.trim();
            String[] arr = e.split("->");
            String from = arr[0].trim();
            String to = arr[1].trim();

            g.putIfAbsent(from, new ArrayList<>());
            g.putIfAbsent(to, new ArrayList<>());

            gr.putIfAbsent(from, new ArrayList<>());
            gr.putIfAbsent(to, new ArrayList<>());

            g.get(from).add(to);
            gr.get(to).add(from);
        }

        // Kosaraju step 1: order by exit time
        used.clear();
        for (String v : g.keySet()) {
            if (!used.contains(v)) dfs1(v);
        }

        // Kosaraju step 2: reverse order on reversed graph
        used.clear();
        Collections.reverse(order);

        List<List<String>> scc = new ArrayList<>();

        for (String v : order) {
            if (!used.contains(v)) {
                component.clear();
                dfs2(v);
                Collections.sort(component); // lexicographic inside SCC
                scc.add(new ArrayList<>(component));
            }
        }

        // Output SCC exactly as required
        for (List<String> c : scc) {
            StringBuilder sb = new StringBuilder();
            for (String v : c) sb.append(v);
            System.out.println(sb);
        }
    }
}
