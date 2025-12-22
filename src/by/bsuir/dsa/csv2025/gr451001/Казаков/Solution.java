package by.bsuir.dsa.csv2025.gr451001.Казаков;

import java.util.*;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class Solution {

    public static List<Integer> topologicalDFS(int n, int[][] edges) {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].add(v);
        }
        
        for (int i = 0; i < n; i++) {
            Collections.sort(graph[i]);
        }

        int[] visited = new int[n];
        List<Integer> result = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            if (visited[i] == 0) {
                if (!dfs(i, graph, visited, result)) {
                    return null;
                }
            }
        }

        Collections.reverse(result);
        return result;
    }

    private static boolean dfs(int node, List<Integer>[] graph, int[] visited, List<Integer> result) {
        if (visited[node] == 2) {
            return false;
        }
        if (visited[node] == 1) {
            return true;
        }

        visited[node] = 2;

        for (int neighbor : graph[node]) {
            if (!dfs(neighbor, graph, visited, result)) {
                return false;
            }
        }

        visited[node] = 1;
        result.add(node);
        return true;
    }

    public static List<Integer> topologicalKahn(int n, int[][] edges) {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        int[] inDegree = new int[n];

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].add(v);
            inDegree[v]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> result = new ArrayList<>();
        int count = 0;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);

            for (int neighbor : graph[node]) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
            count++;
        }

        if (count != n) {
            return null;
        }

        return result;
    }

    private static boolean isTopologicalOrderValid(int n, int[][] edges, List<Integer> order) {
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            position.put(order.get(i), i);
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            if (position.get(u) >= position.get(v)) {
                return false;
            }
        }
        return true;
    }

    public static class SolutionTest {
        @Test
        public void test_1() {
            String input = """
                    3 2
                    0 1
                    1 2
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(0, 1, 2);
            List<Integer> expectedKahn = Arrays.asList(0, 1, 2);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

        @Test
        public void test_2() {
            String input = """
                    4 3
                    0 1
                    0 2
                    0 3
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(0, 3, 2, 1);
            List<Integer> expectedKahn = Arrays.asList(0, 1, 2, 3);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

        @Test
        public void test_3() {
            String input = """
                    4 4
                    0 1
                    0 2
                    1 3
                    2 3
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(0, 2, 1, 3);
            List<Integer> expectedKahn = Arrays.asList(0, 1, 2, 3);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }
        @Test
        public void test_4() {
            String input = """
                    6 5
                    0 1
                    1 2
                    2 3
                    3 4
                    4 5
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(0, 1, 2, 3, 4, 5);
            List<Integer> expectedKahn = Arrays.asList(0, 1, 2, 3, 4, 5);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

        @Test
        public void test_5() {
            String input = """
                    5 4
                    0 1
                    0 2
                    1 3
                    2 4
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(0, 2, 4, 1, 3);
            List<Integer> expectedKahn = Arrays.asList(0, 1, 2, 3, 4);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

        @Test
        public void test_6() {
            String input = """
                    4 3
                    0 1
                    1 2
                    1 3
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(0, 1, 3, 2);
            List<Integer> expectedKahn = Arrays.asList(0, 1, 2, 3);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

        @Test
        public void test_7() {
            String input = """
                    5 5
                    0 1
                    0 2
                    1 3
                    2 3
                    3 4
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(0, 2, 1, 3, 4);
            List<Integer> expectedKahn = Arrays.asList(0, 1, 2, 3, 4);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

        @Test
        public void test_8() {
            String input = """
                    3 1
                    0 1
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(2, 0, 1);
            List<Integer> expectedKahn = Arrays.asList(0, 2, 1);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

        @Test
        public void test_9() {
            String input = """
                    6 4
                    0 1
                    0 2
                    3 4
                    4 5
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(3, 4, 5, 0, 2, 1);
            List<Integer> expectedKahn = Arrays.asList(0, 3, 1, 2, 4, 5);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

        @Test
        public void test_10() {
            String input = """
                    4 2
                    0 2
                    1 3
                    """;
            Scanner scanner = new Scanner(input);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int[][] edges = new int[m][2];
            for (int i = 0; i < m; i++) {
                edges[i][0] = scanner.nextInt();
                edges[i][1] = scanner.nextInt();
            }
            scanner.close();

            List<Integer> expectedDFS = Arrays.asList(1, 3, 0, 2);
            List<Integer> expectedKahn = Arrays.asList(0, 1, 2, 3);

            List<Integer> actualDFS = topologicalDFS(n, edges);
            List<Integer> actualKahn = topologicalKahn(n, edges);

            assertEquals("DFS результат не совпадает с ожидаемым", expectedDFS, actualDFS);
            assertEquals("Kahn результат не совпадает с ожидаемым", expectedKahn, actualKahn);
        }

    }
    
    public static void runTests() {
        Result result = JUnitCore.runClasses(SolutionTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println("FAIL: " + failure.toString());
        }
        System.out.println("Tests run: " + result.getRunCount());
        System.out.println("Tests failed: " + result.getFailureCount());
        System.out.println("All tests passed: " + result.wasSuccessful());
    }
    
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--test")) {
            runTests();
        } else {
            runInteractive();
        }
    }

    private static void runInteractive() {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = scanner.nextInt();
            edges[i][1] = scanner.nextInt();
        }

        List<Integer> dfsResult = topologicalDFS(n, edges);
        List<Integer> kahnResult = topologicalKahn(n, edges);

        if (dfsResult == null || kahnResult == null) {
            System.out.println("Graph contains cycle, topological sorting impossible");
        } else {
            for (int i = 0; i < dfsResult.size(); i++) {
                System.out.print(dfsResult.get(i));
                if (i < dfsResult.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            for (int i = 0; i < kahnResult.size(); i++) {
                System.out.print(kahnResult.get(i));
                if (i < kahnResult.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        scanner.close();
    }
}