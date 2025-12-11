package by.bsuir.dsa.csv2025.gr410901.Томашевич;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

// Главный класс с программой и тестами в одном файле
class Solution {

    // Внутренний класс для ребра графа
    static class Edge implements Comparable<Edge> {
        int u, v;
        long weight;

        Edge(int u, int v, long weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Long.compare(this.weight, other.weight);
        }
    }

    // Класс для DSU (система непересекающихся множеств)
    static class DSU {
        int[] parent;
        int[] rank;
        int components;

        DSU(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            components = n;

            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false;
            }

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            components--;
            return true;
        }

        int getComponents() {
            return components;
        }
    }

    // Метод 1: Алгоритм Краскала
    public static long kruskalAlgorithm(int n, List<Edge> edges) {
        if (n == 0) return 0;
        if (n == 1) return 0;

        Collections.sort(edges);

        DSU dsu = new DSU(n);
        long totalWeight = 0;
        int edgesUsed = 0;

        for (Edge edge : edges) {
            if (dsu.union(edge.u, edge.v)) {
                totalWeight += edge.weight;
                edgesUsed++;

                if (edgesUsed == n - 1) {
                    break;
                }
            }
        }

        if (dsu.getComponents() != 1) {
            return -1; // Граф несвязный
        }

        return totalWeight;
    }

    // Метод 2: Парсинг входных данных
    public static List<Edge> parseInput(int n, int m, Scanner scanner) {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            long w = scanner.nextLong();
            edges.add(new Edge(u, v, w));
        }

        return edges;
    }

    // Метод 3: Форматирование результата
    public static String formatResult(long result) {
        if (result == -1) {
            return "GRAPH IS NOT CONNECTED";
        } else {
            return String.valueOf(result);
        }
    }

    // Главный метод
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Minimum Spanning Tree Calculator ===");
        System.out.println("Enter graph data:");

        // Чтение количества вершин и рёбер
        System.out.print("Enter number of vertices (n) and edges (m): ");
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        // Чтение рёбер
        System.out.println("Enter " + m + " edges (format: u v weight):");
        List<Edge> edges = parseInput(n, m, scanner);

        // Вычисление MST
        long result = kruskalAlgorithm(n, edges);

        // Вывод результата
        System.out.println("Result: " + formatResult(result));

        scanner.close();
    }
}
