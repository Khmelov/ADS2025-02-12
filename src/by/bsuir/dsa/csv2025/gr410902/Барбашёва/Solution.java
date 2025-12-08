package by.bsuir.dsa.csv2025.gr410902.Барбашёва;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class Solution {

    // Проверяет, можно ли завершить все курсы без цикличных зависимостей
    public static boolean canFinish(int n, int[][] prerequisites) {
        // Создаем граф курсов
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
        for (int[] p : prerequisites) graph[p[1]].add(p[0]);

        boolean[] visited = new boolean[n];  // Посещенные вершины
        boolean[] stack = new boolean[n];    // Вершины в текущем пути

        // Проверяем каждый курс на наличие циклов
        for (int i = 0; i < n; i++) {
            if (hasCycle(i, graph, visited, stack)) return false;
        }
        return true;
    }

    // Рекурсивно проверяет наличие цикла в графе
    private static boolean hasCycle(int node, List<Integer>[] graph, boolean[] visited, boolean[] stack) {
        if (stack[node]) return true;    // Найден цикл
        if (visited[node]) return false; // Уже проверяли эту вершину

        visited[node] = true;
        stack[node] = true;

        // Проверяем всех соседей
        for (int neighbor : graph[node]) {
            if (hasCycle(neighbor, graph, visited, stack)) return true;
        }

        stack[node] = false; // Убираем из текущего пути
        return false;
    }

    // Основной метод программы
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            String test_data = sc.nextLine();
            if (test_data.equals("")) return;
            boolean output_data = processInputFromString(test_data);
            System.out.println(output_data);
        } catch (Exception e) {
            System.out.println("false");
        } finally {
            sc.close();
        }
    }

    // Обрабатывает строку с входными данными
    public static boolean processInputFromString(String input) {
        Scanner scanner = new Scanner(input);
        try {
            int n = scanner.nextInt(); // Количество курсов
            int m = scanner.nextInt(); // Количество зависимостей

            int[][] prerequisites = new int[m][2];
            for (int i = 0; i < m; i++) {
                prerequisites[i][0] = scanner.nextInt(); // Курс
                prerequisites[i][1] = scanner.nextInt(); // Пререквизит
            }

            return canFinish(n, prerequisites);
        } finally {
            scanner.close();
        }
    }

    // Запускает тест с переданными входными данными
    public String runTest(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            Scanner sc = new Scanner(System.in);
            String test_data = sc.nextLine();
            sc.close();

            if (test_data.equals("")) return "false";
            boolean result = processInputFromString(test_data);
            return String.valueOf(result);
        } catch (Exception e) {
            return "false";
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    // Набор тестов для проверки решения
    @Test
    public void RunAllTests() throws Exception {
        assertEquals("true", runTest("4 3 1 0 2 1 3 2"));
        assertEquals("false", runTest("3 3 0 1 1 2 2 0"));
        assertEquals("true", runTest("2 1 1 0"));
        assertEquals("true", runTest("3 2 1 0 2 1"));
        assertEquals("false", runTest("4 4 1 0 2 1 3 2 1 3"));
        assertEquals("true", runTest("1 0"));
        assertEquals("true", runTest("3 0"));
        assertEquals("true", runTest("4 3 0 1 1 2 2 3"));
        assertEquals("true", runTest("3 3 0 1 1 2 0 2"));
        assertEquals("false", runTest("4 4 0 1 1 2 2 3 3 0"));

        System.out.println("ALL TESTS PASSED!");
    }
}