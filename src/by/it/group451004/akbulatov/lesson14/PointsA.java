package by.it.group451004.akbulatov.lesson14;

import java.util.*;

public class PointsA
{
    static class DSU
    {
        private final int[] parent;
        private final int[] size;

        public DSU(int n)
        {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++)
            {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x)
        {
            if (parent[x] != x)
                parent[x] = find(parent[x]);

            return parent[x];
        }

        public void union(int x, int y)
        {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            if (size[rootX] < size[rootY])
            {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            }
            else
            {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        public int getClusterSize(int x)
        {
            return size[find(x)];
        }
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        double d = scanner.nextDouble();
        int n = scanner.nextInt();

        double[][] points = new double[n][3];
        for (int i = 0; i < n; i++)
        {
            points[i][0] = scanner.nextDouble();
            points[i][1] = scanner.nextDouble();
            points[i][2] = scanner.nextDouble();
        }

        DSU dsu = new DSU(n);

        for (int i = 0; i < n; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                double distance = calculateDistance(points[i], points[j]);
                if (distance < d) dsu.union(i, j);
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++)
        {
            int root = dsu.find(i);
            if (!visited[root])
            {
                clusterSizes.add(dsu.getClusterSize(root));
                visited[root] = true;
            }
        }

        Collections.sort(clusterSizes);

        for (int i = clusterSizes.size() - 1; i >= 0; i--)
        {
            System.out.print(clusterSizes.get(i));
            if (i > 0) System.out.print(" ");
        }
        System.out.println();
    }

    private static double calculateDistance(double[] p1, double[] p2)
    {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        double dz = p1[2] - p2[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}