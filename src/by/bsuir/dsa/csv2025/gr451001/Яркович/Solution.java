package by.bsuir.dsa.csv2025.gr451001.Яркович;

import java.util.*;

public class Solution {

    public static long shortestPathWithKEdges(int n, List<int[]> edges, int start, int end, int K) {

        long INF = Long.MAX_VALUE / 4;
        long[][] dist = new long[K + 1][n + 1];

        for (int i = 0; i <= K; i++) {
            Arrays.fill(dist[i], INF);
        }

        dist[0][start] = 0;

        for (int k = 1; k <= K; k++) {
            System.arraycopy(dist[k - 1], 0, dist[k], 0, n + 1);

            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[k - 1][u] + w < dist[k][v]) {
                    dist[k][v] = dist[k - 1][u] + w;
                }
            }
        }

        long ans = Long.MAX_VALUE / 4;
        for (int k = 0; k <= K; k++) ans = Math.min(ans, dist[k][end]);

        return ans >= INF ? -1 : ans;
    }


//Тесты
    static List<int[]> E(int[][] arr) {
        return Arrays.asList(arr);
    }

    static void test(String name, long expected, long actual) {
        if (expected == actual)
            System.out.println(name + ": OK");
        else
            System.out.println(name + ": FAIL (expected " + expected + ", got " + actual + ")");
    }

    public static void main(String[] args) {

        System.out.println("Запуск 10 автоматических тестов...\n");

        test("T1_PositivePath",
                5,
                shortestPathWithKEdges(
                        3,
                        E(new int[][]{
                                {1,2,2},
                                {2,3,3},
                                {1,3,10}
                        }),
                        1, 3, 2
                ));

        test("T2_NotEnoughEdges",
                -1,
                shortestPathWithKEdges(
                        4,
                        E(new int[][]{
                                {1,2,1},
                                {2,3,1},
                                {3,4,1}
                        }),
                        1, 4, 2
                ));

        test("T3_PathWithOneEdge",
                4,
                shortestPathWithKEdges(
                        3,
                        E(new int[][]{
                                {1,2,4},
                                {2,3,5}
                        }),
                        1, 2, 1
                ));

        test("T4_ExactEdgeCount",
                2,
                shortestPathWithKEdges(
                        3,
                        E(new int[][]{
                                {1,2,1},
                                {2,3,1}
                        }),
                        1, 3, 2
                ));

        test("T5_NoEdges",
                -1,
                shortestPathWithKEdges(
                        2,
                        E(new int[][]{}),
                        1, 2, 1
                ));

        test("T6_EqualWeights",
                6,
                shortestPathWithKEdges(
                        4,
                        E(new int[][]{
                                {1,2,2},
                                {2,3,2},
                                {3,4,2},
                                {1,4,8}
                        }),
                        1, 4, 3
                ));

        test("T7_ManyPathsChooseShortest",
                4,
                shortestPathWithKEdges(
                        4,
                        E(new int[][]{
                                {1,2,1},
                                {2,4,5},
                                {1,3,2},
                                {3,4,2},
                                {1,4,10}
                        }),
                        1, 4, 2
                ));

        test("T8_EdgeLimitForcesWorsePath",
                10,
                shortestPathWithKEdges(
                        4,
                        E(new int[][]{
                                {1,2,1},
                                {2,4,1},
                                {1,3,1},
                                {3,4,1},
                                {1,4,10}
                        }),
                        1, 4, 1
                ));

        test("T9_DirectEdgeOneStep",
                4,
                shortestPathWithKEdges(
                        3,
                        E(new int[][]{
                                {1,2,5},
                                {2,3,5},
                                {1,3,4}
                        }),
                        1, 3, 1
                ));

        test("T10_NoPathAtAll",
                -1,
                shortestPathWithKEdges(
                        3,
                        E(new int[][]{
                                {1,2,1},
                                {2,1,1}
                        }),
                        1, 3, 2
                ));

        System.out.println("\nВсе тесты завершены.");
    }
}