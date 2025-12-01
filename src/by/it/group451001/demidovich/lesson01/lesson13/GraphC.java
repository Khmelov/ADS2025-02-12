package by.it.group451001.demidovich.lesson01.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {
    static Map<String, List<String>> g = new HashMap<>();
    static Map<String, List<String>> gt = new HashMap<>();
    static Set<String> nodes = new TreeSet<>();
    static Deque<String> order = new ArrayDeque<>();
    static Set<String> vis = new HashSet<>();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        String[] parts = line.split(",");
        for (String raw : parts) {
            String token = raw.trim();
            if (token.isEmpty()) continue;
            if (token.contains("->")) {
                String[] a = token.split("->", 2);
                String from = a[0].trim();
                String to = a[1].trim();
                nodes.add(from);
                nodes.add(to);
                g.putIfAbsent(from, new ArrayList<>());
                g.putIfAbsent(to, new ArrayList<>());
                g.get(from).add(to);
            } else {
                String v = token.trim();
                nodes.add(v);
                g.putIfAbsent(v, new ArrayList<>());
            }
        }

        for (String v : nodes) {
            gt.putIfAbsent(v, new ArrayList<>());
        }
        for (String u : g.keySet()) {
            for (String v : g.get(u)) {
                gt.get(v).add(u);
            }
        }

        vis.clear();
        for (String v : nodes) if (!vis.contains(v)) dfs1(v);

        vis.clear();
        List<List<String>> comps = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.pollLast();
            if (!vis.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, comp);
                Collections.sort(comp);
                comps.add(comp);
            }
        }

        for (List<String> comp : comps) {
            System.out.println(String.join("", comp));
        }
    }

    static void dfs1(String v) {
        vis.add(v);
        for (String nb : g.getOrDefault(v, Collections.emptyList())) {
            if (!vis.contains(nb)) dfs1(nb);
        }
        order.addLast(v);
    }

    static void dfs2(String v, List<String> comp) {
        vis.add(v);
        comp.add(v);
        for (String nb : gt.getOrDefault(v, Collections.emptyList())) {
            if (!vis.contains(nb)) dfs2(nb, comp);
        }
    }
}
