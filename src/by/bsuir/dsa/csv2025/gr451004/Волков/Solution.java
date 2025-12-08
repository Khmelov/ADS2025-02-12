package by.bsuir.dsa.csv2025.gr451004.Волков;

import java.util.*;
import java.io.*;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int s = scanner.nextInt();
        
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph.get(u).add(v);
        }
        
        List<Integer> result = bfs(graph, s, n);
        
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
    }
    
    public static List<Integer> bfs(List<List<Integer>> graph, int start, int n) {
        List<Integer> traversal = new ArrayList<>();
        boolean[] visited = new boolean[n + 1];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(start);
        visited[start] = true;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            traversal.add(current);
            
            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        
        return traversal;
    }
    
    static class Test {
        public static void main(String[] args) {
            runAllTests();
        }
        
        static void runAllTests() {
            test1();
            test2();
            test3();
            test4();
            test5();
            test6();
        }
        
        static void test1() {
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= 5; i++) graph.add(new ArrayList<>());
            graph.get(1).add(2);
            graph.get(1).add(3);
            graph.get(2).add(4);
            graph.get(3).add(5);
            
            List<Integer> result = bfs(graph, 1, 5);
            List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
            System.out.println("Test 1: " + (result.equals(expected) ? "PASS" : "FAIL"));
        }
        
        static void test2() {
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= 4; i++) graph.add(new ArrayList<>());
            graph.get(1).add(2);
            graph.get(2).add(3);
            graph.get(3).add(4);
            graph.get(4).add(1);
            
            List<Integer> result = bfs(graph, 2, 4);
            List<Integer> expected = Arrays.asList(2, 3, 4, 1);
            System.out.println("Test 2: " + (result.equals(expected) ? "PASS" : "FAIL"));
        }
        
        static void test3() {
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= 5; i++) graph.add(new ArrayList<>());
            graph.get(1).add(2);
            graph.get(3).add(4);
            
            List<Integer> result = bfs(graph, 1, 5);
            List<Integer> expected = Arrays.asList(1, 2);
            System.out.println("Test 3: " + (result.equals(expected) ? "PASS" : "FAIL"));
        }
        
        static void test4() {
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= 6; i++) graph.add(new ArrayList<>());
            graph.get(1).add(2);
            graph.get(1).add(3);
            graph.get(1).add(4);
            graph.get(1).add(5);
            graph.get(1).add(6);
            
            List<Integer> result = bfs(graph, 1, 6);
            List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6);
            System.out.println("Test 4: " + (result.equals(expected) ? "PASS" : "FAIL"));
        }
        
        static void test5() {
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= 1; i++) graph.add(new ArrayList<>());
            
            List<Integer> result = bfs(graph, 1, 1);
            List<Integer> expected = Arrays.asList(1);
            System.out.println("Test 5: " + (result.equals(expected) ? "PASS" : "FAIL"));
        }
        
        static void test6() {
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= 4; i++) graph.add(new ArrayList<>());
            graph.get(1).add(2);
            graph.get(1).add(3);
            graph.get(2).add(3);
            graph.get(2).add(4);
            graph.get(3).add(4);
            
            List<Integer> result = bfs(graph, 1, 4);
            List<Integer> expected = Arrays.asList(1, 2, 3, 4);
            System.out.println("Test 6: " + (result.equals(expected) ? "PASS" : "FAIL"));
        }
    }
}