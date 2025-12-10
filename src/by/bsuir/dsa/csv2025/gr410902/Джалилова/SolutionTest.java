package by.bsuir.dsa.csv2025.gr410902.Джалилова;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

 public class SolutionTest {

    // ====================== TESTS ======================
    @Test
    public void testSimpleCase() {
        Solution sol = new Solution();
        int[][] queries = { {2, 4}, {3, 9}, {4, 7} };
        List<Boolean> result = sol.areConnected(10, 1, queries);
        assertEquals(Arrays.asList(true, true, false), result);
    }

    @Test
    public void testNoConnections() {
        Solution sol = new Solution();
        int[][] queries = { {2, 4}, {5, 7} };
        List<Boolean> result = sol.areConnected(10, 10, queries);
        assertEquals(Arrays.asList(false, false), result);
    }

    @Test
    public void testAllConnected() {
        Solution sol = new Solution();
        int[][] queries = { {2, 4}, {5, 10}, {3, 9} };
        List<Boolean> result = sol.areConnected(10, 0, queries);
        assertEquals(Arrays.asList(true, true, true), result);
    }

    @Test
    public void testSingleQuery() {
        Solution sol = new Solution();
        int[][] queries = { {6, 12} };
        List<Boolean> result = sol.areConnected(12, 1, queries);
        assertEquals(Arrays.asList(true), result);
    }

    @Test
    public void testLargeValues() {
        Solution sol = new Solution();
        int[][] queries = { {90, 45}, {100, 50}, {96, 24} };
        List<Boolean> result = sol.areConnected(100, 1, queries);
        assertEquals(Arrays.asList(true, true, true), result);
    }

    @Test
    public void testThresholdEqualsNMinusOne() {
        Solution sol = new Solution();
        int[][] queries = { {1, 2}, {2, 3}, {3, 4} };
        List<Boolean> result = sol.areConnected(5, 4, queries);
        assertEquals(Arrays.asList(false, false, false), result);
    }

    @Test
    public void testSingleElementN() {
        Solution sol = new Solution();
        int[][] queries = { {1, 1} };
        List<Boolean> result = sol.areConnected(1, 0, queries);
        assertEquals(Arrays.asList(true), result);
    }

    @Test
    public void testSamePairsQuery() {
        Solution sol = new Solution();
        int[][] queries = { {5, 5}, {8, 8}, {3, 3} };
        List<Boolean> result = sol.areConnected(10, 1, queries);
        assertEquals(Arrays.asList(true, true, true), result);
    }

    @Test
    public void testMultipleDisconnectedComponents() {
        Solution sol = new Solution();
        int[][] queries = { {2, 3}, {4, 8}, {9, 3}, {7, 14} };
        List<Boolean> result = sol.areConnected(14, 3, queries);
        assertEquals(Arrays.asList(false, true, false, true), result);
    }

    @Test
    public void testManyQueries() {
        Solution sol = new Solution();
        int[][] queries = {
                {2,4}, {3,9}, {4,6}, {6,12}, {5,10},
                {7,14}, {8,16}, {9,18}, {10,20}, {11,22}
        };
        List<Boolean> result = sol.areConnected(22, 1, queries);
        assertEquals(
                Arrays.asList(true, true, true, true, true, true, true, true, true, true),
                result
        );
    }

    // ====================== MAIN ======================
    public static void main(String[] args) {
        Solution sol = new Solution();
        Scanner sc = new Scanner(System.in);

        for (int test = 1; test <= 10; test++) {
            System.out.println("\n=== Test Case " + test + " ===");

            System.out.print("Enter n and threshold: ");
            int n = sc.nextInt();
            int threshold = sc.nextInt();

            System.out.print("Enter number of queries: ");
            int q = sc.nextInt();

            int[][] queries = new int[q][2];
            System.out.println("Enter queries (a b):");
            for (int i = 0; i < q; i++) {
                queries[i][0] = sc.nextInt();
                queries[i][1] = sc.nextInt();
            }

            List<Boolean> result = sol.areConnected(n, threshold, queries);

            System.out.println("Results:");
            for (boolean r : result) {
                System.out.println(r);
            }
        }

        sc.close();
    }
}

// ====================== OTHER CLASSES ======================



  class Solution {
    public List<Boolean> areConnected(int n, int threshold, int[][] queries) {
        UnionFind uf = new UnionFind(n + 1);

        for (int div = threshold + 1; div <= n; div++) {
            for (int multiple = 2 * div; multiple <= n; multiple += div) {
                uf.union(div, multiple);
            }
        }

        List<Boolean> res = new ArrayList<>();
        for (int[] q : queries) {
            res.add(uf.find(q[0]) == uf.find(q[1]));
        }
        return res;
    }
}
