package by.it.group451003.yepanchuntsau.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            return;
        }

        // Список смежности и степени захода
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        // Разбиваем по запятым: "0 -> 2", " 1 -> 3", ...
        String[] parts = line.split(",");

        for (String rawPart : parts) {
            String part = rawPart.trim();
            if (part.isEmpty()) {
                continue;
            }

            // убираем все пробелы: "0 -> 2" → "0->2"
            part = part.replaceAll("\\s+", "");
            // теперь формат "u->v"
            String[] uv = part.split("->");
            if (uv.length != 2) {
                continue; // на всякий случай
            }

            String from = uv[0];
            String to = uv[1];

            // регистрируем вершины
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            indegree.putIfAbsent(from, 0);
            indegree.putIfAbsent(to, 0);

            // добавляем ребро from -> to
            graph.get(from).add(to);
            indegree.put(to, indegree.get(to) + 1);
        }

        // Топологическая сортировка с приоритетом по алфавиту
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : indegree.keySet()) {
            if (indegree.get(v) == 0) {
                pq.add(v);
            }
        }

        List<String> order = new ArrayList<>();

        while (!pq.isEmpty()) {
            String v = pq.poll();
            order.add(v);

            for (String to : graph.getOrDefault(v, Collections.emptyList())) {
                indegree.put(to, indegree.get(to) - 1);
                if (indegree.get(to) == 0) {
                    pq.add(to);
                }
            }
        }

        // вывод в одну строку через пробел
        System.out.println(String.join(" ", order));
    }
}
