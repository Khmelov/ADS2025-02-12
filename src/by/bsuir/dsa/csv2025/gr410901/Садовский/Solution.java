package by.bsuir.dsa.csv2025.gr410901.Садовский;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Solution {

    private int[][] adjacencyMatrix = new int[0][0];
    private int[][] mstEdges = new int[0][3];
    private int mstEdgeCount = 0;
    private int mstWeight = 0;

    public void runPrim(int startVertex) {
        int n = adjacencyMatrix.length;
        if (n == 0) {
            mstEdges = new int[0][3];
            mstEdgeCount = 0;
            mstWeight = 0;
            return;
        }
        if (startVertex < 0 || startVertex >= n) startVertex = 0;
        boolean[] used = new boolean[n];
        int[] minEdge = new int[n];
        int[] selEdge = new int[n];
        for (int i = 0; i < n; i++) {
            minEdge[i] = Integer.MAX_VALUE;
            selEdge[i] = -1;
        }
        minEdge[startVertex] = 0;
        mstEdges = new int[Math.max(0, n - 1)][3];
        mstEdgeCount = 0;
        mstWeight = 0;
        for (int i = 0; i < n; i++) {
            int v = -1;
            for (int j = 0; j < n; j++) {
                if (!used[j] && (v == -1 || minEdge[j] < minEdge[v])) v = j;
            }
            if (v == -1 || minEdge[v] == Integer.MAX_VALUE) break;
            used[v] = true;
            if (selEdge[v] != -1) {
                int u = selEdge[v];
                int w = adjacencyMatrix[u][v];
                mstEdges[mstEdgeCount][0] = u;
                mstEdges[mstEdgeCount][1] = v;
                mstEdges[mstEdgeCount][2] = w;
                mstEdgeCount++;
                mstWeight += w;
            }
            for (int to = 0; to < n; to++) {
                int weight = adjacencyMatrix[v][to];
                if (weight > 0 && !used[to] && weight < minEdge[to]) {
                    minEdge[to] = weight;
                    selEdge[to] = v;
                }
            }
        }
    }

    public void addVertex(int v) {
        int n = adjacencyMatrix.length;
        if (v < n) return;
        int newSize = v + 1;
        int[][] newMatrix = new int[newSize][newSize];
        for (int i = 0; i < n; i++) System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, n);
        adjacencyMatrix = newMatrix;
    }

    public void addEdge(int u, int v, int w) {
        int need = Math.max(u, v);
        addVertex(need);
        adjacencyMatrix[u][v] = w;
        adjacencyMatrix[v][u] = w;
    }

    public int vertexCount() {
        return adjacencyMatrix.length;
    }

    public int edgeCount() {
        int n = adjacencyMatrix.length;
        int c = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (adjacencyMatrix[i][j] > 0) c++;
        return c;
    }

    public int getMSTWeight() {
        return mstWeight;
    }

    public int[][] getMSTEdges() {
        int[][] res = new int[mstEdgeCount][3];
        for (int i = 0; i < mstEdgeCount; i++) {
            res[i][0] = mstEdges[i][0];
            res[i][1] = mstEdges[i][1];
            res[i][2] = mstEdges[i][2];
        }
        return res;
    }

    public String toString() {
        if (mstEdgeCount == 0) return "{}";
        int[][] copy = new int[mstEdgeCount][3];
        for (int i = 0; i < mstEdgeCount; i++) {
            copy[i][0] = mstEdges[i][0];
            copy[i][1] = mstEdges[i][1];
            copy[i][2] = mstEdges[i][2];
        }
        for (int i = 0; i < copy.length - 1; i++) {
            for (int j = 0; j < copy.length - i - 1; j++) {
                if (copy[j][2] > copy[j + 1][2]) {
                    int[] t = copy[j];
                    copy[j] = copy[j + 1];
                    copy[j + 1] = t;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < copy.length; i++) {
            sb.append(copy[i][0]).append("-").append(copy[i][1]).append("=").append(copy[i][2]);
            if (i < copy.length - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public void clear() {
        adjacencyMatrix = new int[0][0];
        mstEdges = new int[0][3];
        mstEdgeCount = 0;
        mstWeight = 0;
    }

    public boolean isEmpty() {
        return adjacencyMatrix.length == 0;
    }

    public boolean hasVertex(int v) {
        return v >= 0 && v < adjacencyMatrix.length;
    }

    public boolean hasEdge(int u, int v) {
        if (!hasVertex(u) || !hasVertex(v)) return false;
        return adjacencyMatrix[u][v] > 0;
    }

    public int[] getNeighbors(int v) {
        if (!hasVertex(v)) return new int[0];
        int n = adjacencyMatrix.length;
        int cnt = 0;
        for (int i = 0; i < n; i++) if (adjacencyMatrix[v][i] > 0) cnt++;
        int[] res = new int[cnt];
        int k = 0;
        for (int i = 0; i < n; i++) if (adjacencyMatrix[v][i] > 0) res[k++] = i;
        return res;
    }

    public int getEdgeWeight(int u, int v) {
        if (!hasVertex(u) || !hasVertex(v)) return 0;
        return adjacencyMatrix[u][v];
    }

    public static void main(String[] args) {
        Solution g = new Solution();
        g.addEdge(0, 1, 3);
        g.addEdge(1, 2, 5);
        g.addEdge(0, 2, 1);
        g.addEdge(2, 3, 4);
        g.addEdge(1, 3, 2);
        g.runPrim(0);
        System.out.println(g.getMSTWeight());
        System.out.println(g.toString());
    }

    @Test
    public void testPrim() {
        {
            Solution g = new Solution();
            g.addEdge(0, 1, 7);
            g.runPrim(0);
            assertEquals(7, g.getMSTWeight());
            assertEquals(1, g.getMSTEdges().length);
        }
        {
            Solution g = new Solution();
            g.addEdge(0, 1, 2);
            g.addEdge(1, 2, 2);
            g.addEdge(2, 3, 2);
            g.addEdge(3, 4, 2);
            g.runPrim(0);
            assertEquals(8, g.getMSTWeight());
            assertEquals(4, g.getMSTEdges().length);
        }
        {
            Solution g = new Solution();
            g.addEdge(0, 1, 10);
            g.addEdge(1, 2, 1);
            g.addEdge(0, 2, 5);
            g.runPrim(0);
            assertEquals(6, g.getMSTWeight());
            assertEquals(2, g.getMSTEdges().length);
        }
        {
            Solution g = new Solution();
            g.addEdge(0, 1, 3);
            g.addEdge(1, 2, 5);
            g.addEdge(0, 2, 1);
            g.addEdge(2, 3, 4);
            g.addEdge(1, 3, 2);
            g.runPrim(0);
            assertEquals(7, g.getMSTWeight());
            assertEquals(3, g.getMSTEdges().length);
        }
        {
            Solution g = new Solution();
            g.addEdge(0, 1, 1);
            g.addEdge(1, 2, 1);
            g.addEdge(2, 3, 1);
            g.addEdge(0, 3, 1);
            g.runPrim(0);
            assertEquals(3, g.getMSTWeight());
            assertEquals(3, g.getMSTEdges().length);
        }
        {
            Solution g = new Solution();
            g.addEdge(0, 1, 4);
            g.addEdge(1, 2, 8);
            g.addEdge(2, 3, 7);
            g.addEdge(3, 4, 9);
            g.addEdge(4, 5, 10);
            g.addEdge(5, 0, 2);
            g.runPrim(0);
            assertTrue(g.getMSTWeight() > 0);
            assertEquals(5, g.getMSTEdges().length);
        }
    }
}