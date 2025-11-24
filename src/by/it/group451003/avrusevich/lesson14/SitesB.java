package by.it.group451003.avrusevich.lesson14;

import java.util.*;

public class SitesB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DSU<String> disjointSet = new DSU<>();

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("end")) {
                break;
            }

            String[] sites = input.split("\\+");

            for (String site : sites) {
                if (!disjointSet.contains(site)) {
                    disjointSet.makeSet(site);
                }
            }

            disjointSet.union(sites[0], sites[1]);
        }

        scanner.close();

        Map<String, Integer> clusterSizes = new HashMap<>();
        HashSet<String> set = new HashSet<>();
        for (String site : disjointSet) {
            if (set.contains(site))
                continue;
            set.add(site);
            String root = disjointSet.findSet(site);
            clusterSizes.put(root, disjointSet.getClusterSize(site));
        }

        ArrayList<Integer> temp = new ArrayList<>();

        temp.addAll(clusterSizes.values());

        Collections.sort(temp);
        Collections.reverse(temp);

        for (int item : temp)
            System.out.print(item + " ");

    }
}