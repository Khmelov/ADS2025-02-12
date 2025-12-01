package by.bsuir.dsa.csv2025.gr451002.Горбач;

import org.junit.Test;

import java.util.*;
import java.io.*;

import static org.junit.Assert.assertEquals;

public class Solution {
    private List<Package> packages;

    public Solution() {
        this.packages = new ArrayList<>();
    }

    public int addPackage(int id, int priority, double distance, int estimatedTime) {
        Package newPackage = new Package(id, priority, distance, estimatedTime);

        if (packages.isEmpty()) {
            packages.add(newPackage);
            return 0;
        }

        int insertIndex = 0;
        while (insertIndex < packages.size()) {
            Package current = packages.get(insertIndex);

            if (newPackage.priority < current.priority) {
                break;
            } else if (newPackage.priority > current.priority) {
                insertIndex++;
                continue;
            } else {
                if (newPackage.distance < current.distance) {
                    break;
                } else if (newPackage.distance > current.distance) {
                    insertIndex++;
                    continue;
                } else {
                    if (newPackage.estimatedTime < current.estimatedTime) {
                        break;
                    } else if (newPackage.estimatedTime > current.estimatedTime) {
                        insertIndex++;
                        continue;
                    } else {
                        insertIndex++;
                        continue;
                    }
                }
            }
        }

        packages.add(insertIndex, newPackage);
        return insertIndex;
    }

    // Вспомогательный метод для получения ID всех пакетов в порядке списка
    public List<Integer> getPackageIds() {
        List<Integer> ids = new ArrayList<>();
        for (Package pkg : packages) {
            ids.add(pkg.id);
        }
        return ids;
    }

    private static class Package {
        int id;
        int priority;
        double distance;
        int estimatedTime;

        public Package(int id, int priority, double distance, int estimatedTime) {
            this.id = id;
            this.priority = priority;
            this.distance = distance;
            this.estimatedTime = estimatedTime;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input: ");

        String firstLine;
        do {
            firstLine = scanner.nextLine().trim();
        } while (firstLine.isEmpty());
        int n = Integer.parseInt(firstLine);

        Solution handler = new Solution();
        List<Integer> results = new ArrayList<>();
        int count = 0;
        while (count < n) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s+");
            int id = Integer.parseInt(parts[0]);
            int priority = Integer.parseInt(parts[1]);
            double distance = Double.parseDouble(parts[2]);
            int estimatedTime = Integer.parseInt(parts[3]);
            int index = handler.addPackage(id, priority, distance, estimatedTime);
            results.add(index);
            count++;
        }
        scanner.close();

        System.out.println("\nOutput:");
        for (int result : results) {
            System.out.println(result);
        }
    }

    // ==================== ТЕСТЫ ====================

    @Test
    public void testBasicPrioritySorting() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(1, 2, 15.5, 30));
        assertEquals(0, handler.addPackage(2, 1, 10.0, 20));
        assertEquals(1, handler.addPackage(3, 2, 5.0, 25));
        assertEquals(2, handler.addPackage(4, 2, 15.5, 25));
    }

    @Test
    public void testMixedPriorities() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(1, 3, 10.0, 30));
        assertEquals(0, handler.addPackage(2, 1, 5.0, 20));
        assertEquals(1, handler.addPackage(3, 2, 8.0, 25));
        assertEquals(1, handler.addPackage(4, 1, 6.0, 15));
        assertEquals(3, handler.addPackage(5, 2, 12.0, 40));
        assertEquals(4, handler.addPackage(6, 3, 7.0, 35));
    }

    @Test
    public void testSamePriorityDifferentDistance() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(10, 2, 15.0, 50));
        assertEquals(0, handler.addPackage(20, 2, 10.0, 40));
        assertEquals(0, handler.addPackage(30, 2, 10.0, 30));
        assertEquals(0, handler.addPackage(40, 2, 5.0, 20));
        assertEquals(1, handler.addPackage(50, 2, 5.0, 25));
    }

    @Test
    public void testExtremePriorityDifferences() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(100, 1, 20.0, 60));
        assertEquals(1, handler.addPackage(200, 5, 1.0, 5));
        assertEquals(0, handler.addPackage(300, 1, 15.0, 45));
        assertEquals(3, handler.addPackage(400, 5, 2.0, 10));
        assertEquals(0, handler.addPackage(500, 1, 10.0, 30));
    }

    @Test
    public void testDescendingPriorities() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(80, 5, 25.0, 100));
        assertEquals(0, handler.addPackage(70, 4, 20.0, 80));
        assertEquals(0, handler.addPackage(60, 3, 15.0, 60));
        assertEquals(0, handler.addPackage(50, 2, 10.0, 40));
        assertEquals(0, handler.addPackage(40, 1, 5.0, 20));
    }

    @Test
    public void testAllSameParameters() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(1, 2, 10.0, 40));
        assertEquals(1, handler.addPackage(2, 2, 10.0, 40));
        assertEquals(2, handler.addPackage(3, 2, 10.0, 40));
        assertEquals(3, handler.addPackage(4, 2, 10.0, 40));
        assertEquals(4, handler.addPackage(5, 2, 10.0, 40));
    }

    @Test
    public void testSamePriorityVaryingDistances() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(1, 1, 0.1, 10));
        assertEquals(1, handler.addPackage(2, 1, 1000.0, 20));
        assertEquals(1, handler.addPackage(3, 1, 500.5, 15));
        assertEquals(1, handler.addPackage(4, 1, 0.9, 5));
        assertEquals(2, handler.addPackage(5, 1, 100.25, 25));
    }

    @Test
    public void testComplexMixedScenario() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(1, 3, 15.0, 45));
        assertEquals(0, handler.addPackage(2, 1, 8.0, 30));
        assertEquals(2, handler.addPackage(3, 4, 5.5, 20));
        assertEquals(1, handler.addPackage(4, 2, 15.0, 60));
        assertEquals(0, handler.addPackage(5, 1, 6.0, 25));
        assertEquals(3, handler.addPackage(6, 3, 9.5, 35));
        assertEquals(2, handler.addPackage(7, 2, 7.0, 28));
        assertEquals(7, handler.addPackage(8, 5, 18.0, 75));
        assertEquals(0, handler.addPackage(9, 1, 4.0, 15));
        assertEquals(8, handler.addPackage(10, 4, 11.0, 50));
    }

    @Test
    public void testSamePriorityAndDistance() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(1, 2, 15.0, 50));
        assertEquals(0, handler.addPackage(2, 2, 15.0, 30));
        assertEquals(1, handler.addPackage(3, 2, 15.0, 40));
        assertEquals(0, handler.addPackage(4, 2, 10.0, 25));
        assertEquals(4, handler.addPackage(5, 2, 20.0, 60));
        assertEquals(2, handler.addPackage(6, 2, 15.0, 35));
        assertEquals(2, handler.addPackage(7, 2, 15.0, 30));
    }

    @Test
    public void testSinglePackage() {
        Solution handler = new Solution();
        assertEquals(0, handler.addPackage(999, 3, 10.0, 30));


    }
}