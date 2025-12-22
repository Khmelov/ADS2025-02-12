package by.bsuir.dsa.csv2025.gr410901.Букшта;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertArrayEquals;

import java.util.Scanner;

public class Solution {

    public int[][] compressCoordinates(int[][] coords) {
        int n = coords.length;

        Set<Integer> setX = new HashSet<>();
        Set<Integer> setY = new HashSet<>();
        for (int[] c : coords) {
            setX.add(c[0]);
            setY.add(c[1]);
        }

        List<Integer> sortedX = new ArrayList<>(setX);
        List<Integer> sortedY = new ArrayList<>(setY);
        Collections.sort(sortedX);
        Collections.sort(sortedY);

        Map<Integer, Integer> mapX = new HashMap<>();
        Map<Integer, Integer> mapY = new HashMap<>();
        for (int i = 0; i < sortedX.size(); i++) mapX.put(sortedX.get(i), i);
        for (int i = 0; i < sortedY.size(); i++) mapY.put(sortedY.get(i), i);

        int[][] result = new int[n][2];
        for (int i = 0; i < n; i++) {
            result[i][0] = mapX.get(coords[i][0]);
            result[i][1] = mapY.get(coords[i][1]);
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[][] coords = new int[n][2];

        for (int i = 0; i < n; i++) {
            coords[i][0] = scanner.nextInt();
            coords[i][1] = scanner.nextInt();
        }

        Solution cc = new Solution();
        int[][] result = cc.compressCoordinates(coords);

        for (int i = 0; i < n; i++) {
            System.out.println(result[i][0] + " " + result[i][1]);
        }

        scanner.close();
    }

    @Test(timeout = 2000)
    public void testExample() {
        Solution cc = new Solution();
        int[][] input = {{100,200},{50,300},{100,300},{50,200},{200,100}};
        int[][] expected = {{1,1},{0,2},{1,2},{0,1},{2,0}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testAllSame() {
        Solution cc = new Solution();
        int[][] input = {{1,1},{1,1},{1,1}};
        int[][] expected = {{0,0},{0,0},{0,0}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testIncreasing() {
        Solution cc = new Solution();
        int[][] input = {{1,1},{2,2},{3,3},{4,4}};
        int[][] expected = {{0,0},{1,1},{2,2},{3,3}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testDecreasing() {
        Solution cc = new Solution();
        int[][] input = {{4,4},{3,3},{2,2},{1,1}};
        int[][] expected = {{3,3},{2,2},{1,1},{0,0}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testRandomOrder() {
        Solution cc = new Solution();
        int[][] input = {{10,5},{20,3},{10,3},{20,5}};
        int[][] expected = {{0,1},{1,0},{0,0},{1,1}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testSinglePoint() {
        Solution cc = new Solution();
        int[][] input = {{42,42}};
        int[][] expected = {{0,0}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testNegativeCoords() {
        Solution cc = new Solution();
        int[][] input = {{-5,-10},{0,0},{-5,0}};
        int[][] expected = {{0,0},{1,1},{0,1}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testLargeCoords() {
        Solution cc = new Solution();
        int[][] input = {{1000000,500000},{500000,1000000},{1000000,1000000}};
        int[][] expected = {{1,0},{0,1},{1,1}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testMixedCoords() {
        Solution cc = new Solution();
        int[][] input = {{3,1},{1,4},{2,2},{3,3}};
        int[][] expected = {{2,0},{0,3},{1,1},{2,2}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }

    @Test(timeout = 2000)
    public void testDuplicates() {
        Solution cc = new Solution();
        int[][] input = {{1,2},{2,3},{1,2},{3,1}};
        int[][] expected = {{0,1},{1,2},{0,1},{2,0}};
        assertArrayEquals(expected, cc.compressCoordinates(input));
    }
}