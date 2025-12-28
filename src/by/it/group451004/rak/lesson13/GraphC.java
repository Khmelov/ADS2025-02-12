package by.it.group451004.rak.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return;

        Graph graph = new Graph(line);
        graph.computeSCC();
        graph.printSCC();
    }

    static class Graph {
        private Map<String, List<String>> g = new HashMap<>();
        private LinkedHashMap<String, Integer> nodesAppear = new LinkedHashMap<>();
        private List<List<String>> comps = new ArrayList<>();

        // поля для DFS
        private Set<String> used;
        private Deque<String> stack;
        private Map<String, List<String>> gr;

        Graph(String input) {
            parse(input);
        }

        private void parse(String input) {
            int idx = 0;
            for (String token : input.split(",")) {
                String s = token.trim();
                if (s.isEmpty()) continue;
                String[] p = s.split("->");
                if (p.length != 2) continue;
                String from = p[0];
                String to = p[1];

                g.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                g.computeIfAbsent(to, k -> new ArrayList<>());

                nodesAppear.putIfAbsent(from, idx++);
                nodesAppear.putIfAbsent(to, idx++);
            }
        }

        public void computeSCC() {
            if (nodesAppear.isEmpty()) return;

            gr = new HashMap<>();
            for (String v : nodesAppear.keySet())
                gr.put(v, new ArrayList<>());
            for (Map.Entry<String, List<String>> e : g.entrySet()) {
                String v = e.getKey();
                for (String u : e.getValue()) {
                    gr.get(u).add(v);
                }
            }

            used = new HashSet<>();
            stack = new ArrayDeque<>();
            for (String v : nodesAppear.keySet()) dfsTranspose(v);

            used.clear();
            while (!stack.isEmpty()) {
                String v = stack.pop();
                if (!used.contains(v)) {
                    List<String> comp = new ArrayList<>();
                    dfsOriginal(v, comp);
                    Collections.sort(comp);
                    comps.add(comp);
                }
            }
        }

        private void dfsTranspose(String v) {
            if (!used.add(v)) return;
            for (String u : gr.getOrDefault(v, Collections.emptyList())) dfsTranspose(u);
            stack.push(v);
        }

        private void dfsOriginal(String v, List<String> comp) {
            if (!used.add(v)) return;
            comp.add(v);
            for (String u : g.getOrDefault(v, Collections.emptyList())) dfsOriginal(u, comp);
        }

        public void printSCC() {
            int m = comps.size();

            Map<String, Integer> compId = new HashMap<>();
            for (int i = 0; i < m; i++)
                for (String v : comps.get(i)) compId.put(v, i);

            List<Set<Integer>> dag = new ArrayList<>();
            for (int i = 0; i < m; i++) dag.add(new HashSet<>());
            int[] indeg = new int[m];

            for (String v : g.keySet()) {
                for (String u : g.get(v)) {
                    int a = compId.get(v), b = compId.get(u);
                    if (a != b && dag.get(a).add(b)) indeg[b]++;
                }
            }

            int[] compMinAppear = new int[m];
            Arrays.fill(compMinAppear, Integer.MAX_VALUE);
            for (int i = 0; i < m; i++) {
                for (String v : comps.get(i)) {
                    int ai = nodesAppear.getOrDefault(v, Integer.MAX_VALUE);
                    if (ai < compMinAppear[i]) compMinAppear[i] = ai;
                }
            }

            String[] compStr = new String[m];
            for (int i = 0; i < m; i++) {
                StringBuilder sb = new StringBuilder();
                for (String v : comps.get(i)) sb.append(v);
                compStr[i] = sb.toString();
            }

            PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> {
                if (compMinAppear[a] != compMinAppear[b])
                    return Integer.compare(compMinAppear[a], compMinAppear[b]);
                return compStr[a].compareTo(compStr[b]);
            });

            for (int i = 0; i < m; i++) if (indeg[i] == 0) pq.add(i);

            List<Integer> order = new ArrayList<>();
            while (!pq.isEmpty()) {
                int cur = pq.poll();
                order.add(cur);
                for (int to : dag.get(cur)) {
                    indeg[to]--;
                    if (indeg[to] == 0) pq.add(to);
                }
            }

            if (order.size() < m) {
                for (int i = 0; i < m; i++) if (!order.contains(i)) order.add(i);
                order.sort((a, b) -> {
                    if (compMinAppear[a] != compMinAppear[b])
                        return Integer.compare(compMinAppear[a], compMinAppear[b]);
                    return compStr[a].compareTo(compStr[b]);
                });
            }

            for (int id : order) System.out.println(compStr[id]);
        }
    }
}
