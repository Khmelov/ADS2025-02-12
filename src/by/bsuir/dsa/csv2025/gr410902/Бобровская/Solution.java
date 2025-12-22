package by.bsuir.dsa.csv2025.gr410902.Бобровская;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int M = scanner.nextInt();
        int N = scanner.nextInt();
        int K = scanner.nextInt();
        int T = scanner.nextInt();

        int[][] airMap = new int[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                airMap[i][j] = scanner.nextInt();
            }
        }

        List<int[]> hotZones = findHotZones(airMap, K, T);
        double[][] normalizedMap = normalizeAirMap(airMap);

        for (int[] coord : hotZones) {
            System.out.println(coord[0] + " " + coord[1]);
        }

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%.2f", normalizedMap[i][j]);
                if (j < N - 1) System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static double[][] normalizeAirMap(int[][] airMap) {
        int M = airMap.length;
        int N = airMap[0].length;
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;

        for (int[] row : airMap) {
            for (int val : row) {
                minValue = Math.min(minValue, val);
                maxValue = Math.max(maxValue, val);
            }
        }

        double[][] normalizedMap = new double[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                normalizedMap[i][j] = (double)(airMap[i][j] - minValue) / (maxValue - minValue);
            }
        }
        return normalizedMap;
    }

    public static List<int[]> findHotZones(int[][] airMap, int K, int T) {
        int M = airMap.length;
        int N = airMap[0].length;
        double maxAvg = Double.NEGATIVE_INFINITY;
        List<int[]> result = new ArrayList<>();

        for (int i = 0; i <= M - K; i++) {
            for (int j = 0; j <= N - K; j++) {
                double sum = 0;
                for (int x = 0; x < K; x++) {
                    for (int y = 0; y < K; y++) {
                        sum += airMap[i + x][j + y];
                    }
                }
                double avg = sum / (K * K);
                if (avg > maxAvg) {
                    maxAvg = avg;
                    result.clear();
                    if (avg >= T) result.add(new int[]{i, j});
                } else if (avg == maxAvg && avg >= T) {
                    result.add(new int[]{i, j});
                }
            }
        }

        return result;
    }

    @Test(timeout = 2000)
    public void checkNormalizeAirMap() {
        int[][] airMap = {
                {100, 200},
                {300, 400}
        };
        double[][] normalized = normalizeAirMap(airMap);

        boolean ok =
                Math.abs(normalized[0][0] - 0.0) < 0.0001 &&
                        Math.abs(normalized[0][1] - 0.3333) < 0.0001 &&
                        Math.abs(normalized[1][0] - 0.6666) < 0.0001 &&
                        Math.abs(normalized[1][1] - 1.0) < 0.0001;

        assertTrue("normalizeAirMap failed", ok);
    }
}
