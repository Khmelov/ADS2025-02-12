package by.it.group451003.rashchenya.lesson13;

import java.util.*;

public class GraphC {

    static class LexicalComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            return s2.compareTo(s1);
        }
    }

    public static void main(String[] args) {
        Map<String, ArrayList<String>> neighbours = new HashMap<>();
        Stack<String> st = new Stack<>();
        Set<String> vis = new HashSet<>();

        Scanner scanner = new Scanner(System.in);
        String[] vals = scanner.nextLine().split(",");
        scanner.close();

        String start;
        String end;

        for (String s : vals) {
            String[] current = s.trim().split("");
            start = current[0];
            end = (current[current.length - 1]);
            if (neighbours.get(start) == null)
                neighbours.put(start, new ArrayList<>());

            neighbours.get(start).add(end);
        }

        for (ArrayList<String> list: neighbours.values()) {
            list.sort(new LexicalComparator());
        }

        for (String w : neighbours.keySet()) {
            if (!vis.contains(w)) {
                dfs(neighbours, w, vis, st);
            }
        }

        Map<String, ArrayList<String>> transpNeighbours = new HashMap<>();
        for (String v :neighbours.keySet()) {
            ArrayList<String> list = neighbours.get(v);
            for (String child : list) {
                if (transpNeighbours.get(child) == null)
                    transpNeighbours.put(child, new ArrayList<>());
                transpNeighbours.get(child).add(v);
            }
        }

        vis.clear();
        while (!st.empty()) {
            String v = st.pop();
            if (!vis.contains(v)) {
                StringBuilder sb = new StringBuilder();
                dfs(transpNeighbours, v, vis, sb);
                char[] charArr = sb.toString().toCharArray();
                Arrays.sort(charArr);
                System.out.println(charArr);
            }
        }

    }

    private static void dfs(Map<String, ArrayList<String>> neighbours,
                            String v, Set<String> vis, Stack<String> st) {
        vis.add(v);

        if (neighbours.get(v) != null) {
            for (String child : neighbours.get(v)) {
                if (!vis.contains(child)) {
                    dfs(neighbours, child, vis, st);
                }
            }
        }

        st.push(v);
    }

    private static void dfs(Map<String, ArrayList<String>> neighbours,
                            String v, Set<String> vis, StringBuilder sb) {
        vis.add(v);
        sb.append(v);
        if (neighbours.get(v) != null) {
            for (String child : neighbours.get(v)) {
                if (!vis.contains(child)) {
                    dfs(neighbours, child, vis, sb);
                }
            }
        }
    }
}