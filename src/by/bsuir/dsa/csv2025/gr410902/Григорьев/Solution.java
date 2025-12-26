package by.bsuir.dsa.csv2025.gr410902.Григорьев;

import java.util.*;
import org.junit.*;
import java.io.*;
import static org.junit.Assert.assertEquals;

public class Solution {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();

        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        boolean[] visited = new boolean[n];
        List<List<Integer>> components = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                List<Integer> comp = new ArrayList<>();
                dfs(i, graph, visited, comp);
                components.add(comp);
            }
        }

        System.out.println(components.size());
        for (List<Integer> comp : components) {
            for (int i = 0; i < comp.size(); i++) {
                System.out.print(comp.get(i));
                if (i < comp.size() - 1) System.out.print(" ");
            }
            System.out.println();
        }
    }

    private static void dfs(int v, List<List<Integer>> graph, boolean[] visited, List<Integer> comp) {
        visited[v] = true;
        comp.add(v);
        for (int to : graph.get(v))
            if (!visited[to])
                dfs(to, graph, visited, comp);
    }

    @Test
    public void test1() {
        String input = "5 3\n0 1\n1 2\n3 4";
        String expected = "2\n0 1 2\n3 4";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test2() {
        String input = "4 0";
        String expected = "4\n0\n1\n2\n3";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test3() {
        String input = "6 5\n0 1\n1 2\n2 0\n3 4\n4 5";
        String expected = "2\n0 1 2\n3 4 5";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test4() {
        String input = "3 3\n0 1\n1 2\n2 0";
        String expected = "1\n0 1 2";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test5() {
        String input = "7 4\n0 1\n1 2\n3 4\n5 6";
        String expected = "3\n0 1 2\n3 4\n5 6";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test6() {
        String input = "5 2\n0 2\n1 3";
        String expected = "3\n0 2\n1 3\n4";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test7() {
        String input = "8 5\n0 1\n1 2\n2 3\n4 5\n6 7";
        String expected = "3\n0 1 2 3\n4 5\n6 7";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test8() {
        String input = "6 0";
        String expected = "6\n0\n1\n2\n3\n4\n5";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test9() {
        String input = "4 3\n0 1\n1 2\n2 3";
        String expected = "1\n0 1 2 3";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test10() {
        String input = "7 6\n0 1\n0 2\n1 2\n3 4\n4 5\n5 3";
        String expected = "3\n0 1 2\n3 4 5\n6";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }
}