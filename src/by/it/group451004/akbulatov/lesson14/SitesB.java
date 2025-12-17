package by.it.group451004.akbulatov.lesson14;

import java.util.*;

public class SitesB
{
    static class DSU
    {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> rank = new HashMap<>();

        public String find(String x)
        {
            if (!parent.containsKey(x))
            {
                parent.put(x, x);
                rank.put(x, 0);
                return x;
            }

            if (!parent.get(x).equals(x))
                parent.put(x, find(parent.get(x)));

            return parent.get(x);
        }

        public void union(String x, String y)
        {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) return;

            if (rank.get(rootX) < rank.get(rootY))
            {
                parent.put(rootX, rootY);
            }
            else if (rank.get(rootX) > rank.get(rootY))
            {
                parent.put(rootY, rootX);
            }
            else
            {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }

        public List<Integer> getClusterSizes()
        {
            Map<String, Integer> clusterSizes = new HashMap<>();

            for (String site : parent.keySet())
                find(site);

            for (String site : parent.keySet())
            {
                String root = find(site);
                clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
            }

            List<Integer> sizes = new ArrayList<>(clusterSizes.values());
            Collections.sort(sizes);
            return sizes;
        }
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true)
        {
            String line = scanner.nextLine().trim();
            if (line.equals("end"))
                break;

            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();

                dsu.union(site1, site2);
            }
        }

        List<Integer> clusterSizes = dsu.getClusterSizes();

        for (int i = clusterSizes.size() - 1; i >= 0; i--)
        {
            System.out.print(clusterSizes.get(i));
            if (i > 0) System.out.print(" ");
        }
        scanner.close();
    }
}