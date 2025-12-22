package by.bsuir.dsa.csv2025.gr451003.Каминский;

import java.util.*;
import java.io.*;

public class Solution {

    static class Edge {
        int u, v;
        long w;
        Edge(int u, int v, long w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    static class DSU {
        int[] parent, rank;

        DSU(int n) {
            parent = new int[n+1];
            rank = new int[n+1];
            for (int i = 1; i <= n; i++) parent[i] = i;
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a != b) {
                if (rank[a] < rank[b]) {
                    parent[a] = b;
                } else if (rank[b] < rank[a]) {
                    parent[b] = a;
                } else {
                    parent[b] = a;
                    rank[a]++;
                }
            }
        }
    }

    static ArrayList<Integer>[] graph;
    static boolean[] used;
    static int[] comp;
    static int compCount = 0;

    static void dfs(int v) {
        used[v] = true;
        comp[v] = compCount;
        for (int u : graph[v]) {
            if (!used[u]) dfs(u);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();

        graph = new ArrayList[n+1];
        for (int i = 1; i <= n; i++) graph[i] = new ArrayList<>();

        ArrayList<Edge> edges = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            long w = sc.nextLong();
            graph[u].add(v);
            graph[v].add(u);
            edges.add(new Edge(u, v, w));
        }
        used = new boolean[n+1];
        comp = new int[n+1];

        for (int i = 1; i <= n; i++) {
            if (!used[i]) {
                compCount++;
                dfs(i);
            }
        }
        ArrayList<Edge>[] compEdges = new ArrayList[compCount+1];
        for (int i = 1; i <= compCount; i++) compEdges[i] = new ArrayList<>();

        for (Edge e : edges) {
            int c1 = comp[e.u];
            int c2 = comp[e.v];
            if (c1 == c2) {
                compEdges[c1].add(e);
            }
        }
        long totalMST = 0;

        for (int c = 1; c <= compCount; c++) {
            DSU dsu = new DSU(n);

            ArrayList<Edge> list = compEdges[c];
            list.sort(Comparator.comparingLong(a -> a.w));

            for (Edge e : list) {
                if (dsu.find(e.u) != dsu.find(e.v)) {
                    dsu.union(e.u, e.v);
                    totalMST += e.w;
                }
            }
        }
        System.out.println(compCount);
        System.out.println(totalMST);
    }

    public static TestResult calculateMST(int n, List<Edge> edges) {

        ArrayList<Integer>[] testGraph = new ArrayList[n+1];
        for (int i = 1; i <= n; i++) testGraph[i] = new ArrayList<>();

        for (Edge e : edges) {
            testGraph[e.u].add(e.v);
            testGraph[e.v].add(e.u);
        }

        boolean[] testUsed = new boolean[n+1];
        int[] testComp = new int[n+1];
        int testCompCount = 0;

        for (int i = 1; i <= n; i++) {
            if (!testUsed[i]) {
                testCompCount++;
                dfsTest(i, testGraph, testUsed, testComp, testCompCount);
            }
        }

        ArrayList<Edge>[] testCompEdges = new ArrayList[testCompCount+1];
        for (int i = 1; i <= testCompCount; i++) testCompEdges[i] = new ArrayList<>();

        for (Edge e : edges) {
            int c1 = testComp[e.u];
            int c2 = testComp[e.v];
            if (c1 == c2) {
                testCompEdges[c1].add(e);
            }
        }

        long testTotalMST = 0;

        for (int c = 1; c <= testCompCount; c++) {
            DSU dsu = new DSU(n);

            ArrayList<Edge> list = testCompEdges[c];
            list.sort(Comparator.comparingLong(a -> a.w));

            for (Edge e : list) {
                if (dsu.find(e.u) != dsu.find(e.v)) {
                    dsu.union(e.u, e.v);
                    testTotalMST += e.w;
                }
            }
        }

        return new TestResult(testCompCount, testTotalMST);
    }
    private static void dfsTest(int v, ArrayList<Integer>[] testGraph,
                                boolean[] testUsed, int[] testComp, int currentComp) {
        testUsed[v] = true;
        testComp[v] = currentComp;
        for (int u : testGraph[v]) {
            if (!testUsed[u]) dfsTest(u, testGraph, testUsed, testComp, currentComp);
        }
    }

    public static class TestResult {
        public int componentCount;
        public long totalWeight;

        public TestResult(int componentCount, long totalWeight) {
            this.componentCount = componentCount;
            this.totalWeight = totalWeight;
        }
    }
}


