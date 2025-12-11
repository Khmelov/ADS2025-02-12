package by.bsuir.dsa.csv2025.gr410901.Гаркуша;

import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение первой строки - начальная и конечная вершины
        String verticesLine = scanner.nextLine().trim();
        String[] vertices = verticesLine.split("\\s+");
        String startVertex = vertices[0];
        String endVertex = vertices[1];

        // Чтение остальных строк - рёбра графа
        Map<String, List<Edge>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();
        allVertices.add(startVertex);
        allVertices.add(endVertex);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }

            // Парсинг строки
            String[] parts = line.split("\\s+-\\s+");
            if (parts.length < 2) {
                continue;
            }

            String from = parts[0].trim();
            String[] toAndWeight = parts[1].split("\\s+");
            String to = toAndWeight[0].trim();
            int weight = Integer.parseInt(toAndWeight[1].trim());

            allVertices.add(from);
            allVertices.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(new Edge(to, weight));

            // Добавляем конечную вершину в граф
            graph.putIfAbsent(to, new ArrayList<>());
        }
        scanner.close();

        // Поиск кратчайшего пути алгоритмом Дейкстры
        Result result = findShortestPath(graph, startVertex, endVertex, allVertices);

        if (result.distance != Integer.MAX_VALUE) {
            String pathString = String.join(" - ", result.path);
            System.out.println(pathString + " " + result.distance);
        } else {
            System.out.println("Путь не найден");
        }
    }

    // Класс для представления ребра графа
    static class Edge {
        String to;
        int weight;
        Edge(String to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // Класс для узла в очереди с приоритетом
    static class Node implements Comparable<Node> {
        String name;
        int dist;
        Node(String name, int dist) {
            this.name = name;
            this.dist = dist;
        }
        public int compareTo(Node other) {
            return Integer.compare(this.dist, other.dist);
        }
    }

    // Класс для возврата результата
    static class Result {
        List<String> path;
        int distance;
        Result(List<String> path, int distance) {
            this.path = path;
            this.distance = distance;
        }
    }

    // Алгоритм Дейкстры
    static Result findShortestPath(Map<String, List<Edge>> graph, String start, String end, Set<String> vertices) {
        Map<String, Integer> dist = new HashMap<>();    // Расстояния от начальной вершины до всех остальных
        Map<String, String> prev = new HashMap<>();     // Предшественники для восстановления пути
        PriorityQueue<Node> pq = new PriorityQueue<>(); // Очередь с приоритетом для вершин

        for (String v : vertices) {
            dist.put(v, Integer.MAX_VALUE);
        }
        dist.put(start, 0);
        pq.add(new Node(start, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String u = current.name;

            if (u.equals(end)) break;
            if (current.dist > dist.get(u)) continue;

            for (Edge edge : graph.get(u)) {
                String v = edge.to;
                int newDist = dist.get(u) + edge.weight;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(new Node(v, newDist));
                }
            }
        }

        List<String> path = buildPath(prev, start, end);
        return new Result(path, dist.get(end));
    }

    // Восстановление пути от конечной вершины к начальной
    private static List<String> buildPath(Map<String, String> prev, String start, String end) {
        List<String> path = new ArrayList<>();
        if (prev.get(end) == null && !end.equals(start)) {
            return path;
        }

        for (String at = end; at != null; at = prev.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        if (path.isEmpty() || !path.get(0).equals(start)) {
            return new ArrayList<>();
        }
        return path;
    }
}

