package by.bsuir.dsa.csv2025.gr451004.Момотюк;

import java.util.*;
import java.io.*;

public class Solution {

    static class Query implements Comparable<Query> {
        int l, r, idx;
        Query(int l, int r, int idx) {
            this.l = l;
            this.r = r;
            this.idx = idx;
        }

        @Override
        public int compareTo(Query other) {
            int block1 = l / blockSize;
            int block2 = other.l / blockSize;
            if (block1 != block2) {
                return Integer.compare(block1, block2);
            }
            return Integer.compare(r, other.r);
        }
    }

    static int blockSize;
    static int distinctCount = 0;
    static Map<Integer, Integer> freq = new HashMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Выберите режим:");
        System.out.println("1 - Ввод данных");
        System.out.println("2 - Запуск тестов");
        System.out.print("Ваш выбор: ");

        String choice = br.readLine();

        if (choice.equals("1")) {
            runNormalMode();
        } else {
            runAllTests();
        }
    }

    static void runNormalMode() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            int n = Integer.parseInt(br.readLine().trim());
            int[] nums = new int[n];

            String[] numsStr = br.readLine().trim().split("\\s+");
            if (numsStr.length != n) {
                throw new IllegalArgumentException("Количество элементов не соответствует размеру массива");
            }
            for (int i = 0; i < n; i++) {
                nums[i] = Integer.parseInt(numsStr[i]);
            }

            int m = Integer.parseInt(br.readLine().trim());
            int[][] queries = new int[m][2];

            for (int i = 0; i < m; i++) {
                String[] queryStr = br.readLine().trim().split("\\s+");
                if (queryStr.length != 2) {
                    throw new IllegalArgumentException("Запрос должен содержать 2 числа");
                }
                queries[i][0] = Integer.parseInt(queryStr[0]);
                queries[i][1] = Integer.parseInt(queryStr[1]);

                if (queries[i][0] < 0 || queries[i][1] >= n || queries[i][0] > queries[i][1]) {
                    throw new IllegalArgumentException("Некорректные границы запроса: " + queries[i][0] + " " + queries[i][1]);
                }
            }

            int[] result = moAlgorithm(nums, queries);

            for (int i = 0; i < result.length; i++) {
                System.out.print(result[i]);
                if (i < result.length - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: неверный формат числа");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    static int[] moAlgorithm(int[] nums, int[][] queries) {
        int n = nums.length;
        int m = queries.length;
        if (n == 0) return new int[0];

        blockSize = (int) Math.sqrt(n);

        Query[] sortedQueries = new Query[m];
        for (int i = 0; i < m; i++) {
            sortedQueries[i] = new Query(queries[i][0], queries[i][1], i);
        }

        Arrays.sort(sortedQueries);

        int[] result = new int[m];
        int curL = 0, curR = -1;
        distinctCount = 0;
        freq.clear();

        for (Query q : sortedQueries) {
            while (curL > q.l) {
                curL--;
                addElement(nums[curL]);
            }
            while (curR < q.r) {
                curR++;
                addElement(nums[curR]);
            }
            while (curL < q.l) {
                removeElement(nums[curL]);
                curL++;
            }
            while (curR > q.r) {
                removeElement(nums[curR]);
                curR--;
            }
            result[q.idx] = distinctCount;
        }

        return result;
    }

    static void addElement(int x) {
        freq.put(x, freq.getOrDefault(x, 0) + 1);
        if (freq.get(x) == 1) {
            distinctCount++;
        }
    }

    static void removeElement(int x) {
        freq.put(x, freq.get(x) - 1);
        if (freq.get(x) == 0) {
            distinctCount--;
        }
    }

    static void runAllTests() {
        System.out.println("Запуск тестов алгоритма Мо...");
        System.out.println();

        int passed = 0;
        int failed = 0;

        if (testAllUniqueElements()) passed++; else failed++;
        if (testAllSameElements()) passed++; else failed++;
        if (testWithNegativeNumbers()) passed++; else failed++;
        if (testAlternatingElements()) passed++; else failed++;
        if (testOriginalExample()) passed++; else failed++;
        if (testSingleElement()) passed++; else failed++;
        if (testEmptyArray()) passed++; else failed++;

        System.out.println();
        System.out.println("ИТОГ:");
        System.out.println("Пройдено: " + passed);
        System.out.println("Провалено: " + failed);

        if (failed == 0) {
            System.out.println("ВСЕ ТЕСТЫ ПРОЙДЕНЫ!");
        } else {
            System.out.println("ЕСТЬ ОШИБКИ");
        }
    }

    static boolean arraysEqual(int[] a, int[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    static boolean runTest(String testName, int[] nums, int[][] queries, int[] expected) {
        System.out.print("Тест: " + testName + " ... ");

        try {
            int[] result = moAlgorithm(nums, queries);

            if (arraysEqual(result, expected)) {
                System.out.println("УСПЕХ");
                System.out.println("  Результат: " + Arrays.toString(result));
                return true;
            } else {
                System.out.println("ОШИБКА");
                System.out.println("  Ожидалось: " + Arrays.toString(expected));
                System.out.println("  Получено:  " + Arrays.toString(result));
                return false;
            }
        } catch (Exception e) {
            System.out.println("ИСКЛЮЧЕНИЕ: " + e.getMessage());
            return false;
        }
    }

    static boolean testAllUniqueElements() {
        int[] nums = {1, 2, 3, 4, 5};
        int[][] queries = {{0, 4}, {1, 3}, {2, 2}};
        int[] expected = {5, 3, 1};
        return runTest("Все уникальные элементы", nums, queries, expected);
    }

    static boolean testAllSameElements() {
        int[] nums = {1, 1, 1, 1, 1, 1};
        int[][] queries = {{0, 5}, {0, 2}, {3, 5}, {1, 4}};
        int[] expected = {1, 1, 1, 1};
        return runTest("Все одинаковые элементы", nums, queries, expected);
    }

    static boolean testWithNegativeNumbers() {
        int[] nums = {-1, 2, -1, 3, 2, -1, 0};
        int[][] queries = {{0, 6}, {0, 3}, {2, 5}, {1, 4}, {4, 6}};
        int[] expected = {4, 3, 3, 3, 3};
        return runTest("С отрицательными числами", nums, queries, expected);
    }

    static boolean testAlternatingElements() {
        int[] nums = {1, 2, 1, 2, 1, 2, 1, 2};
        int[][] queries = {{0, 7}, {0, 3}, {2, 5}, {1, 6}, {3, 7}, {4, 4}};
        int[] expected = {2, 2, 2, 2, 2, 1};
        return runTest("Чередующиеся элементы", nums, queries, expected);
    }

    static boolean testOriginalExample() {
        int[] nums = {1, 2, 3, 1, 2, 2, 4};
        int[][] queries = {{0, 3}, {1, 4}, {2, 5}, {3, 6}};
        int[] expected = {3, 3, 3, 3};
        return runTest("Оригинальный пример", nums, queries, expected);
    }

    static boolean testSingleElement() {
        int[] nums = {42};
        int[][] queries = {{0, 0}};
        int[] expected = {1};
        return runTest("Один элемент", nums, queries, expected);
    }

    static boolean testEmptyArray() {
        int[] nums = {};
        int[][] queries = {};
        int[] expected = {};
        return runTest("Пустой массив", nums, queries, expected);
    }
}