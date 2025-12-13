package by.bsuir.dsa.csv2025.gr451001.Стешиц;

import java.util.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            System.out.println("Некорректный ввод n");
            return;
        }
        int n = sc.nextInt();
        if (!sc.hasNextInt()) {
            System.out.println("Некорректный ввод m");
            return;
        }
        int m = sc.nextInt();
        ArrayList<Integer>[] g = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) g[i] = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (!sc.hasNextInt()) {
                System.out.println("Ожидалось целое u, ввод прерван.");
                return;
            }
            int u = sc.nextInt();
            if (!sc.hasNextInt()) {
                System.out.println("Ожидалось целое v, ввод прерван.");
                return;
            }
            int v = sc.nextInt();
            if (u >= 1 && u <= n) g[u].add(v);
        }
        if (!sc.hasNextInt()) {
            System.out.println("Некорректный ввод s");
            return;
        }
        int s = sc.nextInt();
        if (!sc.hasNextInt()) {
            System.out.println("Некорректный ввод t");
            return;
        }
        int t = sc.nextInt();
        for (int i = 1; i <= n; i++) {
            if (g[i].size() > 1) Collections.sort(g[i]);
        }
        boolean[] visited = new boolean[n + 1];
        int[] parent = new int[n + 1];
        Arrays.fill(parent, -1);
        ArrayList<Integer> order = new ArrayList<>();
        int[] nextIndex = new int[n + 1];
        Deque<Integer> stack = new ArrayDeque<>();
        if (s >= 1 && s <= n) {
            visited[s] = true;
            parent[s] = -1;
            order.add(s);
            stack.push(s);
            while (!stack.isEmpty()) {
                int u = stack.peek();
                boolean pushed = false;
                while (nextIndex[u] < g[u].size()) {
                    int v = g[u].get(nextIndex[u]);
                    nextIndex[u]++;
                    if (v >= 1 && v <= n && !visited[v]) {
                        visited[v] = true;
                        parent[v] = u;
                        order.add(v);
                        stack.push(v);
                        pushed = true;
                        break;
                    }
                }
                if (!pushed) {
                    stack.pop();
                }
            }
        } else {
            System.out.println("s вне диапазона 1..n — обход не выполнен.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("order");
        for (int x : order) {
            sb.append(" ").append(x);
        }
        sb.append("\n");
        if (t >= 1 && t <= n && visited[t]) {
            ArrayList<Integer> path = new ArrayList<>();
            int cur = t;
            while (cur != -1) {
                path.add(cur);
                cur = parent[cur];
            }
            Collections.reverse(path);
            sb.append("path");
            for (int x : path) sb.append(" ").append(x);
        } else {
            sb.append("path NO PATH");
        }

        System.out.print(sb.toString());
        sc.close();
    }

    private void runTest(String input, String expectedOutput) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            Solution.main(new String[0]);

            String actual = baos.toString().replace("\r\n", "\n");
            String expected = expectedOutput.replace("\r\n", "\n");

            assertEquals(expected, actual);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    public void test1() throws Exception {
        runTest("6 6\n" + "1 2\n" + "1 3\n" + "2 4\n" + "3 5\n" + "5 4\n" + "4 6\n" + "1 6",
                "order 1 2 4 6 3 5\n" + "path 1 2 4 6");
    }

    @Test
    public void test2() throws Exception {
        runTest("4 3\n" + "1 2\n" + "2 3\n" + "3 4\n" + "1 4",
                "order 1 2 3 4\n" + "path 1 2 3 4");
    }

    @Test
    public void test3() throws Exception {
        runTest("6 5\n" + "1 2\n" + "1 3\n" + "2 4\n" + "2 5\n" + "3 6\n" + "1 5",
                "order 1 2 4 5 3 6\n" + "path 1 2 5");
    }

    @Test
    public void test4() throws Exception {
        runTest("3 3\n" + "1 2\n" + "2 3\n" + "3 1\n" + "1 3",
                "order 1 2 3\n" + "path 1 2 3");
    }

    @Test
    public void test5() throws Exception {
        runTest("5 3\n" + "1 2\n" + "2 3\n" + "4 5\n" + "1 5",
                "order 1 2 3\n" + "path NO PATH");
    }
    @Test
    public void test6() throws Exception {
        runTest("4 5\n" + "1 2\n" + "1 2\n" + "2 2\n" + "2 3\n" + "3 4\n" + "1 4",
                "order 1 2 3 4\n" + "path 1 2 3 4");
    }

    @Test
    public void test7() throws Exception {
        runTest("3 2\n" + "2 3\n" + "3 1\n" + "1 1",
                "order 1\n" + "path 1");
    }

    @Test
    public void test8() throws Exception {
        runTest("7 7\n" + "1 3\n" + "1 2\n" + "2 5\n" + "2 4\n" + "3 6\n" + "6 7\n" + "5 7\n" + "1 6",
                "order 1 2 4 5 7 3 6\n" + "path 1 3 6");
    }

    @Test
    public void test9() throws Exception {
        runTest("6 6\n" + "1 2\n" + "1 4\n" + "2 3\n" + "3 1\n" + "4 5\n" + "5 6\n" + "1 6",
                "order 1 2 3 4 5 6\n" + "path 1 4 5 6");
    }

    @Test
    public void test10() throws Exception {
        runTest("9 9\n" + "1 2\n" + "2 3\n" + "3 4\n" + "2 5\n" + "5 6\n" + "6 7\n" + "7 3\n" + "4 8\n" + "8 9\n" + "1 9",
                "order 1 2 3 4 8 9 5 6 7\n" + "path 1 2 3 4 8 9");
    }
}