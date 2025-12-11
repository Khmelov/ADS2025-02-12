package by.it.group410902.plekhova.lesson13;

import java.util.*;
// топологическая сортировка
public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = "";
        if (sc.hasNextLine()) {
            input = sc.nextLine();
        }
        sc.close();

        input = input == null ? "" : input.trim();
        if (input.isEmpty()) {
            // для пустого ввода — печатаем пустую строку
            System.out.println("");
            return;
        }

        // Нормализуем разделители и разбиваем по запятым
        input = input.replaceAll("\\s*,\\s*", ",");
        String[] parts = input.split(",");

        // граф: вершина -> множество соседей
        Map<String, Set<String>> graph = new HashMap<>();
        // входящие степени
        Map<String, Integer> indegree = new HashMap<>();

        for (String p : parts) {
            if (p == null) continue;
            p = p.trim(); // удаляем пробелы в начале и конце строки
            if (p.isEmpty()) continue;

            // ожидаем формат "X -> Y"
            String[] arrow = p.split("->");
            if (arrow.length != 2) continue;

            String from = arrow[0].trim();
            String to = arrow[1].trim();
            if (from.isEmpty() || to.isEmpty()) continue;

            //добавляет пару «ключ-значение» в карту только если этот ключ уникальный
            graph.putIfAbsent(from, new TreeSet<>());
            graph.putIfAbsent(to, new TreeSet<>());

            // если ребро новое — добавляем и увеличиваем indegree
            if (graph.get(from).add(to)) {
                indegree.put(to, indegree.getOrDefault(to, 0) + 1);
            }
            // убедимся, что from присутствует в indegree
            indegree.putIfAbsent(from, indegree.getOrDefault(from, 0));
            indegree.putIfAbsent(to, indegree.getOrDefault(to, 0)); // на всякий случай
        }

        // Если после парсинга нет вершин — вывод пустой строки
        if (indegree.isEmpty()) {
            System.out.println("");
            return;
        }

        // добавляет в очередь с приоритетом ключ, если его значение равно 0
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) pq.offer(e.getKey());
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String cur = pq.poll();
            result.add(cur);

            //защищаемся на случай, если у cur нет записи в graph
            Set<String> neigh = graph.getOrDefault(cur, Collections.emptySet());
            for (String nb : neigh) {
                indegree.put(nb, indegree.get(nb) - 1);
                if (indegree.get(nb) == 0) {
                    pq.offer(nb);
                }
            }
        }
        System.out.println(String.join(" ", result));
    }
}