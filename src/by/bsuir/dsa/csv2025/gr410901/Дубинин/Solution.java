package by.bsuir.dsa.csv2025.gr410901.Дубинин;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;


public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[][] pairs = new int[n][2];

        for (int i = 0; i < n; i++) {
            pairs[i][0] = scanner.nextInt();
            pairs[i][1] = scanner.nextInt();
        }

        int[][] result = validArrangement(pairs);

        for (int[] pair : result) {
            System.out.println(pair[0] + " " + pair[1]);
        }
    }

    public static int[][] validArrangement(int[][] pairs) {
        Map<Integer, PriorityQueue<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> degree = new HashMap<>();

        // Построение графа с использованием PriorityQueue для лексикографического порядка
        for (int[] pair : pairs) {
            int u = pair[0];
            int v = pair[1];

            graph.computeIfAbsent(u, k -> new PriorityQueue<>()).add(v);
            degree.put(u, degree.getOrDefault(u, 0) + 1);
            degree.put(v, degree.getOrDefault(v, 0) - 1);
        }

        // Поиск лексикографически наименьшей начальной вершины
        int start = findLexicographicallySmallestStart(degree, graph);

        // Алгоритм Хейерхолцера с лексикографическим порядком
        List<Integer> path = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int current = stack.peek();
            PriorityQueue<Integer> neighbors = graph.get(current);

            if (neighbors != null && !neighbors.isEmpty()) {
                // Извлекаем наименьшего соседа (лексикографический порядок)
                int next = neighbors.poll();
                stack.push(next);
            } else {
                path.add(stack.pop());
            }
        }

        Collections.reverse(path);

        // Формирование результата
        int[][] result = new int[pairs.length][2];
        for (int i = 0; i < pairs.length; i++) {
            result[i][0] = path.get(i);
            result[i][1] = path.get(i + 1);
        }

        return result;
    }

    private static int findLexicographicallySmallestStart(Map<Integer, Integer> degree,
                                                          Map<Integer, PriorityQueue<Integer>> graph) {
        int start = Integer.MAX_VALUE;
        boolean foundStartOfPath = false;

        // Сначала ищем вершины с out_degree = in_degree + 1 (начало Эйлерова пути)
        List<Integer> potentialStarts = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : degree.entrySet()) {
            if (entry.getValue() == 1) {
                potentialStarts.add(entry.getKey());
                foundStartOfPath = true;
            }
        }

        if (foundStartOfPath) {
            // Выбираем наименьшую вершину из потенциальных начал
            Collections.sort(potentialStarts);
            start = potentialStarts.get(0);
        } else {
            // Если все вершины сбалансированы (Эйлеров цикл), ищем наименьшую вершину с исходящими рёбрами
            for (Map.Entry<Integer, PriorityQueue<Integer>> entry : graph.entrySet()) {
                if (!entry.getValue().isEmpty() && entry.getKey() < start) {
                    start = entry.getKey();
                }
            }
        }

        return start;
    }

    @Test
    public void testEulerCycle() {
        int[][] pairs = {{1, 2}, {2, 3}, {3, 1}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 3}, {3, 1}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEulerPath() {
        int[][] pairs = {{1, 2}, {2, 3}, {3, 4}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 3}, {3, 4}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testLexicographicalOrder() {
        int[][] pairs = {{3, 1}, {1, 2}, {2, 3}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 3}, {3, 1}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSinglePair() {
        int[][] pairs = {{5, 10}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{5, 10}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testTwoPairsChain() {
        int[][] pairs = {{1, 2}, {2, 3}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 3}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testTwoPairsCycle() {
        int[][] pairs = {{1, 2}, {2, 1}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 1}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMultiplePaths() {
        int[][] pairs = {{1, 2}, {1, 3}, {2, 4}, {3, 4}, {4, 1}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 4}, {4, 1}, {1, 3}, {3, 4}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testLargeNumbers() {
        int[][] pairs = {{100, 200}, {200, 300}, {300, 100}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{100, 200}, {200, 300}, {300, 100}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNegativeNumbers() {
        int[][] pairs = {{-1, -2}, {-2, -3}, {-3, -1}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{-3, -1}, {-1, -2}, {-2, -3}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMixedNumbers() {
        int[][] pairs = {{-5, 10}, {10, 0}, {0, -5}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{-5, 10}, {10, 0}, {0, -5}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testStarStructure() {
        int[][] pairs = {{1, 2}, {1, 3}, {1, 4}, {2, 1}, {3, 1}, {4, 1}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 1}, {1, 3}, {3, 1}, {1, 4}, {4, 1}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testLongChain() {
        int[][] pairs = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMultipleConnections() {
        int[][] pairs = {{1, 2}, {2, 1}, {1, 3}, {3, 1}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 1}, {1, 3}, {3, 1}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testComplexGraph() {
        int[][] pairs = {{1, 2}, {2, 3}, {3, 1}, {1, 4}, {4, 5}, {5, 1}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 2}, {2, 3}, {3, 1}, {1, 4}, {4, 5}, {5, 1}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMaxLexicographical() {
        int[][] pairs = {{9, 1}, {1, 5}, {5, 9}, {9, 2}, {1, 9}};
        int[][] result = Solution.validArrangement(pairs);

        int[][] expected = {{1, 5}, {5, 9}, {9, 1}, {1, 9}, {9, 2}};
        assertArrayEquals(expected, result);
    }
}