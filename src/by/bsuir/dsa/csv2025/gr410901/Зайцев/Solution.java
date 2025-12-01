package by.bsuir.dsa.csv2025.gr410901.Зайцев;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Solution {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            edges.add(new int[]{sc.nextInt(), sc.nextInt()});
        }
        List<Integer> cycle = findEulerianCycle(n, edges);
        if (cycle == null || cycle.size() != m + 1) {
            System.out.println("NO");
        } else {
            for (int v : cycle) System.out.print(v + " ");
        }
    }

    public static List<Integer> findEulerianCycle(int n, List<int[]> edges) {
        List<LinkedList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new LinkedList<>());
        for (int[] e : edges) {
            graph.get(e[0]).add(e[1]);
            graph.get(e[1]).add(e[0]);
        }

        for (LinkedList<Integer> adj : graph) {
            if (adj.size() % 2 != 0) return null;
        }

        Stack<Integer> stack = new Stack<>();
        List<Integer> cycle = new ArrayList<>();
        stack.push(0);

        while (!stack.isEmpty()) {
            int v = stack.peek();
            if (!graph.get(v).isEmpty()) {
                int u = graph.get(v).poll();
                graph.get(u).remove((Integer) v);
                stack.push(u);
            } else {
                cycle.add(stack.pop());
            }
        }

        Collections.reverse(cycle);
        return cycle;
    }

    @Test
    public void test1() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,2}, new int[]{2,0});
        List<Integer> cycle = findEulerianCycle(3, edges);
        assertEquals(4, cycle.size());
    }

    @Test
    public void test2() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,2}, new int[]{2,3}, new int[]{3,0});
        List<Integer> cycle = findEulerianCycle(4, edges);
        assertEquals(5, cycle.size());
    }

    @Test
    public void test3() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,2});
        List<Integer> cycle = findEulerianCycle(3, edges);
        assertNull(cycle);
    }

    @Test
    public void test4() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,2}, new int[]{2,0}, new int[]{0,3}, new int[]{3,1});
        List<Integer> cycle = findEulerianCycle(4, edges);
        assertNull(cycle);
    }

    @Test
    public void test5() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,2}, new int[]{2,3}, new int[]{3,0}, new int[]{0,2});
        List<Integer> cycle = findEulerianCycle(4, edges);
        assertNull(cycle);
    }

    @Test
    public void test6() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,2}, new int[]{2,0}, new int[]{0,3}, new int[]{3,2});
        List<Integer> cycle = findEulerianCycle(4, edges);
        assertNull(cycle);
    }

    @Test
    public void test7() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,2}, new int[]{2,0}, new int[]{0,3}, new int[]{3,4}, new int[]{4,0});
        List<Integer> cycle = findEulerianCycle(5, edges);
        assertEquals(7, cycle.size());
    }


    @Test
    public void test8() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,0});
        List<Integer> cycle = findEulerianCycle(2, edges);
        assertEquals(3, cycle.size());
    }

    @Test
    public void test9() {
        List<int[]> edges = Arrays.asList(new int[]{0,1});
        List<Integer> cycle = findEulerianCycle(2, edges);
        assertNull(cycle);
    }

    @Test
    public void test10() {
        List<int[]> edges = Arrays.asList(new int[]{0,1}, new int[]{1,2}, new int[]{2,3}, new int[]{3,4}, new int[]{4,0}, new int[]{0,2}, new int[]{1,3}, new int[]{2,4}, new int[]{3,0}, new int[]{4,1});
        List<Integer> cycle = findEulerianCycle(5, edges);
        assertEquals(11, cycle.size());
    }
}
