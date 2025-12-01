package by.bsuir.dsa.csv2025.gr451003.Платонова;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import java.util.*;
@RunWith(Enclosed.class)
public class Solution {
    public static List<Integer> topologicalSort(int N, int M, int[][] dependencies) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }
        int[] indeg = new int[N];
        for (int i = 0; i < M; i++) {
            int u = dependencies[i][0];
            int v = dependencies[i][1];
            graph.get(u).add(v);
            indeg[v]++;
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            if (indeg[i] == 0) {
                queue.offer(i);
            }
        }
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);
            for (int neighbor : graph.get(node)) {
                indeg[neighbor]--;
                if (indeg[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        if (result.size() != N) {
            return Collections.singletonList(-1);
        }
        return result;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        int[][] dependencies = new int[M][2];
        for (int i = 0; i < M; i++) {
            dependencies[i][0] = scanner.nextInt();
            dependencies[i][1] = scanner.nextInt();
        }
        List<Integer> result = topologicalSort(N, M, dependencies);
        if (result.get(0) == -1) {
            System.out.println("CYCLE");
        } else {
            for (int node : result) {
                System.out.print(node + " ");
            }
        }
        scanner.close();
    }
    public static class SolutionTest {
        @Test
        public void testTopologicalSortValid() {
            int[][] dependencies = {
                    {0, 1},
                    {1, 2},
                    {0, 3}
            };
            List<Integer> result = Solution.topologicalSort(4, 3, dependencies);
            List<Integer> expected = Arrays.asList(0, 1, 3, 2);
            assertEquals(expected, result);
        }
        @Test
        public void testTopologicalSortCycle() {
            int[][] dependencies = {
                    {0, 1},
                    {1, 2},
                    {2, 0}
            };
            List<Integer> result = Solution.topologicalSort(3, 3, dependencies);
            assertEquals(Arrays.asList(-1), result);
        }
        @Test
        public void testTopologicalSortNoDependencies() {
            int[][] dependencies = {};
            List<Integer> result = Solution.topologicalSort(5, 0, dependencies);
            List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4);
            assertEquals(expected, result);
        }
        @Test
        public void testTopologicalSortSingleNode() {
            int[][] dependencies = {};
            List<Integer> result = Solution.topologicalSort(1, 0, dependencies);
            List<Integer> expected = Arrays.asList(0);
            assertEquals(expected, result);
        }
        @Test
        public void testTopologicalSortMultipleValidOrders() {
            int[][] dependencies = {
                    {0, 2},
                    {1, 2},
                    {2, 3}
            };
            List<Integer> result = Solution.topologicalSort(4, 3, dependencies);
            List<Integer> expected1 = Arrays.asList(0, 1, 2, 3);
            List<Integer> expected2 = Arrays.asList(1, 0, 2, 3);
            assertTrue(result.equals(expected1) || result.equals(expected2));
        }
        @Test
        public void testTopologicalSortDisconnectedGraph() {
            int[][] dependencies = {
                    {0, 1},
                    {2, 3}
            };
            List<Integer> result = Solution.topologicalSort(4, 2, dependencies);
            List<Integer> expected = Arrays.asList(0, 2, 1, 3);
            assertEquals(expected, result);
        }
        @Test
        public void testTopologicalSortTwoIndependentChains() {
            int[][] dependencies = {
                    {0, 1},
                    {2, 3}
            };
            List<Integer> result = Solution.topologicalSort(4, 2, dependencies);
            List<Integer> expected = Arrays.asList(0, 2, 1, 3);
            assertEquals(expected, result);
        }
        @Test
        public void testTopologicalSortWithMultipleDependencies() {
            int[][] dependencies = {
                    {0, 2},
                    {1, 2},
                    {2, 3},
                    {1, 3}
            };
            List<Integer> result = Solution.topologicalSort(4, 4, dependencies);
            List<Integer> expected = Arrays.asList(0, 1, 2, 3);
            assertEquals(expected, result);
        }
        @Test
        public void testTopologicalSortLargeGraph() {
            int[][] dependencies = new int[10][2];
            for (int i = 0; i < 9; i++) {
                dependencies[i] = new int[]{i, i + 1};
            }
            List<Integer> result = Solution.topologicalSort(10, 9, dependencies);
            List<Integer> expected = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                expected.add(i);
            }
            assertEquals(expected, result);
        }
        @Test
        public void testTopologicalSortCycleWithMultipleNodes() {
            int[][] dependencies = {
                    {0, 1},
                    {1, 2},
                    {2, 0},
                    {3, 4},
                    {4, 5},
                    {5, 3}
            };
            List<Integer> result = Solution.topologicalSort(6, 6, dependencies);
            assertEquals(Arrays.asList(-1), result);
        }
    }
}