package by.bsuir.dsa.csv2025.gr410901.Гаркуша;

import java.util.*;
import java.io.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import static org.junit.Assert.*;

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

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SolutionTest.Test1.class,
        SolutionTest.Test2.class,
        SolutionTest.Test3.class,
        SolutionTest.Test4.class,
        SolutionTest.Test5.class,
        SolutionTest.Test6.class,
        SolutionTest.Test7.class,
        SolutionTest.Test8.class,
        SolutionTest.Test9.class,
        SolutionTest.Test10.class,
})
class SolutionTest {

    public static class Test1 {
        @Test
        public void test() {
            testCase("A C\nA - B 5\nB - C 3", "A - B - C 8");
        }
    }

    public static class Test2 {
        @Test
        public void test() {
            testCase("A D\nA - B 4\nA - C 1\nB - D 1\nC - B 2\nC - D 5", "A - C - B - D 4");
        }
    }

    public static class Test3 {
        @Test
        public void test() {
            testCase("A B\nA - B 10\nA - C 2\nC - B 1\nB - D 3", "A - C - B 3");
        }
    }

    public static class Test4 {
        @Test
        public void test() {
            testCase("A D\nA - B 2\nB - C 3\nC - A 1\nC - D 4", "A - B - C - D 9");
        }
    }

    public static class Test5 {
        @Test
        public void test() {
            testCase("Start End\nStart - A 3\nStart - B 1\nA - End 2\nB - End 4\nB - A 1", "Start - B - A - End 4");
        }
    }

    public static class Test6 {
        @Test
        public void test() {
            testCase("X End\nX - Y 5\nY - Z 2\nA - B 10\nZ - End 1", "X - Y - Z - End 8");
        }
    }

    public static class Test7 {
        @Test
        public void test() {
            testCase("1 5\n1 - 2 10\n1 - 3 1\n2 - 4 1\n3 - 2 1\n3 - 4 20\n4 - 5 2", "1 - 3 - 2 - 4 - 5 5");
        }
    }

    public static class Test8 {
        @Test
        public void test() {
            testCase("A F\nA - B 2\nB - C 1\nC - D 3\nD - E 1\nE - F 2", "A - B - C - D - E - F 9");
        }
    }

    public static class Test9 {
        @Test
        public void test() {
            testCase("A F\nA - B 4\nA - C 2\nB - D 5\nC - D 1\nC - E 3\nD - F 2\nE - F 4", "A - C - D - F 5");
        }
    }

    public static class Test10 {
        @Test
        public void test() {
            testCase("Home Work\nHome - Shop 5\nHome - Park 2\nPark - Shop 1\nShop - Work 3\nPark - Work 6", "Home - Park - Shop - Work 6");
        }
    }

    private static void testCase(String input, String expectedOutput) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(out);

            InputStream oldIn = System.in;
            PrintStream oldOut = System.out;

            try {
                System.setIn(in);
                System.setOut(printStream);
                Solution.main(new String[]{});
            } finally {
                System.setIn(oldIn);
                System.setOut(oldOut);
            }

            String result = out.toString().trim();
            assertEquals(expectedOutput, result);
        } catch (Exception e) {
            fail("Тест завершился с ошибкой: " + e.getMessage());
        }
    }
}