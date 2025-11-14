package by.it.group451001.romeyko.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();

        // 1. Строим граф и транспонированный граф
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> transposed = new HashMap<>();

        String[] parts = line.split(",");
        for (String p : parts) {
            String[] lr = p.trim().split("->");
            String from = lr[0].trim();
            String to = lr[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            graph.putIfAbsent(to, new ArrayList<>());

            // транспонированный граф
            transposed.putIfAbsent(to, new ArrayList<>());
            transposed.putIfAbsent(from, new ArrayList<>());
            transposed.get(to).add(from);
        }

        // 2. Первый DFS — формируем порядок выхода
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        List<String> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices);

        for (String v : vertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, stack);
            }
        }

        // 3. Второй DFS на транспонированном графе
        visited.clear();
        List<List<String>> sccList = new ArrayList<>();

        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited.contains(v)) {
                TreeSet<String> component = new TreeSet<>();
                dfs2(v, transposed, visited, component);
                sccList.add(new ArrayList<>(component));
            }
        }

        // 4. Вывод результата
        for (List<String> comp : sccList) {
            for (String vert : comp) {
                System.out.print(vert);
            }
            System.out.println();
        }
    }

    // Первый DFS — формируем порядок выхода
    private static void dfs1(String v, Map<String, List<String>> graph, Set<String> visited, Stack<String> stack) {
        visited.add(v);
        List<String> neighbors = new ArrayList<>(graph.get(v));
        Collections.sort(neighbors);
        for (String neigh : neighbors) {
            if (!visited.contains(neigh)) {
                dfs1(neigh, graph, visited, stack);
            }
        }
        stack.push(v);
    }

    // Второй DFS на транспонированном графе — формирует компоненты
    private static void dfs2(String v, Map<String, List<String>> transposed, Set<String> visited, TreeSet<String> component) {
        visited.add(v);
        component.add(v);
        List<String> neighbors = new ArrayList<>(transposed.get(v));
        Collections.sort(neighbors);
        for (String neigh : neighbors) {
            if (!visited.contains(neigh)) {
                dfs2(neigh, transposed, visited, component);
            }
        }
    }
}
