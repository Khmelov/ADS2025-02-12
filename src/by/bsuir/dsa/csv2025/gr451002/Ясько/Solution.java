package by.bsuir.dsa.csv2025.gr451002.Ясько;

import org.junit.Test;

import java.util.*;
import java.io.*;

import static org.junit.Assert.assertEquals;

public class Solution {
    // Основной алгоритм поиска минимального времени пути с учетом пересадок
    public String findMinTravelTime(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        // Основные параметры графа
        int n = scanner.nextInt(); // остановки
        int m = scanner.nextInt(); // маршруты
        int t = scanner.nextInt(); // типы транспорта

        // Время пересадки для каждого типа
        int[] transferTime = new int[t];
        for (int i = 0; i < t; i++) {
            transferTime[i] = scanner.nextInt();
        }

        // Построение графа
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Чтение маршрутов
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            int type = scanner.nextInt();
            // Неориентированный граф
            graph.get(u).add(new int[]{v, w, type});
            graph.get(v).add(new int[]{u, w, type});
        }

        // Начальная и конечная остановки
        int start = scanner.nextInt();
        int end = scanner.nextInt();

        // dist[вершина][транспорт] - минимальное время
        int[][] dist = new int[n][t];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        // Очередь с приоритетом: [вершина, время, транспорт]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);

        // Инициализация начальной вершины
        for (int transport = 0; transport < t; transport++) {
            dist[start][transport] = 0;
            pq.add(new int[]{start, 0, transport});
        }

        // Алгоритм Дейкстры
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int time = current[1];
            int lastTransport = current[2];

            if (time > dist[u][lastTransport]) continue;

            for (int[] route : graph.get(u)) {
                int v = route[0];
                int travelTime = route[1];
                int currentTransport = route[2];

                int totalTime = time + travelTime;

                // Добавляем пересадку при смене транспорта
                if (lastTransport != currentTransport) {
                    totalTime += transferTime[currentTransport];
                }

                if (totalTime < dist[v][currentTransport]) {
                    dist[v][currentTransport] = totalTime;
                    pq.add(new int[]{v, totalTime, currentTransport});
                }
            }
        }

        // Поиск минимального времени до конечной остановки
        int minTime = Integer.MAX_VALUE;
        for (int transport = 0; transport < t; transport++) {
            minTime = Math.min(minTime, dist[end][transport]);
        }

        scanner.close();
        return minTime == Integer.MAX_VALUE ? "-1" : String.valueOf(minTime);
    }

    // Точка входа в программу - чтение ввода и вывод результата
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input:");

        // Собираем все данные в одну строку
        StringBuilder input = new StringBuilder();

        // Читаем первую строку (N M T)
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            input.append(line).append("\n");
        }

        // Читаем вторую строку (transfer times)
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            input.append(line).append("\n");
        }

        // Читаем M строк маршрутов
        String firstLine = input.toString();
        String[] firstLineParts = firstLine.split("\\s+");
        int m = Integer.parseInt(firstLineParts[1]);

        for (int i = 0; i < m; i++) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                input.append(line).append("\n");
            }
        }

        // Читаем последнюю строку (start end)
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            input.append(line);
        }

        scanner.close();

        // Вычисляем и выводим результат
        Solution solver = new Solution();
        String result = solver.findMinTravelTime(input.toString());
        System.out.println("Output:");
        System.out.println(result);
    }

    // ТЕСТЫ

    @Test
    public void test1() {
        Solution solver = new Solution();
        assertEquals("21", solver.findMinTravelTime("3 2 2\n5 3\n0 1 10 0\n1 2 8 1\n0 2"));
    }

    @Test
    public void test2() {
        Solution solver = new Solution();
        assertEquals("7", solver.findMinTravelTime("4 4 2\n5 3\n0 1 6 0\n0 2 4 1\n1 3 7 0\n2 3 3 1\n0 3"));
    }

    @Test
    public void test3() {
        Solution solver = new Solution();
        assertEquals("10", solver.findMinTravelTime("3 3 2\n2 4\n0 1 5 0\n1 2 3 1\n0 2 10 0\n0 2"));
    }

    @Test
    public void test4() {
        Solution solver = new Solution();
        assertEquals("15", solver.findMinTravelTime("5 6 3\n3 5 2\n0 1 8 0\n0 2 6 1\n1 3 4 0\n2 3 5 2\n1 4 7 1\n3 4 3 0\n0 4"));
    }

    @Test
    public void test5() {
        Solution solver = new Solution();
        assertEquals("35", solver.findMinTravelTime("4 3 2\n4 6\n0 1 12 0\n1 2 8 1\n2 3 5 0\n0 3"));
    }

    @Test
    public void test6() {
        Solution solver = new Solution();
        assertEquals("25", solver.findMinTravelTime("3 2 2\n5 5\n0 1 15 0\n1 2 10 0\n0 2"));
    }

    @Test
    public void test7() {
        Solution solver = new Solution();
        assertEquals("11", solver.findMinTravelTime("4 4 2\n3 7\n0 1 5 0\n0 2 8 1\n1 3 6 0\n2 3 4 1\n0 3"));
    }

    @Test
    public void test8() {
        Solution solver = new Solution();
        assertEquals("-1", solver.findMinTravelTime("3 1 2\n2 3\n0 1 20 0\n0 2"));
    }

    @Test
    public void test9() {
        Solution solver = new Solution();
        assertEquals("10", solver.findMinTravelTime("2 1 1\n5\n0 1 10 0\n0 1"));
    }

    @Test
    public void test10() {
        Solution solver = new Solution();
        assertEquals("13", solver.findMinTravelTime("4 5 2\n4 6\n0 1 8 0\n0 2 5 1\n1 2 2 0\n1 3 6 1\n2 3 4 0\n0 3"));
    }
}