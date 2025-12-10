package by.it.group451003.yepanchuntsau.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            // Пустой граф — циклов нет
            System.out.println("no");
            return;
        }

        // Списки смежности и степени захода
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        // Пример строки: "1 -> 2, 1 -> 3, 2 -> 3"
        String[] parts = line.split(",");

        for (String rawPart : parts) {
            String part = rawPart.trim();
            if (part.isEmpty()) {
                continue;
            }

            // "1 -> 2" -> "1->2"
            part = part.replaceAll("\\s+", "");

            String[] uv = part.split("->");
            if (uv.length != 2) {
                continue; // на всякий случай игнорируем кривой кусок
            }

            String from = uv[0];
            String to = uv[1];

            // Регистрируем вершины
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            indegree.putIfAbsent(from, 0);
            indegree.putIfAbsent(to, 0);

            // Добавляем ребро from -> to
            graph.get(from).add(to);
            indegree.put(to, indegree.get(to) + 1);
        }

        // Алгоритм Кана: если можем "съесть" все вершины — цикла нет
        Deque<String> queue = new ArrayDeque<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) {
                queue.add(e.getKey());
            }
        }

        int visited = 0;

        while (!queue.isEmpty()) {
            String v = queue.poll();
            visited++;

            for (String to : graph.getOrDefault(v, Collections.emptyList())) {
                indegree.put(to, indegree.get(to) - 1);
                if (indegree.get(to) == 0) {
                    queue.add(to);
                }
            }
        }

        // Если прошли все вершины — циклов нет
        if (visited == indegree.size()) {
            System.out.println("no");
        } else {
            System.out.println("yes");
        }
    }
}
