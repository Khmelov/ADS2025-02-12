package by.it.group410902.harkavy.lesson13;
import java.util.*;
import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();

        // граф: для каждой вершины список её выходящих рёбер
        Map<String, List<String>> graph = new HashMap<>();
        // степень захода для каждой вершины
        Map<String, Integer> indegree = new HashMap<>();
        // множество всех вершин
        Set<String> vertices = new HashSet<>();

        if (!line.isEmpty()) {
            // "0 -> 2, 1 -> 3" → ["0 -> 2", " 1 -> 3"]
            String[] parts = line.split(",");
            for (String part : parts) {
                String edge = part.trim();
                if (edge.isEmpty()) continue;

                String[] uv = edge.split("->");
                if (uv.length != 2) continue;

                String u = uv[0].trim();
                String v = uv[1].trim();

                vertices.add(u);
                vertices.add(v);

                // добавляем ребро u -> v
                graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);

                // инициализируем степени захода
                indegree.putIfAbsent(u, indegree.getOrDefault(u, 0));
                indegree.put(v, indegree.getOrDefault(v, 0) + 1);
            }
        }

        // вершины с нулевой степенью захода — в приоритетную очередь (лексикографический порядок)
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : vertices) {
            if (indegree.getOrDefault(v, 0) == 0) {
                queue.add(v);
            }
        }

        List<String> topo = new ArrayList<>();

        // алгоритм Кана
        while (!queue.isEmpty()) {
            String v = queue.poll();
            topo.add(v);

            List<String> neighbours = graph.get(v);
            if (neighbours != null) {
                for (String to : neighbours) {
                    int deg = indegree.get(to) - 1;
                    indegree.put(to, deg);
                    if (deg == 0) {
                        queue.add(to);
                    }
                }
            }
        }

        // вывод
        for (int i = 0; i < topo.size(); i++) {
            System.out.print(topo.get(i));
            if (i + 1 < topo.size()) System.out.print(" ");
        }
    }
}