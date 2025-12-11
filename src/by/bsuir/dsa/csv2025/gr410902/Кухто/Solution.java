package by.bsuir.dsa.csv2025.gr410902.Кухто;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution{

    public static void main(String[] args) {
        //считывание поступающих данных
        Scanner scanner = new Scanner(System.in);
        int M = scanner.nextInt();
        int N = scanner.nextInt();
        int[][] cityMap = new int[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                cityMap[i][j] = scanner.nextInt();
            }
        }
        int numTowers = scanner.nextInt();
        int coverageRadius = scanner.nextInt();

        List<int[]> towers = placeTowers(cityMap, numTowers, coverageRadius);
        int totalCoverage = calculateTotalCoverage(cityMap, towers, coverageRadius);

        //вывод результата
        for (int i = 0; i < towers.size(); i++) {
            System.out.print(towers.get(i)[0] + " " + towers.get(i)[1]);
            if (i < towers.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.println(totalCoverage);
    }
//определение  места для размещения вышки
    public static List<int[]> placeTowers(int[][] cityMap, int numTowers, int coverageRadius) {
        int M = cityMap.length;
        int N = cityMap[0].length;
        boolean[][] covered = new boolean[M][N];
        boolean[][] towerPlaced = new boolean[M][N];
        List<int[]> towerPositions = new ArrayList<>();

        for (int t = 0; t < numTowers; t++) {
            int bestI = -1;
            int bestJ = -1;
            int bestCoverage = -1;

            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    if (towerPlaced[i][j]) continue;
                    int coverage = calculateCoverage(cityMap, covered, i, j, coverageRadius);
                    if (coverage > bestCoverage
                            || (coverage == bestCoverage && (bestI == -1 || i < bestI || (i == bestI && j < bestJ)))) {
                        bestCoverage = coverage;
                        bestI = i;
                        bestJ = j;
                    }
                }
            }

            if (bestI == -1) break;
            towerPositions.add(new int[]{bestI, bestJ});
            towerPlaced[bestI][bestJ] = true;
            markCovered(covered, bestI, bestJ, coverageRadius);
        }

        return towerPositions;
    }
//сколько человек покрывает поставленная вышка
    private static int calculateCoverage(int[][] cityMap, boolean[][] covered, int i, int j, int radius) {
        int M = cityMap.length;
        int N = cityMap[0].length;
        int coverage = 0;

        for (int x = Math.max(0, i - radius); x <= Math.min(M - 1, i + radius); x++) {
            for (int y = Math.max(0, j - radius); y <= Math.min(N - 1, j + radius); y++) {
                int distance = Math.max(Math.abs(x - i), Math.abs(y - j));
                if (distance <= radius && !covered[x][y]) {
                    coverage += cityMap[x][y];
                }
            }
        }

        return coverage;
    }

    private static void markCovered(boolean[][] covered, int i, int j, int radius) {
        int M = covered.length;
        int N = covered[0].length;

        for (int x = Math.max(0, i - radius); x <= Math.min(M - 1, i + radius); x++) {
            for (int y = Math.max(0, j - radius); y <= Math.min(N - 1, j + radius); y++) {
                int distance = Math.max(Math.abs(x - i), Math.abs(y - j));
                if (distance <= radius) {
                    covered[x][y] = true;
                }
            }
        }
    }
//итоговое число людей
    public static int calculateTotalCoverage(int[][] cityMap, List<int[]> towerPositions, int coverageRadius) {
        int M = cityMap.length;
        int N = cityMap[0].length;
        boolean[][] covered = new boolean[M][N];
        int totalCoverage = 0;

        for (int[] tower : towerPositions) {
            int i = tower[0];
            int j = tower[1];

            for (int x = Math.max(0, i - coverageRadius); x <= Math.min(M - 1, i + coverageRadius); x++) {
                for (int y = Math.max(0, j - coverageRadius); y <= Math.min(N - 1, j + coverageRadius); y++) {
                    int distance = Math.max(Math.abs(x - i), Math.abs(y - j));
                    if (distance <= coverageRadius && !covered[x][y]) {
                        totalCoverage += cityMap[x][y];
                        covered[x][y] = true;
                    }
                }
            }
        }

        return totalCoverage;
    }



    @Test
    public void test1() {
        int[][] cityMap = {
                {10, 20, 30},
                {40, 50, 60},
                {70, 80, 90}
        };
        int numTowers = 2;
        int coverageRadius = 1;

        List<int[]> towers = placeTowers(cityMap, numTowers, coverageRadius);
        int totalCoverage = calculateTotalCoverage(cityMap, towers, coverageRadius);

        assertEquals(2, towers.size());
        assertArrayEquals(new int[]{1, 1}, towers.get(0));
        assertArrayEquals(new int[]{0, 0}, towers.get(1));
        assertEquals(450, totalCoverage);
    }

    @Test
    public void test2() {
        int[][] cityMap = {{1}};
        int numTowers = 1;
        int coverageRadius = 0;

        List<int[]> towers = placeTowers(cityMap, numTowers, coverageRadius);
        int totalCoverage = calculateTotalCoverage(cityMap, towers, coverageRadius);

        assertEquals(1, towers.size());
        assertArrayEquals(new int[]{0, 0}, towers.get(0));
        assertEquals(1, totalCoverage);
    }

    @Test
    public void test3() {
        int[][] cityMap = {{5, 5, 5}};
        int numTowers = 1;
        int coverageRadius = 0;

        List<int[]> towers = placeTowers(cityMap, numTowers, coverageRadius);
        int totalCoverage = calculateTotalCoverage(cityMap, towers, coverageRadius);

        assertEquals(1, towers.size());
        assertArrayEquals(new int[]{0, 0}, towers.get(0));
        assertEquals(5, totalCoverage);
    }

    @Test
    public void test4() {
        int[][] cityMap = {{5, 5, 5}};
        int numTowers = 2;
        int coverageRadius = 0;

        List<int[]> towers = placeTowers(cityMap, numTowers, coverageRadius);
        int totalCoverage = calculateTotalCoverage(cityMap, towers, coverageRadius);

        assertEquals(2, towers.size());
        assertArrayEquals(new int[]{0, 0}, towers.get(0));
        assertArrayEquals(new int[]{0, 1}, towers.get(1));
        assertEquals(10, totalCoverage);
    }

    @Test
    public void test5() {
        int[][] cityMap = {
                {1, 2, 3},
                {4, 5, 6}
        };
        int numTowers = 1;
        int coverageRadius = 1;

        List<int[]> towers = placeTowers(cityMap, numTowers, coverageRadius);
        int totalCoverage = calculateTotalCoverage(cityMap, towers, coverageRadius);

        assertEquals(1, towers.size());
        assertArrayEquals(new int[]{0, 1}, towers.get(0));
        assertEquals(21, totalCoverage);
    }
}