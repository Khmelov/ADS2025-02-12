package by.it.group451003.plyushchevich.lesson13;

import java.util.*;

/**
 * Выводит топологическую сортировку (в одну строку, вершины через пробел).
 * При равнозначности (несколько вершин с indegree == 0) выбирает вершину
 * в лексикографическом порядке, но с поправкой: если обе вершины — целые числа,
 * сравнивает их как числа (чтобы "10" > "2", а не "10" < "2").
 */
public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();
        sc.close();

        if (line.isEmpty()) {
            System.out.println();
            return;
        }

        Comparator<String> vertexComparator = (a, b) -> {
            boolean aNum = isInteger(a);
            boolean bNum = isInteger(b);
            if (aNum && bNum) {
                long la = Long.parseLong(a);
                long lb = Long.parseLong(b);
                return Long.compare(la, lb);
            }
            return a.compareTo(b);
        };

        Set<String> nodes = new HashSet<>();
        Map<String, Set<String>> adj = new HashMap<>();

        String[] parts = line.split(",");
        for (String raw : parts) {
            String e = raw.trim();
            if (e.isEmpty()) continue;
            int arrow = e.indexOf("->");
            if (arrow < 0) continue;

            String from = e.substring(0, arrow).trim();
            String to = e.substring(arrow + 2).trim();

            if (from.isEmpty() || to.isEmpty()) continue;

            nodes.add(from);
            nodes.add(to);

            adj.computeIfAbsent(from, k -> new HashSet<>()).add(to);
        }

        Map<String, Integer> indeg = new HashMap<>();
        for (String v : nodes) indeg.put(v, 0);

        for (Map.Entry<String, Set<String>> entry : adj.entrySet()) {
            for (String to : entry.getValue()) {
                indeg.put(to, indeg.getOrDefault(to, 0) + 1);
            }
        }

        // PriorityQueue с нашим компаратором — будет выдавать минимальную вершину по нашим правилам
        PriorityQueue<String> pq = new PriorityQueue<>(vertexComparator);

        for (String v : nodes) {
            if (indeg.getOrDefault(v, 0) == 0) pq.add(v);
        }

        // Список результата топологической сортировки
        List<String> topo = new ArrayList<>();

        while (!pq.isEmpty()) {
            // Извлекаем вершину с нулевой входящей степенью и минимальной по нашему компаратору
            String u = pq.poll();
            topo.add(u);

            Set<String> neighbors = adj.get(u);
            if (neighbors == null) continue;

            // Чтобы поведение было стабильным и предсказуемым (для отладки и тестов),
            // перебираем соседей в порядке, определяемом тем же компаратором.
            List<String> sortedNeighbors = new ArrayList<>(neighbors);
            sortedNeighbors.sort(vertexComparator);

            for (String v : sortedNeighbors) {
                indeg.put(v, indeg.get(v) - 1);
                if (indeg.get(v) == 0) pq.add(v);
            }
        }

        // Тесты ожидают корректный DAG, но на всякий случай — если цикл есть,
        // мы всё равно выведем то, что смогли получить (частичный порядок).
        StringJoiner sj = new StringJoiner(" ");
        for (String s : topo) sj.add(s);
        System.out.println(sj.toString());
    }


    private static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) return false;
        int i = 0;
        if (s.charAt(0) == '-') {
            if (s.length() == 1) return false;
            i = 1;
        }
        for (; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
}
