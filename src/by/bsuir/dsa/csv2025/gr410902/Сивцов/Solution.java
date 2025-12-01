package by.bsuir.dsa.csv2025.gr410902.Сивцов;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    /**
     * Решение задачи коммивояжера с использованием динамического программирования
     * с запоминанием состояний (mask, last)
     */
    public int findMinRouteDistance(int n, int[][] distances) {
        // Количество точек включая склад: 0 (склад) + n точек доставки
        int totalPoints = n + 1;

        // dp[mask][last] - минимальное расстояние для посещения точек из mask,
        // заканчивая на точке last
        int[][] dp = new int[1 << totalPoints][totalPoints];

        // Инициализация значением "бесконечность"
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
        }

        // Начальное состояние: посетили только склад (точка 0)
        dp[1][0] = 0;

        // Перебор всех масок
        for (int mask = 1; mask < (1 << totalPoints); mask++) {
            for (int last = 0; last < totalPoints; last++) {
                // Если текущее состояние недостижимо, пропускаем
                if (dp[mask][last] == Integer.MAX_VALUE / 2) continue;

                // Пробуем перейти во все непосещенные точки
                for (int next = 0; next < totalPoints; next++) {
                    if ((mask & (1 << next)) != 0) continue; // уже посещена

                    int newMask = mask | (1 << next);
                    int newDistance = dp[mask][last] + distances[last][next];

                    if (newDistance < dp[newMask][next]) {
                        dp[newMask][next] = newDistance;
                    }
                }
            }
        }

        // Находим минимальное расстояние возврата на склад
        // Должны посетить все точки: склад (0) + n точек доставки
        int fullMask = (1 << totalPoints) - 1;
        int minDistance = Integer.MAX_VALUE;

        // Возвращаемся на склад из любой последней точки
        for (int last = 1; last < totalPoints; last++) {
            if (dp[fullMask][last] != Integer.MAX_VALUE / 2) {
                minDistance = Math.min(minDistance,
                        dp[fullMask][last] + distances[last][0]);
            }
        }

        return minDistance;
    }

    /**
     * Вспомогательный метод для создания тестовой матрицы расстояний
     */
    private int[][] createDistanceMatrix(int n, int[][] edges) {
        int totalPoints = n + 1;
        int[][] dist = new int[totalPoints][totalPoints];

        // Инициализация большими значениями
        for (int i = 0; i < totalPoints; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE / 2);
            dist[i][i] = 0;
        }

        // Заполнение заданными расстояниями
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            int distance = edge[2];
            dist[from][to] = distance;
            dist[to][from] = distance;
        }

        return dist;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int size = n + 1;
        int[][] distances = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                distances[i][j] = scanner.nextInt();
            }
        }

        int result = solution.findMinRouteDistance(n, distances);
        System.out.println(result);

        scanner.close();
    }

    @Test
    public void testDeliveryOptimization() {
        Solution solution = new Solution();

        // Тест 1: 1 точка доставки
        // Маршрут: 0 -> 1 -> 0 = 5 + 5 = 10
        int n1 = 1;
        int[][] dist1 = {
                {0, 5},
                {5, 0}
        };
        assertEquals(10, solution.findMinRouteDistance(n1, dist1));

        // Тест 2: 2 точки доставки - оптимальный маршрут 0->1->2->0 = 10 + 20 + 15 = 45
        int n2 = 2;
        int[][] dist2 = {
                {0, 10, 15},
                {10, 0, 20},
                {15, 20, 0}
        };
        assertEquals(45, solution.findMinRouteDistance(n2, dist2));

        // Тест 3: 2 точки доставки - другой маршрут
        // 0->1->2->0 = 5 + 8 + 6 = 19
        int n3 = 2;
        int[][] dist3 = {
                {0, 5, 6},
                {5, 0, 8},
                {6, 8, 0}
        };
        assertEquals(19, solution.findMinRouteDistance(n3, dist3));

        // Тест 4: 3 точки доставки
        // Оптимальный маршрут: 0->1->3->2->0 = 10 + 25 + 30 + 15 = 80
        int n4 = 3;
        int[][] dist4 = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
        assertEquals(80, solution.findMinRouteDistance(n4, dist4));

        // Тест 5: 3 точки доставки - все расстояния равны 1
        // Любой маршрут: 0->1->2->3->0 = 1+1+1+1 = 4
        int n5 = 3;
        int[][] dist5 = {
                {0, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
        };
        assertEquals(4, solution.findMinRouteDistance(n5, dist5));

        // Тест 6: 4 точки доставки - создаем через вспомогательный метод
        int n6 = 4;
        int[][] edges6 = {
                {0, 1, 2}, {0, 2, 9}, {0, 3, 10}, {0, 4, 5},
                {1, 2, 6}, {1, 3, 11}, {1, 4, 4},
                {2, 3, 8}, {2, 4, 7},
                {3, 4, 3}
        };
        int[][] dist6 = createDistanceMatrix(n6, edges6);
        int result6 = solution.findMinRouteDistance(n6, dist6);
        assertTrue(result6 > 0);

        // Правильный расчет для теста 6:
        // Рассмотрим возможные маршруты:
        // 0->1->4->3->2->0 = 2 + 4 + 3 + 8 + 9 = 26
        // 0->1->2->3->4->0 = 2 + 6 + 8 + 3 + 5 = 24
        // 0->4->3->2->1->0 = 5 + 3 + 8 + 6 + 2 = 24
        // 0->4->1->2->3->0 = 5 + 4 + 6 + 8 + 10 = 33
        // Минимальный: 24
        assertEquals(24, result6);

        // Тест 7: Линейное расположение точек
        // 0->1->2->3->0 = 1+2+3+6 = 12
        int n7 = 3;
        int[][] dist7 = {
                {0, 1, 3, 6},
                {1, 0, 2, 5},
                {3, 2, 0, 3},
                {6, 5, 3, 0}
        };
        assertEquals(12, solution.findMinRouteDistance(n7, dist7));

        // Тест 8: Звездообразное расположение
        // 0->1->2->3->0 = 2+4+4+2 = 12
        int n8 = 3;
        int[][] dist8 = {
                {0, 2, 2, 2},
                {2, 0, 4, 4},
                {2, 4, 0, 4},
                {2, 4, 4, 0}
        };
        assertEquals(12, solution.findMinRouteDistance(n8, dist8));

        // Тест 9: Большие расстояния
        // 0->1->2->0 = 100+150+200 = 450
        int n9 = 2;
        int[][] dist9 = {
                {0, 100, 200},
                {100, 0, 150},
                {200, 150, 0}
        };
        assertEquals(450, solution.findMinRouteDistance(n9, dist9));

        // Тест 10: Случай с минимальными расстояниями
        // 0->1->2->0 = 1+1+2 = 4
        int n10 = 2;
        int[][] dist10 = {
                {0, 1, 2},
                {1, 0, 1},
                {2, 1, 0}
        };
        assertEquals(4, solution.findMinRouteDistance(n10, dist10));
    }
}