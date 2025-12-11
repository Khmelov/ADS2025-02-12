package by.bsuir.dsa.csv2025.gr451004.Иванов;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class Solution {
    private static int timer = 0;
    
    public static List<int[]> findBridges(int n, List<int[]> edges) {
        // Создаём список смежности
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // Заполняем граф (используем индексы с 1)
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        boolean[] visited = new boolean[n + 1];
        int[] tin = new int[n + 1];  // время входа
        int[] low = new int[n + 1];  // минимальное время достижимости
        List<int[]> bridges = new ArrayList<>();
        
        timer = 0;
        
        // Запускаем DFS для каждой компоненты связности
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                dfs(i, -1, graph, visited, tin, low, bridges);
            }
        }
        
        // Сортируем мосты
        bridges.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });
        
        return bridges;
    }
    
    private static void dfs(int v, int parent, List<List<Integer>> graph, 
                           boolean[] visited, int[] tin, int[] low, 
                           List<int[]> bridges) {
        visited[v] = true;
        tin[v] = low[v] = ++timer;
        
        for (int to : graph.get(v)) {
            if (to == parent) {
                continue;
            }
            
            if (visited[to]) {
                // Обратное ребро - обновляем low
                low[v] = Math.min(low[v], tin[to]);
            } else {
                // Прямое ребро - рекурсивный вызов
                dfs(to, v, graph, visited, tin, low, bridges);
                low[v] = Math.min(low[v], low[to]);
                
                // Проверяем, является ли ребро мостом
                if (low[to] > tin[v]) {
                    // Ребро (v, to) - мост
                    int u = Math.min(v, to);
                    int w = Math.max(v, to);
                    bridges.add(new int[]{u, w});
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            edges.add(new int[]{u, v});
        }
        
        List<int[]> bridges = findBridges(n, edges);
        
        if (bridges.isEmpty()) {
            System.out.println(-1);
        } else {
            for (int[] bridge : bridges) {
                System.out.println(bridge[0] + " " + bridge[1]);
            }
        }
        
        scanner.close();
    }
    
    // ========== Unit Tests ==========
    
    private static List<String> bridgesToString(List<int[]> bridges) {
        List<String> result = new ArrayList<>();
        for (int[] bridge : bridges) {
            result.add(bridge[0] + " " + bridge[1]);
        }
        return result;
    }
    
    private static List<String> expectedToString(String[] expected) {
        return Arrays.asList(expected);
    }
    
    @Test
//    @DisplayName("Тест 1: Базовый тест с несколькими мостами")
    public void test1_BasicMultipleBridges() {
        int n = 7;
        List<int[]> edges = Arrays.asList(
            new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 6},
            new int[]{1, 4}, new int[]{4, 5}, new int[]{2, 5}, new int[]{5, 7}
        );
        List<int[]> result = findBridges(n, edges);
        List<String> resultStr = bridgesToString(result);
        List<String> expected = expectedToString(new String[]{"2 3", "3 6", "5 7"});
        assertEquals(expected, resultStr);
    }
    
    @Test
//    @DisplayName("Тест 2: Цикл без мостов")
    public void test2_CycleNoBridges() {
        int n = 4;
        List<int[]> edges = Arrays.asList(
            new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 4}, new int[]{4, 1}
        );
        List<int[]> result = findBridges(n, edges);
        assertTrue( "Должен вернуться пустой список для проверки вывода -1 в main",result.isEmpty());
    }
    
    @Test
//    @DisplayName("Тест 3: Простой путь - все рёбра мосты")
    public void test3_PathAllBridges() {
        int n = 5;
        List<int[]> edges = Arrays.asList(
            new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 4}, new int[]{4, 5}
        );
        List<int[]> result = findBridges(n, edges);
        List<String> resultStr = bridgesToString(result);
        List<String> expected = expectedToString(new String[]{"1 2", "2 3", "3 4", "4 5"});
        assertEquals(expected, resultStr);
    }
    
    @Test
//    @DisplayName("Тест 4: Дерево - все рёбра мосты")
    public void test4_TreeAllBridges() {
        int n = 6;
        List<int[]> edges = Arrays.asList(
            new int[]{1, 2}, new int[]{1, 3}, new int[]{2, 4}, 
            new int[]{2, 5}, new int[]{3, 6}
        );
        List<int[]> result = findBridges(n, edges);
        List<String> resultStr = bridgesToString(result);
        List<String> expected = expectedToString(new String[]{"1 2", "1 3", "2 4", "2 5", "3 6"});
        assertEquals(expected, resultStr);
    }
    
    @Test
//    @DisplayName("Тест 5: Две компоненты связности")
    public void test5_TwoConnectedComponents() {
        int n = 6;
        List<int[]> edges = Arrays.asList(
            new int[]{1, 2}, new int[]{2, 3}, new int[]{4, 5}, new int[]{5, 6}
        );
        List<int[]> result = findBridges(n, edges);
        List<String> resultStr = bridgesToString(result);
        List<String> expected = expectedToString(new String[]{"1 2", "2 3", "4 5", "5 6"});
        assertEquals(expected, resultStr);
    }
    
    @Test
//    @DisplayName("Тест 6: Граф с одним мостом в центре")
    public void test6_SingleBridgeInCenter() {
        int n = 6;
        List<int[]> edges = Arrays.asList(
            new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 1},
            new int[]{4, 5}, new int[]{5, 6}, new int[]{6, 4}, new int[]{2, 4}
        );
        List<int[]> result = findBridges(n, edges);
        List<String> resultStr = bridgesToString(result);
        List<String> expected = expectedToString(new String[]{"2 4"});
        assertEquals(expected, resultStr);
    }
    
    @Test
//    @DisplayName("Тест 7: Звезда с центром")
    public void test7_StarGraph() {
        int n = 5;
        List<int[]> edges = Arrays.asList(
            new int[]{1, 2}, new int[]{1, 3}, new int[]{1, 4}, new int[]{1, 5}
        );
        List<int[]> result = findBridges(n, edges);
        List<String> resultStr = bridgesToString(result);
        List<String> expected = expectedToString(new String[]{"1 2", "1 3", "1 4", "1 5"});
        assertEquals(expected, resultStr);
    }
    
    @Test
//    @DisplayName("Тест 8: Граф с несколькими циклами")
    public void test8_MultipleCycles() {
        int n = 8;
        List<int[]> edges = Arrays.asList(
            new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 4}, new int[]{4, 1},
            new int[]{5, 6}, new int[]{6, 7}, new int[]{7, 8}, new int[]{8, 5},
            new int[]{2, 5}
        );
        List<int[]> result = findBridges(n, edges);
        List<String> resultStr = bridgesToString(result);
        List<String> expected = expectedToString(new String[]{"2 5"});
        assertEquals(expected, resultStr);
    }
    
    @Test
//    @DisplayName("Тест 9: Одна вершина")
    public void test9_SingleVertex() {
        int n = 1;
        List<int[]> edges = new ArrayList<>();
        List<int[]> result = findBridges(n, edges);
        assertTrue("Должен вернуться пустой список для проверки вывода -1 в main",result.isEmpty());
    }
    
    @Test
//    @DisplayName("Тест 10: Две вершины с одним ребром")
    public void test10_TwoVerticesOneEdge() {
        int n = 2;
        List<int[]> edges = Arrays.asList(new int[]{1, 2});
        List<int[]> result = findBridges(n, edges);
        List<String> resultStr = bridgesToString(result);
        List<String> expected = expectedToString(new String[]{"1 2"});
        assertEquals(expected, resultStr);
    }
}


