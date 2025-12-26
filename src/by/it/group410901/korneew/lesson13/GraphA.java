package by.it.group410901.korneew.lesson13;
import java.util.*;

public class GraphA {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            // Парсинг входной строки
            Map<String, List<String>> graph = new HashMap<>();
            Set<String> allVertices = new HashSet<>();

            // Разбиваем на пары "вершина -> список смежных"
            String[] edges = input.split(", ");
            for (String edge : edges) {
                String[] parts = edge.split(" -> ");
                String from = parts[0];
                String to = parts[1];

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allVertices.add(from);
                allVertices.add(to);
            }

            // Вычисляем полустепени захода
            Map<String, Integer> inDegree = new HashMap<>();
            for (String vertex : allVertices) {
                inDegree.put(vertex, 0);
            }

            for (List<String> neighbors : graph.values()) {
                for (String neighbor : neighbors) {
                    inDegree.put(neighbor, inDegree.get(neighbor) + 1);
                }
            }

            // Топологическая сортировка (алгоритм Кана)
            PriorityQueue<String> queue = new PriorityQueue<>(); // для лексикографического порядка
            List<String> result = new ArrayList<>();

            // Добавляем вершины с нулевой полустепенью захода
            for (String vertex : allVertices) {
                if (inDegree.get(vertex) == 0) {
                    queue.offer(vertex);
                }
            }

            while (!queue.isEmpty()) {
                String current = queue.poll();
                result.add(current);

                // Уменьшаем полустепени захода для смежных вершин
                if (graph.containsKey(current)) {
                    for (String neighbor : graph.get(current)) {
                        inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                        if (inDegree.get(neighbor) == 0) {
                            queue.offer(neighbor);
                        }
                    }
                }
            }

            // Вывод результата
            for (int i = 0; i < result.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(result.get(i));
            }
            System.out.println();
        }
    }

